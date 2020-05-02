package me.uniodex.unioperks.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.uniodex.uniocustomitems.CustomItems;
import me.uniodex.uniocustomitems.managers.ItemManager;
import me.uniodex.unioperks.UnioPerks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Utils {

    public static String colorizeMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message.replaceAll("%hataPrefix%", UnioPerks.hataPrefix).replaceAll("%bilgiPrefix%", UnioPerks.bilgiPrefix).replaceAll("%dikkatPrefix%", UnioPerks.dikkatPrefix).replaceAll("%prefix%", UnioPerks.bilgiPrefix));
    }

    public static List<String> colorizeMessages(List<String> messages) {
        List<String> newList = new ArrayList<>();
        for (String msg : messages) {
            newList.add(ChatColor.translateAlternateColorCodes('&', msg.replaceAll("%hataPrefix%", UnioPerks.hataPrefix).replaceAll("%bilgiPrefix%", UnioPerks.bilgiPrefix).replaceAll("%dikkatPrefix%", UnioPerks.dikkatPrefix).replaceAll("%prefix%", UnioPerks.bilgiPrefix)));
        }
        return newList;
    }

    public static String secondsToString(Long seconds) {
        long second = seconds % 60;
        long minute = seconds % 3600 / 60;
        long hours = seconds % 86400 / 3600;
        long day = seconds / 86400;

        if (day == 0 && minute == 0 && hours == 0) {
            return second + " saniye";
        }

        if (day == 0 && hours == 0) {
            return minute + " dakika, " + second + " saniye";
        }

        if (day == 0) {
            return hours + " saat, " + minute + " dakika, " + second + " saniye";
        }

        return day + " gün, " + hours + " saat, " + minute + " dakika, " + second + " saniye";
    }

    public static boolean isTool(ItemStack item) {
        if (item == null) return false;
        if (!Enchantment.DURABILITY.canEnchantItem(item)) return false;
        return Stream.of("helmet", "chestplate", "leggings", "boots").noneMatch(s -> item.getType().toString().toLowerCase().contains(s)); // Returns false if armor, true otherwise.
    }

    public static boolean isMultiblock(ItemStack item) {
        return CustomItems.instance.itemManager.isItemNamed(item) &&
                item.getItemMeta().getDisplayName()
                        .equals(CustomItems.instance.itemManager.getItem(ItemManager.Items.buyuluAlanKazmasi).getItemMeta().getDisplayName());
    }

    public static boolean shouldDamage(ItemStack item) {
        return isTool(item) && !isMultiblock(item);
    }

    public static boolean isThereEnoughSpace(ItemStack item, Inventory inventory) {
        int freeSpace = 0;
        for (ItemStack i : inventory) {
            if (i == null) {
                freeSpace += item.getType().getMaxStackSize();
            } else if (i.getType() == item.getType()) {
                freeSpace += i.getType().getMaxStackSize() - i.getAmount();
            }
        }
        return item.getAmount() <= freeSpace;
    }

    public static boolean isThereEnoughSpace(List<ItemStack> items, Inventory inventory) {
        for (ItemStack item : items) {
            if (!isThereEnoughSpace(item, inventory)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLocationInArea(Location location, String area) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) return false;

        List<String> regionIds = new ArrayList<>();
        RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(location.getWorld());
        ApplicableRegionSet regionsAtLocation = regionManager.getApplicableRegions(location);

        for (ProtectedRegion region : regionsAtLocation) {
            regionIds.add(region.getId());
        }

        return regionIds.contains(area);
    }

    public static boolean isGlass(Block block) {
        return block.getType().toString().toLowerCase().contains("glass");
    }
}
