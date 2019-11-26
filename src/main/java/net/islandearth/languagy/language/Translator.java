package net.islandearth.languagy.language;

import net.islandearth.languagy.LanguagyPlugin;
import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.api.event.PlayerTranslateEvent;
import net.islandearth.languagy.api.event.PluginHookEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Translator {

	private Plugin plugin;

	private File fallback;

	private TranslatorOptions options;

	private boolean debug;
	
	protected HookedPlugin hook;
	
	/**
	 * @deprecated
	 * @see #Translator(Plugin, File)
	 */
	@Deprecated
	public Translator(@NotNull JavaPlugin plugin, @NotNull File fallback) {
		plugin.getLogger().warning("[Languagy] Plugin is using deprecated translator constructor! Please nag the author(s), " + plugin.getDescription().getAuthors() + ", about this!");
		plugin.getLogger().warning("[Languagy] The author should be using the new LanguagyImplementation annotation.");
		setup(plugin, fallback);
	}
	
	public Translator(@NotNull Plugin plugin, File fallback) {
		this.options = new TranslatorOptions(this);
		setup(plugin, fallback);
	}
	
	/**
	 * Sets the display material in the editor UI.
	 * @param material material to display
	 * @return {@link Translator} instance
	 */
	public Translator setDisplay(@NotNull Material material) {
		hook.setDisplay(material);
		return this;
	}
	
	/**
	 * @deprecated use of {@link Plugin} is now preferred.
	 * @param newPlugin
	 * @return {@link Translator} instance
	 * @see #setPlugin(Plugin)
	 */
	@Deprecated
	public Translator setPlugin(JavaPlugin newPlugin) {
		return setPlugin(Bukkit.getPluginManager().getPlugin(newPlugin.getName()));
	}
	
	/**
	 * Sets the plugin to use, this is unsafe and should be used with caution
	 * @param newPlugin new plugin to set
	 * @return {@link Translator} instance
	 */
	public Translator setPlugin(Plugin newPlugin) {
		this.plugin = newPlugin;
		return this;
	}
	
	/**
	 * Sets the fallback file
	 * @param fallback fallback file
	 * @return {@link Translator} instance
	 */
	public Translator setFallback(File fallback) {
		this.fallback = fallback;
		return this;
	}
	
	/**
	 * 
	 * @param target Player
	 * @param path Configuraton path
	 * @return Translated message for player.
	 * Colour codes are automatically translated.
	 * If your plugin does not support their language,
	 * your fallback file will be used.
	 */
	public String getTranslationFor(@NotNull Player target, @NotNull String path) {
		String lang = fallback.getAbsoluteFile().getParentFile().toString();
		File file = new File(lang + "/" + target.getLocale() + ".yml");
		if (file.exists()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if (config.getString(path) == null) {
				if (plugin.getConfig().getBoolean("Debug")) plugin.getLogger().warning("[Languagy] Translation was requested, but path did not exist in target locale! Try regenerating language files?");
				FileConfiguration fallbackConfig = YamlConfiguration.loadConfiguration(fallback);
				String translation = ChatColor.translateAlternateColorCodes('&', fallbackConfig.getString(path));
				Bukkit.getPluginManager().callEvent(new PlayerTranslateEvent(target, path, translation, hook));
				return translation;
			}
			
			String translation = ChatColor.translateAlternateColorCodes('&', config.getString(path));
			Bukkit.getPluginManager().callEvent(new PlayerTranslateEvent(target, path, translation, hook));
			return translation;
		} else {
			FileConfiguration config = YamlConfiguration.loadConfiguration(fallback);
			if (config.getString(path) == null) {
				plugin.getLogger().warning("[Languagy] Translation was requested, but path did not exist anywhere! Try regenerating language files?");
				return "";
			}
			
			String translation = ChatColor.translateAlternateColorCodes('&', config.getString(path));
			Bukkit.getPluginManager().callEvent(new PlayerTranslateEvent(target, path, translation, hook));
			return translation;
		}
	}
	
	/**
	 * 
	 * @param target Player
	 * @param path Configuraton path (Must be a list)
	 * @return Translated list for player.
	 * Colour codes are automatically translated.
	 * If your plugin does not support their language,
	 * your fallback file will be used.
	 */
	public List<String> getTranslationListFor(@NotNull Player target, @NotNull String path) {
		String lang = fallback.getAbsoluteFile().getParentFile().toString();
		File file = new File(lang + "/" + target.getLocale() + ".yml");
		if (file.exists()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if (config.getStringList(path) == null) {
				plugin.getLogger().warning("[Languagy] Translation was requested, but path did not exist! Try regenerating language files?");
				return Arrays.asList();
			}
			
			List<String> vals = new ArrayList<>();
			for (String string : config.getStringList(path)) {
				vals.add(ChatColor.translateAlternateColorCodes('&', string));
			}
			
			Bukkit.getPluginManager().callEvent(new PlayerTranslateEvent(target, path, vals, hook));
			return vals;
		} else {
			FileConfiguration config = YamlConfiguration.loadConfiguration(fallback);
			if (config.getStringList(path) == null) {
				plugin.getLogger().warning("[Languagy] Translation was requested, but path did not exist! Try regenerating language files?");
				return Arrays.asList();
			}
			
			List<String> vals = new ArrayList<>();
			for (String string : config.getStringList(path)) {
				vals.add(ChatColor.translateAlternateColorCodes('&', string));
			} 
			
			Bukkit.getPluginManager().callEvent(new PlayerTranslateEvent(target, path, vals, hook));
			return vals;
		}
	}
	
	/**
	 * 
	 * @param target Player
	 * @return configuration for player's language
	 */
	public FileConfiguration getFileConfiguration(@NotNull Player target) {
		String lang = fallback.getAbsoluteFile().getParentFile().toString();
		File file = new File(lang + "/" + target.getLocale() + ".yml");
		return YamlConfiguration.loadConfiguration(file);
	}
	
	private void setup(Plugin plugin, File fallback) {
		
		if (!fallback.exists()) {
			plugin.getLogger().warning("[Languagy] Could not initiate new translator: Fallback does not exist!");
			return;
		}
		
		if (LanguagyPlugin.getPlugin() != null) {
			HookedPlugin remove = null;
			for (HookedPlugin hook : LanguagyPlugin.getPlugin().getHookedPlugins()) {
				if (hook.getPlugin().getName().equals(plugin.getName())) {
					remove = hook;
					break;
				}
			}
			
			if (remove != null) {
				LanguagyPlugin.getPlugin().getHookedPlugins().remove(remove);
			}
		}
		
		this.plugin = plugin;
		this.fallback = fallback;
		File lang = fallback.getAbsoluteFile().getParentFile();
		this.hook = new HookedPlugin(plugin, Material.DIRT, lang, fallback);
		if (LanguagyPlugin.getPlugin() != null) LanguagyPlugin.getPlugin().getHookedPlugins().add(hook);
		Bukkit.getPluginManager().callEvent(new PluginHookEvent(hook));
		System.out.println(lang.toString());
		for (Language language : Language.values()) {
			File file = new File(lang.toString() + "/" + language.getCode() + ".yml");
			if (!file.exists() || !file.getName().contains(language.getCode())) {
				String reason = !file.exists() ? "Does not exist" : "File name is incorrect";
				if (plugin.getConfig().getBoolean("Debug")) plugin.getLogger().warning("[Languagy] Language file could not be loaded: " + file.getName() + ". Reason: " + reason);
			} else {
				if (plugin.getConfig().getBoolean("Debug")) plugin.getLogger().info("[Languagy] Loaded language '" + language.getCode() + "'.");
			}
		}
	}

	public File getFallback() {
		return fallback;
	}

	public TranslatorOptions getOptions() {
		return options;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Plugin getPlugin() {
		return plugin;
	}
}
