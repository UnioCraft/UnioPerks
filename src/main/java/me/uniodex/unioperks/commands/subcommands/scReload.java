package me.uniodex.unioperks.commands.subcommands;

import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class scReload implements SubCommand {

    private UnioPerks plugin;
    private int minArgs = 1;
    private String name = "reload";
    private String permission = "unioperks.reload";
    private String usage = "";

    public scReload(UnioPerks plugin) {
        this.plugin = plugin;
        usage = plugin.getMessage("commands.reload.usage");
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
        plugin.reload();
        sender.sendMessage(plugin.getMessage("commands.reload.success"));
    }
}
