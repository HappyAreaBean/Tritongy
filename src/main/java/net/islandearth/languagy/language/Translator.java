package net.islandearth.languagy.language;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.NonNull;

public class Translator {
	
	@Getter @NonNull private JavaPlugin plugin;
	@Getter @NonNull private File fallback;
	
	public Translator(@NonNull JavaPlugin plugin, @NonNull File fallback)
	{
		if(!fallback.exists())
		{
			Bukkit.getLogger().warning("[Languagy] [" + plugin.getName() + "] Could not initiate new translator: Fallback does not exist!");
			return;
		}
		
		this.plugin = plugin;
		this.fallback = fallback;
		File lang = fallback.getAbsoluteFile().getParentFile();
		System.out.println(lang.toString());
		for(Language language : Language.values())
		{
			File file = new File(lang.toString() + "/" + language.getCode() + ".yml");
			if(!file.exists() || !file.getName().contains(language.getCode()))
			{
				String reason = !file.exists() ? "Does not exist" : "File name is incorrect";
				Bukkit.getLogger().warning("[Languagy] [" + plugin.getName() + "] Language file could not be loaded: " + file.getName() + ". Reason: " + reason);
			} else Bukkit.getLogger().info("[Languagy] [" + plugin.getName() + "] Loaded language '" + language.getCode() + "'.");
		}
	}
	
	public Translator setPlugin(JavaPlugin newPlugin)
	{
		this.plugin = newPlugin;
		return this;
	}
	
	/**
	 * 
	 * @param target - Player
	 * @param path - Configuraton path
	 * @return Translated message for player.
	 * Colour codes are automatically translated.
	 * If your plugin does not support their language,
	 * your fallback file will be used.
	 */
	public String getTranslationFor(Player target, String path)
	{
		String lang = fallback.getAbsoluteFile().getParentFile().toString();
		File file = new File(lang + "/" + target.getLocale() + ".yml");
		if(file.exists())
		{
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			return ChatColor.translateAlternateColorCodes('&', config.getString(path));
		} else {
			FileConfiguration config = YamlConfiguration.loadConfiguration(fallback);
			return ChatColor.translateAlternateColorCodes('&', config.getString(path));
		}
	}
}
