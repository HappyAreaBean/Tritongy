package net.islandearth.languagy.commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
<<<<<<< HEAD
import org.bukkit.entity.Player;

import net.islandearth.languagy.Languagy;

public class LanguagyCommand extends BukkitCommand {
	
	private Languagy plugin;
	
	public LanguagyCommand(Languagy plugin)
=======

public class LanguagyCommand extends BukkitCommand {
	
	public LanguagyCommand()
>>>>>>> 64fb4f3178edb9eb6131b0c119ac1a65719b80f5
	{
		super("Languagy");
		this.description = "Languagy Information";
		this.usageMessage = "/Languagy";
		this.setAliases(Arrays.asList("lgy"));
<<<<<<< HEAD
		this.plugin = plugin;
=======
>>>>>>> 64fb4f3178edb9eb6131b0c119ac1a65719b80f5
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
<<<<<<< HEAD
		switch(args.length)
		{
			case 0:
				sender.sendMessage(ChatColor.WHITE + "Languagy is a Spigot API for providing player-individual languages for plugins.");
				sender.sendMessage(ChatColor.YELLOW + "© 2018 IslandEarth. All rights reserved. Made with" + " ❤ " + "by SamB440.");
				break;
			case 1:
				switch(args[0].toLowerCase())
				{
					case "test":
						if(sender instanceof Player)
						{
							Player player = (Player) sender;
							if(player.isOp() || player.getUniqueId().toString().equals("4b319cd4-e827-4dcf-a303-9a3fce310755"))
							{
								player.sendMessage(plugin.getTranslateTester().getTranslationFor(player, "Example"));
							} else player.sendMessage(ChatColor.RED + "You don't have permission to do this!");
						} else sender.sendMessage(ChatColor.RED + "You need to be a player to do this!");
						break;
					default:
						sender.sendMessage(ChatColor.RED + "Unknown command!");
						break;
				}
				
				break;
			default:
				sender.sendMessage(ChatColor.WHITE + "Languagy is a Spigot API for providing player-individual languages for plugins.");
				sender.sendMessage(ChatColor.YELLOW + "© 2018 IslandEarth. All rights reserved. Made with" + " ❤ " + "by SamB440.");
				break;
		} return true;
=======
		sender.sendMessage(ChatColor.WHITE + "Languagy is a Spigot API for providing player-individual languages for plugins.");
		sender.sendMessage(ChatColor.YELLOW + "© 2018 IslandEarth. All rights reserved. Made with" + " ❤ " + "by SamB440.");
		return true;
>>>>>>> 64fb4f3178edb9eb6131b0c119ac1a65719b80f5
	}
}