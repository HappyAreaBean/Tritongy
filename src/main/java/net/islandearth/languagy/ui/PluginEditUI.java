package net.islandearth.languagy.ui;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.language.Language;
import net.islandearth.languagy.language.LanguageParser;

public class PluginEditUI extends UI {

	public PluginEditUI(HookedPlugin plugin) {
		super((int) roundUp(plugin.getFallbackFolder().listFiles().length + 1, 9), "Edit languages");
		int current = 0;
		for (File file : plugin.getFallbackFolder().listFiles()) {
			if (file.isFile()) {
				ItemStack lang = new ItemStack(Material.WRITABLE_BOOK);
				ItemMeta lm = lang.getItemMeta();
				Language language = LanguageParser.getFromCode(file.getName().replace(".yml", ""));
				if (language != null) {
					lm.setDisplayName(ChatColor.WHITE + StringUtils.capitalize(language.toString().toLowerCase()));
					lang.setItemMeta(lm);
					setItem(current, lang, player -> {
						new ValueEditUI(plugin, file).openInventory(player);
					});
					current++;
				}
			}
		}
	}
	
	private static long roundUp(long n, long m) {
	    return n >= 0 ? ((n + m - 1) / m) * m : (n / m) * m;
	}
}
