package net.islandearth.languagy.ui;

import java.io.File;
import java.io.IOException;

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

	public ValueEditUI(HookedPlugin plugin, File file) {
		super((int) roundUp(getKeySize(file), 9), plugin.getPlugin().getName());
		int current = 0;
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
				setItem(current, lang, player -> {
					openAnvil(player, config, key, file, type);
				});
				current++;
			}
		}
	}

	private static long roundUp(long n, long m) {
	    return n >= 0 ? ((n + m - 1) / m) * m : (n / m) * m;
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
	
	private static int getKeySize(File file) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config.getValues(true).size();
	}
	
	private enum KeyType {
		BOOLEAN,
		STRING
	}
}
