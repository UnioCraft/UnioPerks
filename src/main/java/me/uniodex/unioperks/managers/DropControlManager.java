package me.uniodex.unioperks.managers;

import com.earth2me.essentials.Essentials;
import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.utils.Utils;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.*;

public class DropControlManager {

    private UnioPerks plugin;
    private List<String> autoSellPlayers = new ArrayList<>();
    private List<String> autoCollectPlayers = new ArrayList<>();

    public DropControlManager(UnioPerks plugin) {
        this.plugin = plugin;
    }

    public boolean isInAutoSellMode(Player player) {
        return autoSellPlayers.contains(player.getName());
    }

    public boolean isInAutoCollectMode(Player player) {
        return autoCollectPlayers.contains(player.getName());
    }

    public void setAutoSell(Player player, boolean toggledOn) {
        if (toggledOn && !autoSellPlayers.contains(player.getName())) {
            autoSellPlayers.add(player.getName());
        } else {
            autoSellPlayers.remove(player.getName());
        }
    }

    public void setAutoCollect(Player player, boolean toggledOn) {
        if (toggledOn && !autoCollectPlayers.contains(player.getName())) {
            autoCollectPlayers.add(player.getName());
        } else {
            autoCollectPlayers.remove(player.getName());
        }
    }

    private double getPrice(Player player, ItemStack item) {
        if (plugin.getShopGui() != null) {
            return ShopGuiPlusApi.getItemStackPriceSell(player, item);
        } else if (plugin.getEssentials() != null) {
            Essentials essentials = (Essentials) plugin.getEssentials();
            BigDecimal worth = essentials.getWorth().getPrice(essentials, item);
            if (worth != null) {
                return worth.doubleValue() * item.getAmount();
            }
        }
        return -1;
    }

    public boolean autoSell(Player player, List<ItemStack> items) {
        if (items == null || items.isEmpty()) {
            return false;
        }
        boolean sold = false;
        for (ItemStack selling : items) {
            if (getPrice(player, selling) <= 0.0) {
                continue;
            }
            double total = getPrice(player, selling);
            if (plugin.getVaultHook().pay(player, total)) {
                sold = true;
            }
        }
        return sold;
    }

    public boolean isBlockIgnored(Block block) {
        boolean whitelistMode = plugin.getConfig().getBoolean("dropControl.useBlacklistAsWhitelist");

        List<String> blocks = plugin.getConfig().getStringList("dropControl.blacklist");
        if (blocks == null) return whitelistMode;
        if (blocks.contains(block.getType().toString())) {
            return !whitelistMode;
        }
        return whitelistMode;
    }

    public void damageItem(Player player, ItemStack item) {
        if (item.containsEnchantment(Enchantment.DURABILITY)) {
            int level = item.getEnchantmentLevel(Enchantment.DURABILITY);
            if (new Random().nextInt(level) != 0) {
                return;
            }
        }

        item.setDurability((short) (item.getDurability() + 1));

        if (item.getDurability() >= item.getType().getMaxDurability()) {
            player.getInventory().remove(item);
            player.updateInventory();
        }
    }

    private boolean shouldGiveExp(Location location) {
        if (Bukkit.getPluginManager().isPluginEnabled("USB3")) {
            if (Utils.isLocationInArea(location, "maden")) {
                return false;
            }
        }
        return true;
    }

    public void giveExp(Player p, Location location, int xp) {
        if (shouldGiveExp(location)) {
            if (Utils.isMultiblock(p.getInventory().getItemInMainHand())) {
                p.giveExp(new Random().nextInt(8) + 1);
            } else {
                p.giveExp(xp);
            }
        }
    }

    public List<ItemStack> getBlockDrops(Player player, Block block) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        Collection<ItemStack> drops;

        if (block.getDrops(itemInHand).size() > 0) {
            drops = block.getDrops(itemInHand);
        } else {
            drops = block.getDrops();
        }

        if (Utils.isGlass(block) && itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
            drops = Arrays.asList(new ItemStack(block.getType(), 1, block.getState().getData().getData()));
        }

        List<ItemStack> finalDrops = new ArrayList<>();
        for (ItemStack item : drops) {
            ItemStack itemClone = new ItemStack(item);

            if (Utils.isTool(itemInHand)) {
                if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
                    if (block.getType() == Material.GLOWING_REDSTONE_ORE || block.getType() == Material.REDSTONE_ORE) {
                        itemClone.setAmount(1);
                        itemClone.setType(Material.REDSTONE_ORE);
                        finalDrops.add(itemClone);
                        break;
                    } else {
                        itemClone.setAmount(1);
                        itemClone.setType(block.getType());
                        finalDrops.add(itemClone);
                        break;
                    }
                }

                if (itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                    int level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

                    boolean setFortune = false;
                    for (String material : plugin.getConfig().getStringList("dropControl.fortuneBlocks")) {
                        if (material.equalsIgnoreCase(block.getType().name())) {
                            setFortune = true;
                            break;
                        }
                    }

                    if (setFortune) {
                        itemClone.setAmount(new Random().nextInt(level) + 1);
                    }
                }
            }
            finalDrops.add(itemClone);
        }

        return finalDrops;
    }
}
