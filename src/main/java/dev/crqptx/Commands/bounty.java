package dev.crqptx.Commands;
import dev.crqptx.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.Arrays;

public class bounty implements CommandExecutor {

    Main cfg;



    public bounty(Main instance) {cfg = instance; }

    public Inventory inv;

    public void createInventory(Player player) {
        String Menu = cfg.getConfig().getString("Menu-Name");
        inv = Bukkit.createInventory(null, 54, Menu);


        for (String username : cfg.getConfig().getConfigurationSection("bounties").getKeys(false)) {
            String head = username;
            int bounty = cfg.getConfig().getInt("bounties." + username);
            ItemStack item = cfg.getItem(head);
            String lore = "§bBounty: " + "§f" + bounty;
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setLore(Arrays.asList(lore));
            meta.setDisplayName("§b" + username);
            item.setItemMeta(meta);
            inv.addItem(item);
        }
        player.openInventory(inv);
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (args.length == 0) {
            sender.sendMessage("§c------------------------------------------------------");
            sender.sendMessage("§fMade by Crqptx");
            sender.sendMessage(" ");
            sender.sendMessage("§c/bounty menu §7- §fOpent het Bounty Menu");
            sender.sendMessage("§c/bounty set §4<player> §4<bedrag> §7- §fZet een bounty op een speler.");
            sender.sendMessage("§c/bounty remove §4<player> §7- §fVerwijder een bounty van een speler.");
            sender.sendMessage("§c/bounty info §4<player> §7- §fToon de bounty van een speler.");
            sender.sendMessage("§c------------------------------------------------------");
            return true;
        }


        if (args[0].equalsIgnoreCase("set")) {

            if (args.length == 0) {
                sender.sendMessage("§c------------------------------------------------------");
                sender.sendMessage("§c/bounty set §4<player> §4<bedrag> §7- §fZet een bounty op een speler.");
                sender.sendMessage("§c------------------------------------------------------");
                return true;
            }

            // Get the balance from the sender, check if they have enought money, if they don't tell them, else withdraw arg 2 from their balance.
            Economy econ = cfg.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
            String sender1 = sender.getName();
            String amount = args[2];
            if (econ.getBalance(sender1) < Integer.parseInt(amount)) {
                sender.sendMessage("§cJe hebt niet genoeg geld om deze bounty te zetten.");
                return true;
            } else {
                // Withdraw the balance of arg 2 from the player that executed command
                econ.withdrawPlayer(sender1, Integer.parseInt(amount));
            }

            if (sender.getName().equalsIgnoreCase(args[1])) {
                sender.sendMessage("§cJe kan geen bounty op jezelf zetten!");
                return true;
            }


            if (!Bukkit.getServer().getPlayer(args[1]).isOnline()) {
                sender.sendMessage("§cDeze speler is niet online!");
                return true;
            }




            if (!Character.isUpperCase(args[1].charAt(0))) {
                args[1] = args[1].substring(0, 1).toUpperCase() + args[1].substring(1);

            }


            if (args.length == 3) {
                if (cfg.getConfig().getString("bounties." + args[1]) != null) {
                    cfg.getConfig().set("bounties." + args[1], cfg.getConfig().getInt("bounties." + args[1]) + Integer.parseInt(args[2]));
                    cfg.saveConfig();
                    cfg.getServer().broadcastMessage("§c§lBOUNTY §f: De bounty van: " + "§4§l " + args[1] + " §fis verhoogd met: " + "§4§l€" + args[2] + " §fen staat nu op: " + "§4§l€" + cfg.getConfig().getInt("bounties." + args[1]));
                    return true;
                }
                cfg.getConfig().set("bounties." + args[1], Integer.parseInt(args[2]));
                cfg.saveConfig();
                sender.sendMessage("§cDe bounty is ingesteld op: " + "§4 " + args[1] + " §cmet waarde van: " + "§4€" + args[2]);

                cfg.getServer().broadcastMessage("§c§lBOUNTY §f: Er is een bounty gezet op: " + "§4§l " + args[1] + " §fmet een waarde van: " + "§4§l€" + args[2] + " §fdoor: " + "§4§l" + sender.getName());
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("§c------------------------------------------------------");
            sender.sendMessage("§fMade by Crqptx");
            sender.sendMessage(" ");
            sender.sendMessage("§c/bounty set §4<player> §4<bedrag> §7- §fZet een bounty op een speler.");
            sender.sendMessage("§c/bounty menu §7- §fOpent het Bounty Menu");
            sender.sendMessage("§c/bounty remove §4<player> §7- §fVerwijder een bounty van een speler.");
            sender.sendMessage("§c/bounty info §4<player> §7- §fToon de bounty van een speler.");
            sender.sendMessage("§c------------------------------------------------------");
            return true;
        }
        if (args[0].equalsIgnoreCase("menu")) {
            if (cfg.getConfig().getConfigurationSection("bounties").getKeys(false).isEmpty()) {
                sender.sendMessage("§cHelaas is er geen bounty om te laten zien!");
                return true;
            }
            createInventory((Player) sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 1) {
                sender.sendMessage("§cJe moet een speler opgeven!");
                return true;
            }

            if (!Character.isUpperCase(args[1].charAt(0))) {
                args[1] = args[1].substring(0, 1).toUpperCase() + args[1].substring(1);
            }

            if (cfg.getServer().getPlayer(args[1]) != null) {
                sender.sendMessage("§cDe bounty van " + args[1] + " staat op: " + "§4€"  + cfg.getConfig().getInt("bounties." + args[1]));
                return true;
            }
            sender.sendMessage("§cDeze speler heeft geen bounty.");
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (sender.hasPermission("bounty.remove" )) {
                if (args.length == 1) {
                    sender.sendMessage("§cJe moet een speler opgeven!");
                    return true;
                }
                if (cfg.getConfig().getString("bounties." + args[1]) != null) {
                    cfg.getConfig().set("bounties." + args[1], null);
                    cfg.saveConfig();
                    sender.sendMessage("§cDe bounty van: " + "§4" + args[1] + " §cis verwijderd!");
                    return true;
                }
                sender.sendMessage("§cDeze speler heeft geen bounty.");
                return true;
            }
        }


        return true;
    }
}
