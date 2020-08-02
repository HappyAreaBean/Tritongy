package net.islandearth.languagy.api.tasks;

import net.islandearth.languagy.api.HookedPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CacheReloadTask implements Runnable {

	private final HookedPlugin hookedPlugin;

	public CacheReloadTask(HookedPlugin hookedPlugin) {
		this.hookedPlugin = hookedPlugin;
	}

	@Override
	public void run() {
		hookedPlugin.getCachedLanguages().forEach((language, fileConfiguration) -> {
			File lang = hookedPlugin.getFallback().getAbsoluteFile().getParentFile();
			File file = new File(lang.toString() + "/" + language.getCode() + ".yml");
			hookedPlugin.addCachedLanguage(language, YamlConfiguration.loadConfiguration(file));
		});
	}
}
