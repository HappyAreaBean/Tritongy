package net.islandearth.languagy.commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import net.islandearth.languagy.LanguagyPlugin;
import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.ui.EditUI;

public class LanguagyCommand extends BukkitCommand {
	
	private LanguagyPlugin plugin;
	
	public LanguagyCommand(LanguagyPlugin plugin) {
		super("Languagy");
		this.description = "Languagy Information";
		this.usageMessage = "/Languagy";
		this.setAliases(Arrays.asList("lgy"));
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		switch (args.length) {
			case 0:
				sender.sendMessage(ChatColor.WHITE + "Languagy is a Spigot API for providing player-individual languages for plugins.");
				sender.sendMessage(ChatColor.YELLOW + "© 2019 IslandEarth. All rights reserved.");
				break;
			case 1:
				switch (args[0].toLowerCase()) {
					case "test":
						if (sender instanceof Player) {
							Player player = (Player) sender;
							if (player.isOp() || player.getUniqueId().toString().equals("4b319cd4-e827-4dcf-a303-9a3fce310755")) {
								player.sendMessage(plugin.getTranslateTester().getTranslationFor(player, "Example"));
							} else {
								player.sendMessage(ChatColor.RED + "You don't have permission to do this!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "You need to be a player to do this!");
						}
						break;
					case "edit":
						if (sender instanceof Player) {
							Player player = (Player) sender;
							if (plugin.getVersion().currentVersion == plugin.getVersion().latestVersion) {
								if (player.isOp() || player.getUniqueId().toString().equals("4b319cd4-e827-4dcf-a303-9a3fce310755")) {
									new EditUI(plugin, player).openInventory(player);
								} else {
									player.sendMessage(ChatColor.RED + "You don't have permission to do this!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "This feature only supports the latest version!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "You need to be a player to do this!");
						}
						break;
					case "update":
						if (sender instanceof Player) {
							Player player = (Player) sender;
							player.sendMessage(ChatColor.RED + "Sorry, but this feature is no longer supported!");
						} else {
							sender.sendMessage(ChatColor.RED + "You need to be a player to do this!");
						}
						break;
					default:
						sender.sendMessage(ChatColor.RED + "Unknown command!");
						break;
				}
				break;
			case 2:
				switch (args[0].toLowerCase()) {
					case "view": 
						String pluginName = args[1];
						for (HookedPlugin hp : plugin.getHookedPlugins()) {
							if (hp.getPlugin().getName().equalsIgnoreCase(pluginName)) {
								sender.sendMessage(ChatColor.GREEN + "Plugin " + pluginName + " is hooked and working.");
								sender.sendMessage("Version: " + hp.getPlugin().getDescription().getVersion());
								sender.sendMessage("Display: " + hp.getDisplay().toString());
								sender.sendMessage("Fallback: " + hp.getFallback().getPath());
								return true;
							}
						}
						
						sender.sendMessage(ChatColor.RED + "No plugin by that name can be found.");
						break;
				}
				break;
			default:
				sender.sendMessage(ChatColor.WHITE + "Languagy is a Spigot API for providing player-individual languages for plugins.");
				sender.sendMessage(ChatColor.YELLOW + "© 2018 IslandEarth. All rights reserved. Made with" + " ❤ " + "by SamB440.");
				break;
		} return true;
	}
}
