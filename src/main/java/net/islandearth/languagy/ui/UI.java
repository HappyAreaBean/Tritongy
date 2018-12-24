/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 ******************************************************************************/
package net.islandearth.languagy.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

public abstract class UI {
	
	@Getter private Inventory inventory;
	@Getter private UUID uuid;
	@Getter private Map<Integer, ItemClick> actions;
    @Getter private static Map<UUID, UI> inventories = new HashMap<UUID, UI>();
    @Getter private static Map<UUID, UUID> open = new HashMap<UUID, UUID>();
	
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
            	if (open.containsKey(pl.getUniqueId())) open.remove(pl.getUniqueId());
            }
        } inventories.remove(uuid);
    }
	
	public interface ItemClick {
		void click(Player player);
	}
}
