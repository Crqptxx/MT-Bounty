package dev.crqptx;

import dev.crqptx.Commands.bounty;
import dev.crqptx.Events.InventoryClickListener;
import dev.crqptx.Events.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import java.util.logging.Logger;

public final class SkyMTBounties extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;


    @Override
    public void onEnable() {
        // Plugin startup logic

        getLogger().info(ChatColor.translateAlternateColorCodes('&', "<text>"));
        getServer().getPluginManager().registerEvents(new PlayerDeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getCommand("bounty").setExecutor(new bounty(this));

        // Laad config.
        getConfig().options().copyDefaults();
        saveDefaultConfig();


        if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
            getLogger().severe("Vault is not enabled, disabling plugin.");

            for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                if (all.isOp()) {
                    all.sendMessage("§c§lVAULT §f: §4Vault is not enabled!, Please enable or install vault!");
                }
            }

            getServer().getPluginManager().disablePlugin(this);
            return;
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ItemStack getItem(String head) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(head);
        item.setItemMeta(meta);
        return item;
    }

    public HumanEntity getPlayer() {
        return getServer().getPlayer("");
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }
}
