package me.uniodex.unioperks.commands.subcommands;

import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.commands.SubCommand;
import me.uniodex.unioperks.perks.PerkFly;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class scFly implements SubCommand {

    private UnioPerks plugin;
    private int minArgs = 1;
    private String name = "fly";
    private String permission = "unioperks.fly";
    private String usage = "";

    public scFly(UnioPerks plugin) {
        this.plugin = plugin;
        usage = plugin.getMessage("commands.fly.usage");
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
        if (sender instanceof Player) {
            PerkFly.getInstance().run((Player) sender);
            return;
        }
        sender.sendMessage(plugin.getMessage("commands.playerOnly"));
    }
}
