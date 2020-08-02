package net.islandearth.languagy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.islandearth.languagy.LanguagyPlugin;
import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.api.LanguagyCache;
import net.islandearth.languagy.ui.EditUI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("lgy")
public class LanguagyCommand extends BaseCommand {
	
	private LanguagyPlugin plugin;
	
	public LanguagyCommand(LanguagyPlugin plugin) {
		this.plugin = plugin;
	}

	@Default
	public void onCommand(CommandSender sender) {
		sender.sendMessage(ChatColor.WHITE + "Languagy is a Spigot API for providing player-individual languages for plugins.");
		sender.sendMessage(ChatColor.YELLOW + "https://www.spigotmc.org/resources/languagy-1-8-1-14.61663/");
		sender.sendMessage(ChatColor.YELLOW + "Copyright 2020 IslandEarth. All rights reserved.");
	}
	
	@Subcommand("test")
	@CommandPermission("languagy.admin")
	public void test(Player player) {
		player.sendMessage(plugin.getTranslateTester().getTranslationFor(player, "Example"));
	}
	
	@Subcommand("edit")
	@CommandPermission("languagy.admin")
	public void edit(Player player) {
		new EditUI(plugin, player).openInventory(player);
	}
	
	@Subcommand("view")
	public void view(CommandSender sender, String[] args) {
		if (args.length == 0) {
			String hooks = "";
			for (HookedPlugin hp : LanguagyCache.get().getHookedPlugins()) {
				hooks = hooks + ChatColor.GREEN + hp.getPlugin().getName() + ChatColor.WHITE + ", ";
			}
			sender.sendMessage(ChatColor.WHITE + "Hooks(" + LanguagyCache.get().getHookedPlugins().size() + "): " + hooks);
			return;
		}
		
		String pluginName = args[0];
		for (HookedPlugin hp : LanguagyCache.get().getHookedPlugins()) {
			if (hp.getPlugin().getName().equalsIgnoreCase(pluginName)) {
				sender.sendMessage(ChatColor.GREEN + "Plugin " + pluginName + " is hooked and working.");
				sender.sendMessage("Version: " + hp.getPlugin().getDescription().getVersion());
				sender.sendMessage("Display: " + hp.getDisplay().toString());
				sender.sendMessage("Fallback: " + hp.getFallback().getPath());
				return;
			}
		}
		sender.sendMessage(ChatColor.RED + "No plugin by that name can be found.");
	}
}
