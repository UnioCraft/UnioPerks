package me.uniodex.unioperks.managers;

import lombok.Getter;
import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.perks.PerkPotion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerkManager {

    private UnioPerks plugin;

    @Getter
    private Map<String, PerkPotion> potionPerks = new HashMap<>();

    public PerkManager(UnioPerks plugin) {
        this.plugin = plugin;
        initializePotionPerks();
    }

    private void initializePotionPerks() {
        for (String perkName : plugin.getConfig().getConfigurationSection("potionPerks").getKeys(false)) {
            potionPerks.put(perkName,
                    new PerkPotion(perkName,
                            plugin.getConfig().getString("potionPerks." + perkName + ".command"),
                            plugin.getConfig().getString("potionPerks." + perkName + ".potion"),
                            plugin.getConfig().getInt("potionPerks." + perkName + ".potionLevel"),
                            plugin.getConfig().getInt("potionPerks." + perkName + ".potionLength"),
                            plugin.getConfig().getLong("potionPerks." + perkName + ".cooldown")));
            plugin.getUnioEssentials().getCommandManager().addCommandInstance(
                    plugin.getConfig().getString("potionPerks." + perkName + ".command"),
                    "unioperks potion " + perkName);
            List<String> aliases = plugin.getConfig().getStringList("potionPerks." + perkName + ".aliases");
            for (String alias : aliases) {
                plugin.getUnioEssentials().getCommandManager().addCommandInstance(alias, "unioperks potion " + perkName);
            }
        }
    }
}
