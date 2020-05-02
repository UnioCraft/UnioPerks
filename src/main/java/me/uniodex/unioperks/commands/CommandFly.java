package me.uniodex.unioperks.commands;

import me.uniodex.unioperks.UnioPerks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandFly implements CommandExecutor {

    private UnioPerks plugin;

    public CommandFly(UnioPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getCommandUnioPerks().getSubCommands().get("fly").run(sender, command, label, args);
        return true;
    }
}
