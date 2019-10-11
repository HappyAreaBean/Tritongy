package net.islandearth.languagy.ui;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.islandearth.languagy.LanguagyPlugin;
import net.islandearth.languagy.api.HookedPlugin;

public class EditUI extends UI {
	
	public EditUI(LanguagyPlugin plugin, Player open) {
		super((int) roundUp(plugin.getHookedPlugins().size() - 1, 9), "Language Editor");
		
		int current = 0;
		try {
			for (HookedPlugin hookedPlugin : plugin.getHookedPlugins()) {
				String name = hookedPlugin.getPlugin().getDescription().getName();
				ItemStack hooked = new ItemStack(hookedPlugin.getDisplay());
				ItemMeta hm = hooked.getItemMeta();
				hm.setDisplayName(ChatColor.WHITE + name);
				hm.setLore(Arrays.asList(hookedPlugin.getPlugin().getDescription().getDescription(), 
						ChatColor.GRAY + "" + ChatColor.ITALIC + "Authors: " + hookedPlugin.getPlugin().getDescription().getAuthors().toString(), 
						ChatColor.GRAY + "" + ChatColor.ITALIC + "Version: " + hookedPlugin.getPlugin().getDescription().getVersion()));
				hooked.setItemMeta(hm);
				
				setItem(current, hooked, player -> {
					new PluginEditUI(hookedPlugin, null).openInventory(player);
				});
				current++;
			}
		} catch (IndexOutOfBoundsException e) {
			open.sendMessage(ChatColor.RED + "You do not have a plugin that hooks into Languagy installed. Please install one to use Languagy.");
			return;
		}
	}
	
	private static long roundUp(long n, long m) {
	    return n >= 0 ? ((n + m - 1) / m) * m : (n / m) * m;
	}
}
