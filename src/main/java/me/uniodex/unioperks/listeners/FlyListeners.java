package me.uniodex.unioperks.listeners;

import me.uniodex.unioperks.UnioPerks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FlyListeners implements Listener {

    private UnioPerks plugin;

    public FlyListeners(UnioPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getFlyManager().onJoin(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getFlyManager().onQuit(player);
    }

    @EventHandler
    public void flyToggle(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission(plugin.getFlyManager().getUnlimitedFlyPermission()) && plugin.getFlyManager().isFlying(player)) {
            plugin.getFlyManager().stopFly(player);
        }
    }
}
