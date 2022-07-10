package dev.crqptx.Events;

import dev.crqptx.SkyMTBounties;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class PlayerDeathEvent implements Listener {
    SkyMTBounties cfg;

    public PlayerDeathEvent(SkyMTBounties instance) {cfg = instance; }

   @EventHandler
    public void onPlayerDeathEvent(org.bukkit.event.entity.PlayerDeathEvent event) {


        if (cfg.getConfig().getString("bounties." + event.getEntity().getName()) != null) {

            Economy econ = cfg.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
            String bountyAmount = cfg.getConfig().getInt("bounties." + event.getEntity().getName()) + "";

            EconomyResponse response = econ.depositPlayer(event.getEntity().getKiller(), Double.parseDouble(bountyAmount));


            event.getEntity().getKiller().sendMessage("§c§lBOUNTY §f: §fJe hebt de bounty van " + "§4§l" + event.getEntity().getName() + " §fgeclaimed en je hebt het volgende bedrag ontvangen: " + "§4€" + cfg.getConfig().getInt("bounties." + event.getEntity().getName()));
            cfg.getConfig().set("bounties." + event.getEntity().getName(), null);
            cfg.saveConfig();


            cfg.getServer().broadcastMessage("§c§lBOUNTY §fvan: " + "§4§l" + event.getEntity().getName() + "§f is geclaimed door: " + "§4§l"  + event.getEntity().getKiller().getName());




        }

    }










}
