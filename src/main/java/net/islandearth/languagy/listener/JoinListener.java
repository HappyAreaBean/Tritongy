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
		if (player.isOp() && plugin.getConfig().getBoolean("Check for updates")) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				Updater updater = new Updater(plugin);
				String updates = updater.getLatestVersion();
				if (!updates.equals(plugin.getDescription().getVersion())) {
				    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "Languagy" + ChatColor.GOLD + "] New update available:");
				    player.sendMessage(ChatColor.GOLD + "New version: " + ChatColor.YELLOW + updates);
				    player.sendMessage(ChatColor.GOLD + "Your version: " + ChatColor.YELLOW + plugin.getDescription().getVersion());
				}
			});
		}
	}
}
