package me.uniodex.unioperks.commands.subcommands;

import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.commands.SubCommand;
import me.uniodex.unioperks.perks.PerkPotion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class scPotion implements SubCommand {

    private UnioPerks plugin;
    private int minArgs = 2;
    private String name = "potion";
    private String permission = "";
    private String usage;

    public scPotion(UnioPerks plugin) {
        this.plugin = plugin;
        usage = plugin.getMessage("commands.potion.usage");
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

        for (PerkPotion perkPotion : plugin.getPerkManager().getPotionPerks().values()) {
            if (args[1].equalsIgnoreCase(perkPotion.getPerkName())) {
                perkPotion.run((Player) sender);
                return;
            }
        }
    }
}
