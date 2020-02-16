package net.islandearth.languagy.tasks;

import net.islandearth.languagy.LanguagyPlugin;
import net.islandearth.languagy.api.HookedPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CacheReloadTask implements Runnable {

	private final LanguagyPlugin plugin;

	public CacheReloadTask(LanguagyPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (HookedPlugin hook : plugin.getHookedPlugins()) {
			hook.getCachedLanguages().forEach((language, fileConfiguration) -> {
				File lang = hook.getFallback().getAbsoluteFile().getParentFile();
				File file = new File(lang.toString() + "/" + language.getCode() + ".yml");
				hook.addCachedLanguage(language, YamlConfiguration.loadConfiguration(file));
			});
		}
	}
}
