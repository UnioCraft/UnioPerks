package me.uniodex.unioperks.managers;

import lombok.Getter;
import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.managers.ConfigManager.Config;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class FlyManager {

    private UnioPerks plugin;
    private HashMap<Player, Long> flyStartTimes = new HashMap<>();
    @Getter
    private HashMap<Player, Long> flyTimes = new HashMap<>();
    private HashMap<String, Long> flyData = new HashMap<>();
    @Getter
    private String unlimitedFlyPermission = "unioperks.fly.bypass";

    public FlyManager(UnioPerks plugin) {
        this.plugin = plugin;
        loadData();

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (flyStartTimes.containsKey(player) && flyTimes.containsKey(player)) {
                    if (!player.getAllowFlight()) {
                        stopFly(player);
                        continue;
                    }
                    long timeSpent = (System.currentTimeMillis() / 1000) - flyStartTimes.get(player);
                    long flyTime = (flyTimes.get(player) - timeSpent);
                    if (flyTime <= 0) {
                        stopFly(player);
                        takeFly(player.getName());
                        player.sendMessage(plugin.getMessage("perkTypes.fly.flightTimeExpired"));
                    } else if (flyTime <= 10) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(plugin.getMessage("perkTypes.fly.flightTimeRemaining").replaceAll("%flyTime%", String.valueOf(flyTime))));
                    }
                }
            }
        }, 20L, 20L);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> saveData(), 20L, 6000L);
    }

    private void loadData() {
        for (String player : plugin.getConfigManager().getConfig(Config.FLYDATA).getKeys(false)) {
            flyData.put(player, plugin.getConfigManager().getConfig(Config.FLYDATA).getLong(player));
        }
    }

    public void loadFly(Player p) {
        if (p != null) {
            if (flyData.containsKey(p.getName())) {
                Long l = flyData.get(p.getName());
                if (!Objects.isNull(l) && l > 0) {
                    flyTimes.put(p, l);
                }
            }
        }
    }

    public void saveFly(Player player) {
        if (flyTimes.containsKey(player)) {
            if (flyStartTimes.containsKey(player)) {
                long timeSpent = (System.currentTimeMillis() / 1000) - flyStartTimes.get(player);
                long flyTime = (flyTimes.get(player) - timeSpent);
                if (flyTime <= 0) {
                    stopFly(player);
                    takeFly(player.getName());
                } else {
                    flyData.put(player.getName(), flyTimes.get(player));
                }
            } else {
                flyData.put(player.getName(), flyTimes.get(player));
            }
        }
    }

    public boolean startFly(Player player) {
        if (player.hasPermission(unlimitedFlyPermission)) {
            player.setAllowFlight(true);
            player.setFlying(true);
            return true;
        }

        loadFly(player);
        if (flyTimes.containsKey(player)) {
            if (flyTimes.get(player) > 0) {
                flyStartTimes.put(player, System.currentTimeMillis() / 1000);
                player.setAllowFlight(true);
                player.setFlying(true);
                return true;
            }
        }
        return false;
    }

    public void stopFly(Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setFallDistance(0f);

        if (player.hasPermission(unlimitedFlyPermission)) {
            if (flyStartTimes.containsKey(player) || flyTimes.containsKey(player)) {
                takeFly(player.getName());
            }
            return;
        }

        if (flyStartTimes.containsKey(player) && flyTimes.containsKey(player)) {
            long timeSpent = (System.currentTimeMillis() / 1000) - flyStartTimes.get(player);
            long flyTime = (flyTimes.get(player) - timeSpent);
            if (flyTime > 0) {
                flyStartTimes.remove(player);
                flyTimes.remove(player);
                flyTimes.put(player, flyTime);

                flyData.put(player.getName(), flyTimes.get(player));
            }
        }
    }

    public void giveFly(String player, Long duration) {
        Player p = Bukkit.getPlayer(player);
        if (p != null && p.isOnline()) {
            loadFly(p);
            if (flyTimes.containsKey(p)) {
                long flyTime = flyTimes.get(p);
                flyTimes.put(p, flyTime + duration);

                flyData.put(player, flyTimes.get(p));
            } else {
                flyTimes.put(p, duration);

                flyData.put(player, flyTimes.get(p));
            }
        } else {
            Long l = plugin.getConfigManager().getConfig(Config.FLYDATA).getLong(player);
            if (l > 0) {
                flyData.put(player, l + duration);
            } else {
                flyData.put(player, duration);
            }
        }
    }

    public void takeFly(String player) {
        Player p = Bukkit.getPlayer(player);
        if (p != null && p.isOnline()) {
            if (flyTimes.containsKey(p)) {
                flyTimes.remove(p);
            }
            if (flyStartTimes.containsKey(p)) {
                flyStartTimes.remove(p);
            }
        }

        if (flyData.containsKey(player)) {
            flyData.remove(player);
        }
    }

    private void saveData() {
        for (String player : plugin.getConfigManager().getConfig(Config.FLYDATA).getKeys(false)) {
            plugin.getConfigManager().getConfig(Config.FLYDATA).set(player, null);
        }

        for (String player : flyData.keySet()) {
            plugin.getConfigManager().getConfig(Config.FLYDATA).set(player, flyData.get(player));
        }

        plugin.getConfigManager().saveConfig(Config.FLYDATA);
    }

    public boolean hasFly(Player player) {
        loadFly(player);
        if (flyTimes.containsKey(player)) {
            if (flyTimes.get(player) > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isFlying(Player p) {
        return (flyStartTimes.containsKey(p) && flyTimes.containsKey(p));
    }

    public void onDisable() {
        for (Player p : flyStartTimes.keySet()) {
            stopFly(p);
        }
        for (Player p : flyTimes.keySet()) {
            saveFly(p);
        }
        saveData();
    }

    public void onJoin(Player p) {
        loadFly(p);
    }

    public void onQuit(Player p) {
        stopFly(p);
        if (flyTimes.containsKey(p)) {
            if (flyTimes.get(p) <= 1) {
                takeFly(p.getName());
                return;
            }
            flyTimes.remove(p);
        }
        if (flyStartTimes.containsKey(p)) {
            flyStartTimes.remove(p);
        }
    }
}
