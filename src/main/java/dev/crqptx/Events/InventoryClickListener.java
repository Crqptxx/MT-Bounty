package dev.crqptx.Events;

import dev.crqptx.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    Main cfg;

    public InventoryClickListener(Main instance) {cfg = instance; }

    @EventHandler
    public void guiClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String menuName = cfg.getConfig().getString("Menu-Name");
        if (event.getView().getTitle().equalsIgnoreCase(menuName)) {
            event.setCancelled(true);
        }

    }


}
