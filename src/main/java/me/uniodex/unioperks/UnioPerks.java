package me.uniodex.unioperks;

import lombok.Getter;
import me.uniodex.unioessentials.UnioEssentials;
import me.uniodex.unioperks.commands.CommandFly;
import me.uniodex.unioperks.commands.CommandUnioPerks;
import me.uniodex.unioperks.hooks.VaultHook;
import me.uniodex.unioperks.listeners.DropControlListeners;
import me.uniodex.unioperks.listeners.FlyListeners;
import me.uniodex.unioperks.managers.ConfigManager;
import me.uniodex.unioperks.managers.ConfigManager.Config;
import me.uniodex.unioperks.managers.DropControlManager;
import me.uniodex.unioperks.managers.FlyManager;
import me.uniodex.unioperks.managers.PerkManager;
import me.uniodex.unioperks.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class UnioPerks extends JavaPlugin {

    @Getter
    private ConfigManager configManager;
    @Getter
    private FlyManager flyManager;
    @Getter
    private PerkManager perkManager;
    @Getter
    private DropControlManager dropControlManager;
    @Getter
    private static UnioPerks instance;

    @Getter
    private Plugin shopGui;
    @Getter
    private Plugin essentials;
    @Getter
    private Plugin unioCustomItems;
    @Getter
    private VaultHook vaultHook;
    @Getter
    private UnioEssentials unioEssentials;

    public static String hataPrefix;
    public static String dikkatPrefix;
    public static String bilgiPrefix;
    public static String consolePrefix;

    @Getter
    private CommandUnioPerks commandUnioPerks;

    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        initializePrefixes();

        // Plugins
        unioEssentials = (UnioEssentials) Bukkit.getPluginManager().getPlugin("UnioEssentials");
        shopGui = Bukkit.getPluginManager().getPlugin("ShopGUIPlus");
        essentials = Bukkit.getPluginManager().getPlugin("Essentials");
        unioCustomItems = Bukkit.getPluginManager().getPlugin("UnioCustomItems");
        vaultHook = new VaultHook(this);

        // Managers
        flyManager = new FlyManager(this);
        dropControlManager = new DropControlManager(this);
        perkManager = new PerkManager(this);

        // Listeners
        Bukkit.getPluginManager().registerEvents(new FlyListeners(this), this);
        Bukkit.getPluginManager().registerEvents(new DropControlListeners(this), this);

        // Commands
        getCommand("unioperks").setExecutor(commandUnioPerks = new CommandUnioPerks(this));
        getCommand("fly").setExecutor(new CommandFly(this));
    }

    public void onDisable() {
        flyManager.onDisable();
    }

    private void initializePrefixes() {
        bilgiPrefix = getMessage("prefix.bilgiPrefix");
        dikkatPrefix = getMessage("prefix.dikkatPrefix");
        hataPrefix = getMessage("prefix.hataPrefix");
        consolePrefix = getMessage("prefix.consolePrefix");
    }

    public String getMessage(String configSection) {
        if (configManager.getConfig(Config.LANG).getString(configSection) == null) return null;

        return Utils.colorizeMessage(configManager.getConfig(Config.LANG).getString(configSection));
    }

    public List<String> getMessages(String configSection) {
        if (configManager.getConfig(Config.LANG).getStringList(configSection) == null) return null;

        return Utils.colorizeMessages(configManager.getConfig(Config.LANG).getStringList(configSection));
    }

    public void reload() {
        reloadConfig();
    }
}
