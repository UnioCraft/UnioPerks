package me.uniodex.unioperks.commands;

import lombok.Getter;
import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class CommandUnioPerks implements CommandExecutor {

    private UnioPerks plugin;

    @Getter
    private Map<String, SubCommand> subCommands = new HashMap<>();

    public CommandUnioPerks(UnioPerks plugin) {
        this.plugin = plugin;
        initializeSubCommand();
    }

    private void initializeSubCommand() {
        scReload scReload = new scReload(plugin);
        scClearPotions scClearPotions = new scClearPotions(plugin);
        scPotion scPotion = new scPotion(plugin);
        scFly scFly = new scFly(plugin);
        scAutoCollect scAutoCollect = new scAutoCollect(plugin);
        scAutoSell scAutoSell = new scAutoSell(plugin);
        scGiveFly scGiveFly = new scGiveFly(plugin);

        subCommands.put(scReload.getName(), scReload);
        subCommands.put(scClearPotions.getName(), scClearPotions);
        subCommands.put(scPotion.getName(), scPotion);
        subCommands.put(scFly.getName(), scFly);
        subCommands.put(scAutoCollect.getName(), scAutoCollect);
        subCommands.put(scAutoSell.getName(), scAutoSell);
        subCommands.put(scGiveFly.getName(), scGiveFly);

        plugin.getUnioEssentials().getCommandManager().addCommandInstance(scClearPotions.getName(), "unioperks " + scClearPotions.getName());
        plugin.getUnioEssentials().getCommandManager().addCommandInstance(scAutoCollect.getName(), "unioperks " + scAutoCollect.getName());
        plugin.getUnioEssentials().getCommandManager().addCommandInstance(scAutoSell.getName(), "unioperks " + scAutoSell.getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(plugin.getMessage("commands.invalidCommand"));
            return true;
        }

        for (SubCommand subCommand : subCommands.values()) {
            if (!args[0].equalsIgnoreCase(subCommand.getName())) {
                continue;
            }

            if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
                sender.sendMessage(plugin.getMessage("general.noPermission"));
                return true;
            }

            if (args.length < subCommand.getMinArgs()) {
                sender.sendMessage(subCommand.getUsage());
                return true;
            }

            subCommand.run(sender, command, label, args);
            return true;
        }
        return true;
    }
}
