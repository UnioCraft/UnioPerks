package me.uniodex.unioperks.commands.subcommands;

import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class scClearPotions implements SubCommand {

    private UnioPerks plugin;
    private int minArgs = 1;
    private String name = "clearpotions";
    private String permission = "";
    private String usage;

    public scClearPotions(UnioPerks plugin) {
        this.plugin = plugin;
        usage = plugin.getMessage("commands.clearpotions.usage");
    }

    public int getMinArgs() {
        return minArgs;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return usage;
    }

    public void run(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("commands.playerOnly"));
            return;
        }
        Player player = (Player) sender;
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        player.sendMessage(plugin.getMessage("commands.clearpotions.success"));
    }
}
