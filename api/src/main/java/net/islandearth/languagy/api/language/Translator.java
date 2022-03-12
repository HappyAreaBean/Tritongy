package net.islandearth.languagy.api.language;

import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.api.event.AsyncPlayerTranslateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

	private final Plugin plugin;
	private final TranslatorOptions options;

	private final Language defaultLanguage;

	private File fallback;
	private boolean debug;
	private HookedPlugin hook;
	
	public Translator(@NotNull Plugin plugin, File fallback, Language defaultLanguage) {
		this.plugin = plugin;
		this.options = new TranslatorOptions(this);
		this.defaultLanguage = defaultLanguage;
		setup(plugin, fallback);
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
	 * Colour codes are automatically translated.
	 * If your plugin does not support their language,
	 * your fallback file will be used.
	 * @param target the target player
	 * @param path configuration path
	 * @return translated string, colour coded
	 */
	@NotNull
	public String getTranslationFor(@NotNull Player target, @NotNull String path) {
		String lang = fallback.getAbsoluteFile().getParentFile().toString();
		File file = new File(lang + File.separator + target.spigot().getLocale() + ".yml");
		final Language targetLanguage = Language.getFromCode(target.spigot().getLocale());
		FileConfiguration config = file.exists()
				? hook.getCachedLanguages().get(targetLanguage)
				: hook.getCachedLanguages().get(defaultLanguage);
		if (config.getString(path) == null) {
			if (hook.isDebug()) {
				String text = "[Languagy] Translation was requested, but path did not exist in '%s'! Try regenerating language files?";
				plugin.getLogger().warning(String.format(text, file.exists() ? targetLanguage : defaultLanguage));
			}
			return Translations.NOT_FOUND;
		}

		String translation = translate(config.getString(path));
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new AsyncPlayerTranslateEvent(target, path, translation, hook)));
		return translation;
	}
	
	/**
	 * Gets a translated list for the target player.
	 * Colour codes are automatically translated.
	 * If your plugin does not support their language,
	 * your fallback file will be used.
	 * @param target the target player
	 * @param path configuration path (Must be a list)
	 * @return translated list of strings, colour coded
	 */
	@NotNull
	public List<String> getTranslationListFor(@NotNull Player target, @NotNull String path) {
		String lang = fallback.getAbsoluteFile().getParentFile().toString();
		File file = new File(lang + File.separator + target.spigot().getLocale() + ".yml");
		FileConfiguration config = file.exists()
				? hook.getCachedLanguages().get(Language.getFromCode(target.spigot().getLocale()))
				: hook.getCachedLanguages().get(defaultLanguage);
		List<String> vals = new ArrayList<>();
		for (String string : config.getStringList(path)) {
			vals.add(translate(string));
		}

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new AsyncPlayerTranslateEvent(target, path, vals, hook)));
		return vals;
	}
	
	/**
	 * Gets the language config for the specified player.
	 * @param target the target player
	 * @return configuration file for the player's language
	 */
	@Nullable
	public FileConfiguration getFileConfiguration(@NotNull Player target) {
		return hook.getCachedLanguages().get(Language.getFromCode(target.spigot().getLocale()));
	}
	
	private void setup(Plugin plugin, File fallback) {
		if (!fallback.exists()) {
			plugin.getLogger().warning("[Languagy] Could not initiate new translator: Fallback does not exist!");
			return;
		}

		this.fallback = fallback;
		File lang = fallback.getAbsoluteFile().getParentFile();
		this.hook = new HookedPlugin(plugin, lang, fallback);

		if (hook.isDebug()) plugin.getLogger().info(lang.toString());
		for (Language language : Language.values()) {
			File file = new File(lang.toString() + File.separator + language.getCode() + ".yml");
			if (!file.exists() || !file.getName().contains(language.getCode())) {
				String reason = !file.exists() ? "Does not exist" : "File name is incorrect";
				if (hook.isDebug()) plugin.getLogger().warning("[Languagy] Language file could not be loaded: " + file.getName() + ". Reason: " + reason);
				continue;
			}
			hook.addCachedLanguage(language, YamlConfiguration.loadConfiguration(file));
			if (hook.isDebug()) plugin.getLogger().info("[Languagy] Loaded language '" + language.getCode() + "'.");
		}
	}

	@NotNull
	public File getFallback() {
		return fallback;
	}

	@NotNull
	@Deprecated
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

	private String translate(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
