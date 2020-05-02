package me.uniodex.unioperks.hooks;

import lombok.Getter;
import me.uniodex.unioperks.UnioPerks;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private UnioPerks plugin;
    @Getter
    private Economy economy;

    public VaultHook(UnioPerks plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }

    public boolean pay(OfflinePlayer p, double amount) {
        EconomyResponse econResp = economy.depositPlayer(p, amount);
        return econResp.transactionSuccess();
    }

    public double getBalance(OfflinePlayer p) {
        return economy.getBalance(p);
    }
}
