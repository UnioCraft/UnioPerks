package me.uniodex.unioperks.perks;

import lombok.Getter;
import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PerkFly {

    private UnioPerks plugin;
    @Getter
    private static PerkFly instance;

    public PerkFly(UnioPerks plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public void run(Player player) {
        if (player.hasPermission(plugin.getFlyManager().getUnlimitedFlyPermission()) || plugin.getFlyManager().hasFly(player)) {
            if (player.getAllowFlight()) {
                plugin.getFlyManager().stopFly(player);
                Long flyTime = plugin.getFlyManager().getFlyTimes().get(player);
                if (!Objects.isNull(flyTime) && !player.hasPermission(plugin.getFlyManager().getUnlimitedFlyPermission())) {
                    player.sendMessage(plugin.getMessage("perkTypes.fly.flightDisabledWithTime").replaceAll("%time%", Utils.secondsToString(flyTime)));
                } else {
                    player.sendMessage(plugin.getMessage("perkTypes.fly.flightDisabled"));
                }
            } else {
                if (plugin.getFlyManager().startFly(player)) {
                    Long flyTime = plugin.getFlyManager().getFlyTimes().get(player);
                    if (!Objects.isNull(flyTime) && !player.hasPermission(plugin.getFlyManager().getUnlimitedFlyPermission())) {
                        player.sendMessage(plugin.getMessage("perkTypes.fly.flightEnabledWithTime").replaceAll("%time%", Utils.secondsToString(flyTime)));
                    } else {
                        player.sendMessage(plugin.getMessage("perkTypes.fly.flightEnabled"));
                    }
                } else {
                    player.sendMessage(plugin.getMessage("perkTypes.fly.noFlightTime"));
                }
            }
        } else {
            player.sendMessage(plugin.getMessage("perkTypes.fly.noFlightTime"));
        }
    }
}
