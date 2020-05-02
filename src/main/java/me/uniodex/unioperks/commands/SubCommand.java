package me.uniodex.unioperks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface SubCommand {
    int getMinArgs();

    String getName();

    String getPermission();

    String getUsage();

    void run(CommandSender sender, Command command, String label, String[] args);
}
