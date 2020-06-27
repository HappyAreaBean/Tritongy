package net.islandearth.languagy.listener;

import net.islandearth.languagy.ui.UI;
import net.islandearth.languagy.ui.UI.ItemClick;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class InventoryListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent ice) {
		if (ice.getWhoClicked() instanceof Player) {
			Player player = (Player) ice.getWhoClicked();
			UUID uuid = UI.getOpen().get(player.getUniqueId());
			if (uuid != null) {
				ice.setCancelled(true);
				UI ui = UI.getInventories().get(uuid);
				ItemClick action = ui.getActions().get(ice.getSlot());
				
				if (action != null) {
					action.click(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent ice) {
		if (ice.getPlayer() instanceof Player) {
			Player player = (Player) ice.getPlayer();
			UI ui = UI.getInventories().get(UI.getOpen().get(player.getUniqueId()));
			if (ui != null) ui.delete();
			UI.getOpen().remove(player.getUniqueId());
			player.updateInventory();
		}
	}
}
