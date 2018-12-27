package net.islandearth.languagy.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import lombok.AllArgsConstructor;
import net.islandearth.languagy.LanguagyPlugin;
import net.islandearth.languagy.update.Updater;

@AllArgsConstructor
public class JoinListener implements Listener {
	
	private LanguagyPlugin plugin;
	
	@EventHandler
	public void onJoin(PlayerJoinEvent pje) {
		Player player = pje.getPlayer();
		if (player.isOp()) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				Object[] updates = new Updater(plugin).getLastUpdate();
				if (updates.length == 2) {
				    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "Languagy" + ChatColor.GOLD + "] New update avaible:");
				    player.sendMessage(ChatColor.GOLD + "New version: " + ChatColor.YELLOW + updates[0]);
				    player.sendMessage(ChatColor.GOLD + "Your version: " + ChatColor.YELLOW + plugin.getDescription().getVersion());
				    player.sendMessage(ChatColor.GOLD + "What's new: " + ChatColor.YELLOW + updates[1]);
				    player.sendMessage(ChatColor.GREEN + "Type /lgy update to update the plugin. Your server will not restart.");
				}
			});
		}
	}
}
