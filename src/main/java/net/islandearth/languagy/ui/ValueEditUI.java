package net.islandearth.languagy.ui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.islandearth.languagy.LanguagyPlugin;
import net.islandearth.languagy.api.HookedPlugin;
import net.wesjd.anvilgui.AnvilGUI;

public class ValueEditUI extends UI {
	
	protected int MAX_SIZE = 44;

	public ValueEditUI(HookedPlugin plugin, File file) {
		super(54, plugin.getPlugin().getName());
		int current = 0;
		Map<String, ItemStack> overflow = new HashMap<>();
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (String key : config.getValues(true).keySet()) {
			if (config.isSet(key)
					&& (config.isString(key)
					|| config.isBoolean(key))) {
				KeyType type = config.isString(key) ? KeyType.STRING : KeyType.BOOLEAN;
				ItemStack lang = new ItemStack(Material.PAPER);
				ItemMeta lm = lang.getItemMeta();
				lm.setDisplayName(ChatColor.WHITE + key);
				lang.setItemMeta(lm);
				
				if (current > MAX_SIZE) {
					overflow.put(key, lang);
				} else {
					setItem(current, lang, player -> {
						openAnvil(player, config, key, file, type);
					});
				}
				current++;
			}
		}
		
		ItemStack next = new ItemStack(Material.ARROW);
		ItemMeta nm = next.getItemMeta();
		nm.setDisplayName(ChatColor.GREEN + "Next Page");
		next.setItemMeta(nm);
		setItem(53, next, player -> {
			new ValueEditUI(plugin, overflow, file, player).openInventory(player);
		});
	}

	protected ValueEditUI(HookedPlugin plugin, 
			Map<String, ItemStack> overflow, 
			File file,
			Player player) {
		super(54, "Language Editor");
		int current = 0;
		Map<String, ItemStack> extraOverflow = new HashMap<>();
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (String key : overflow.keySet()) {
			ItemStack item = overflow.get(key);
			KeyType type = config.isString(key) ? KeyType.STRING : KeyType.BOOLEAN;
			if (current > MAX_SIZE) {
				extraOverflow.put(key, item);
			} else {
				setItem(current, item, player2 -> {
					openAnvil(player2, config, key, file, type);
				});
			}
			current++;
		}
		
		/*ItemStack previous = new ItemStack(Material.ARROW);
		ItemMeta pm = previous.getItemMeta();
		pm.setDisplayName(ChatColor.GREEN + "Previous Page");
		previous.setItemMeta(pm);
		setItem(54, previous, player -> {
			origin.openInventory(player);
		});*/
		
		ItemStack next = new ItemStack(Material.ARROW);
		ItemMeta nm = next.getItemMeta();
		nm.setDisplayName(ChatColor.GREEN + "Next Page");
		next.setItemMeta(nm);
		setItem(53, next, player2 -> {
			new ValueEditUI(plugin, extraOverflow, file, player2).openInventory(player2);
		});
	}
	
	private void openAnvil(Player player, FileConfiguration config, String path, File file, KeyType type) {
		new AnvilGUI(LanguagyPlugin.getPlugin(), player, type.toString(), (oPlayer, reply) -> {
			if (!reply.isEmpty()) {
				config.set(path, reply);
				saveConfig(config, file);
				player.closeInventory();
				player.sendMessage(ChatColor.GREEN + "Configuration saved.");
			    return null;
			} else {
				return "Invalid value!";
			}
		});
	}
	
	private void saveConfig(FileConfiguration config, File file) {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private enum KeyType {
		BOOLEAN,
		STRING
	}
}
