/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 ******************************************************************************/
package net.islandearth.languagy.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class UI {

	private Inventory inventory;
	private UUID uuid;
	private Map<Integer, ItemClick> actions;
	private static Map<UUID, UI> inventories = new HashMap<UUID, UI>();
	private static Map<UUID, UUID> open = new HashMap<UUID, UUID>();

	public Inventory getInventory() {
		return inventory;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Map<Integer, ItemClick> getActions() {
		return actions;
	}

	public static Map<UUID, UI> getInventories() {
		return inventories;
	}

	public static Map<UUID, UUID> getOpen() {
		return open;
	}
	
	public UI(int size, String name) {
		this.inventory = Bukkit.createInventory(null, size, name);
		this.uuid = UUID.randomUUID();
		this.actions = new HashMap<Integer, ItemClick>();
		inventories.put(uuid, this);
	}
	
	public void setItem(int slot, ItemStack item, ItemClick action){
		inventory.setItem(slot, item);
		actions.put(slot, action);
	}
	
	public void setItem(int slot, ItemStack item) {
		inventory.setItem(slot, item);
	}
	
	public void openInventory(Player player) {
		player.openInventory(inventory);
		open.put(player.getUniqueId(), uuid);
	}
	
	public void delete() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            UUID uuid = open.get(pl.getUniqueId());
            if (uuid != null && uuid.equals(this.uuid)) {
				open.remove(pl.getUniqueId());
            }
        } inventories.remove(uuid);
    }
	
	public interface ItemClick {
		void click(Player player);
	}
}
