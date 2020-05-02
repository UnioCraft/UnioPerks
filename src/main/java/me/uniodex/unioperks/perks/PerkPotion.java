package me.uniodex.unioperks.perks;

import lombok.Getter;
import me.uniodex.unioperks.UnioPerks;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class PerkPotion {

    @Getter
    private String perkName;
    @Getter
    private String perkCommand;
    @Getter
    private String potionType;
    @Getter
    private int potionLevel;
    @Getter
    private int potionLength;
    @Getter
    private long cooldown;

    private Map<String, Long> potionActivationTimes = new HashMap<>();

    public PerkPotion(String perkName, String perkCommand, String potionType, int potionLevel, int potionLength, long cooldown) {
        this.perkName = perkName;
        this.perkCommand = perkCommand;
        this.potionType = potionType;
        this.potionLevel = potionLevel;
        this.potionLength = potionLength;
        this.cooldown = cooldown;
    }

    private PotionEffect getPotionEffect() {
        return new PotionEffect(PotionEffectType.getByName(potionType), 20 * potionLength, potionLevel);
    }

    private void applyPotion(Player player) {
        player.addPotionEffect(getPotionEffect());
    }

    private long getCooldown(Player player) {
        if (potionActivationTimes.get(player.getName()) != null) {
            return potionActivationTimes.get(player.getName()) + (cooldown * 1000) - System.currentTimeMillis();
        }
        return -1;
    }

    public void run(Player player) {
        long cooldown = getCooldown(player);
        if (cooldown <= 0) {
            potionActivationTimes.put(player.getName(), System.currentTimeMillis());
            applyPotion(player);
            player.sendMessage(UnioPerks.getInstance().getMessage("perks.potions." + perkName + ".enabled"));
        } else {
            player.sendMessage(UnioPerks.getInstance().getMessage("perks.inCooldown").replaceAll("%seconds%", String.valueOf(cooldown / 1000)));
        }

    }
}
