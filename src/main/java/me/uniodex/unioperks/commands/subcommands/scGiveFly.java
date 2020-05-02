package me.uniodex.unioperks.commands.subcommands;

import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class scGiveFly implements SubCommand {

    private UnioPerks plugin;
    private int minArgs = 3;
    private String name = "givefly";
    private String permission = "unioperks.givefly";
    private String usage = "";

    public scGiveFly(UnioPerks plugin) {
        this.plugin = plugin;
        usage = plugin.getMessage("commands.givefly.usage");
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
        if (Bukkit.getOfflinePlayer(args[1]) != null) {
            String playerName = args[1];
            long duration = 0;
            try {
                duration = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sender.sendMessage(plugin.getMessage("commands.givefly.badnumber"));
                return;
            }
            plugin.getFlyManager().giveFly(playerName, duration);
            sender.sendMessage(plugin.getMessage("commands.givefly.success"));
        } else {
            sender.sendMessage(plugin.getMessage("commands.givefly.playernotexist"));
        }
    }
}
