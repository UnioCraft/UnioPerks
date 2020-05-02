package me.uniodex.unioperks.listeners;

import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import me.uniodex.unioperks.UnioPerks;
import me.uniodex.unioperks.managers.DropControlManager;
import me.uniodex.unioperks.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DropControlListeners implements Listener {

    private UnioPerks plugin;
    private DropControlManager dropControlManager;

    public DropControlListeners(UnioPerks plugin) {
        this.plugin = plugin;
        this.dropControlManager = plugin.getDropControlManager();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        dropControlManager.setAutoCollect(player, false);
        dropControlManager.setAutoSell(player, false);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();

        if (player.getGameMode().equals(GameMode.CREATIVE)) return;

        if (plugin.getConfig().getStringList("dropControl.disabledWorlds").contains(player.getWorld().getName()))
            return;

        if (dropControlManager.isBlockIgnored(block)) return;

        List<ItemStack> drops = dropControlManager.getBlockDrops(player, block);
        if (drops.isEmpty()) return;

        boolean shouldGiveExp = false;

        if (dropControlManager.isInAutoSellMode(player)) {
            if (dropControlManager.autoSell(player, drops)) {
                if (Utils.shouldDamage(itemInHand)) {
                    dropControlManager.damageItem(player, itemInHand);
                }

                shouldGiveExp = true;
                block.setType(Material.AIR);
            }
        } else if (dropControlManager.isInAutoCollectMode(player)) {
            if (Utils.isThereEnoughSpace(drops, player.getInventory())) {
                if (Utils.shouldDamage(itemInHand)) {
                    dropControlManager.damageItem(player, itemInHand);
                }

                shouldGiveExp = true;
                block.setType(Material.AIR);

                for (ItemStack i : drops) {
                    InventoryWorkaround.addItems(player.getInventory(), i);
                    if (i.getType().equals(Material.WHEAT)) {
                        InventoryWorkaround.addItems(player.getInventory(), new ItemStack(Material.SEEDS, 1));
                    }
                }
                player.updateInventory();
            }
        }

        if (shouldGiveExp) {
            if (block.getType().toString().contains("ORE")) {
                dropControlManager.giveExp(player, block.getLocation(), event.getExpToDrop());
            }
        }
    }
}
