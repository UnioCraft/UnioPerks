package me.uniodex.unioperks.commands.subcommands;

import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class scAutoCollect implements SubCommand {

    private UnioPerks plugin;
    private int minArgs = 1;
    private String name = "autocollect";
    private String permission = "unioperks.autocollect";
    private String usage;

    public scAutoCollect(UnioPerks plugin) {
        this.plugin = plugin;
        usage = plugin.getMessage("commands.autocollect.usage");
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
        if (plugin.getDropControlManager().isInAutoCollectMode(player)) {
            plugin.getDropControlManager().setAutoCollect(player, false);
            sender.sendMessage(plugin.getMessage("commands.autoCollect.toggledOff"));
        } else {
            plugin.getDropControlManager().setAutoCollect(player, true);
            sender.sendMessage(plugin.getMessage("commands.autoCollect.toggledOn"));
        }
    }
}
