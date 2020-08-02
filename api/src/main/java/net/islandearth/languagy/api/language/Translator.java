package net.islandearth.languagy.api.language;

import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.api.LanguagyCache;
import net.islandearth.languagy.api.event.AsyncPlayerTranslateEvent;
import net.islandearth.languagy.api.event.PluginHookEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Translator {

	private Plugin plugin;
	private File fallback;
	private final TranslatorOptions options;
	private boolean debug;
	private HookedPlugin hook;
	private final Language language;
	
	public Translator(@NotNull Plugin plugin, File fallback, Language language) {
		this.options = new TranslatorOptions(this);
		this.language = language;
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

	@Nullable
	public HookedPlugin getHook() {
		return hook;
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
			FileConfiguration config = hook.getCachedLanguages().get(Language.getFromCode(target.getLocale()));
			if (config.getString(path) == null) {
				if (hook.isDebug()) plugin.getLogger().warning("[Languagy] Translation was requested, but path did not exist in target locale! Try regenerating language files?");
				FileConfiguration fallbackConfig = hook.getCachedLanguages().get(language);
				String translation = ChatColor.translateAlternateColorCodes('&', fallbackConfig.getString(path));
				Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new AsyncPlayerTranslateEvent(target, path, translation, hook)));
				return translation;
			}
			
			String translation = ChatColor.translateAlternateColorCodes('&', config.getString(path));
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new AsyncPlayerTranslateEvent(target, path, translation, hook)));
			return translation;
		} else {
			FileConfiguration config = hook.getCachedLanguages().get(language);
			if (config.getString(path) == null) {
				plugin.getLogger().warning("[Languagy] Translation was requested, but path did not exist anywhere! Try regenerating language files?");
				return "";
			}
			
			String translation = ChatColor.translateAlternateColorCodes('&', config.getString(path));
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new AsyncPlayerTranslateEvent(target, path, translation, hook)));
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
			FileConfiguration config = hook.getCachedLanguages().get(Language.getFromCode(target.getLocale()));
			
			List<String> vals = new ArrayList<>();
			for (String string : config.getStringList(path)) {
				vals.add(ChatColor.translateAlternateColorCodes('&', string));
			}
			
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new AsyncPlayerTranslateEvent(target, path, vals, hook)));
			return vals;
		} else {
			FileConfiguration config = hook.getCachedLanguages().get(language);
			
			List<String> vals = new ArrayList<>();
			for (String string : config.getStringList(path)) {
				vals.add(ChatColor.translateAlternateColorCodes('&', string));
			}

			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new AsyncPlayerTranslateEvent(target, path, vals, hook)));
			return vals;
		}
	}
	
	/**
	 * 
	 * @param target Player
	 * @return configuration for player's language
	 */
	public FileConfiguration getFileConfiguration(@NotNull Player target) {
		return hook.getCachedLanguages().get(Language.getFromCode(target.getLocale()));
	}
	
	private void setup(Plugin plugin, File fallback) {
		
		if (!fallback.exists()) {
			plugin.getLogger().warning("[Languagy] Could not initiate new translator: Fallback does not exist!");
			return;
		}

		LanguagyCache cache = LanguagyCache.get();
		if (cache == null) cache = new LanguagyCache();
		HookedPlugin remove = null;
		for (HookedPlugin hook : cache.getHookedPlugins()) {
			if (hook.getPlugin().getName().equals(plugin.getName())) {
				remove = hook;
				break;
			}
		}

		if (remove != null) {
			cache.getHookedPlugins().remove(remove);
		}
		
		this.plugin = plugin;
		this.fallback = fallback;
		File lang = fallback.getAbsoluteFile().getParentFile();
		this.hook = new HookedPlugin(plugin, Material.DIRT, lang, fallback);
		cache.getHookedPlugins().add(hook);
		Bukkit.getPluginManager().callEvent(new PluginHookEvent(hook));

		if (hook.isDebug()) plugin.getLogger().info(lang.toString());
		for (Language language : Language.values()) {
			File file = new File(lang.toString() + "/" + language.getCode() + ".yml");
			if (!file.exists() || !file.getName().contains(language.getCode())) {
				String reason = !file.exists() ? "Does not exist" : "File name is incorrect";
				if (hook.isDebug()) plugin.getLogger().warning("[Languagy] Language file could not be loaded: " + file.getName() + ". Reason: " + reason);
			} else {
				hook.addCachedLanguage(language, YamlConfiguration.loadConfiguration(file));
				if (hook.isDebug()) plugin.getLogger().info("[Languagy] Loaded language '" + language.getCode() + "'.");
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
