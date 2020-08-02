package net.islandearth.languagy.ui;

import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.api.language.Language;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PluginEditUI extends UI {

	protected static final int MAX_SIZE = 44;
	
	public PluginEditUI(HookedPlugin plugin, @Nullable Map<String, ItemStack> currentOverflow) {
		super(54, plugin.getPlugin().getName() + " : Edit languages");
		int current = 0;
		
		Map<String, ItemStack> overflow = new HashMap<>();
		if (currentOverflow == null || currentOverflow.isEmpty()) {
			for (File file : plugin.getFallbackFolder().listFiles()) {
				if (file.isFile()) {
					ItemStack lang = new ItemStack(Material.WRITABLE_BOOK);
					ItemMeta lm = lang.getItemMeta();
					Language language = Language.getFromCode(file.getName().replace(".yml", ""));
					if (language != null) {
						lm.setDisplayName(ChatColor.WHITE + StringUtils.capitalize(language.toString().toLowerCase()));
						lang.setItemMeta(lm);
						
						if (current > MAX_SIZE) {
							overflow.put(language.getCode(), lang);
							continue;
						}
						
						setItem(current, lang, player -> {
							new ValueEditUI(plugin, file).openInventory(player);
						});
						current++;
					}
				}
			}
		} else {
			for (String key : currentOverflow.keySet()) {
				ItemStack item = currentOverflow.get(key);
				if (current > MAX_SIZE) {
					overflow.put(key, item);
					continue;
				}
				
				setItem(current, item, player -> {
					new ValueEditUI(plugin, new File(plugin.getFallbackFolder() + "/" + key + ".yml")).openInventory(player);
				});
				current++;
			}
		}
		
		ItemStack next = new ItemStack(Material.ARROW);
		ItemMeta nm = next.getItemMeta();
		nm.setDisplayName(ChatColor.GREEN + "Next Page");
		next.setItemMeta(nm);
		setItem(53, next, player -> {
			new PluginEditUI(plugin, overflow).openInventory(player);
		});
	}
}
