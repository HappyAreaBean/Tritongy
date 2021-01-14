package net.islandearth.languagy.api.language;

import net.islandearth.languagy.api.tasks.CacheReloadTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;

public interface LanguagyPluginHook {

	/**
	 * Called when Languagy has successfully hooked into your plugin.
	 * <p>
	 * Within this, you may access your annotated field and set options as desired, such as {@link Translator#setDisplay(org.bukkit.Material)}
	 */
	void onLanguagyHook();

	default void hook(Plugin plugin) {
		if (!plugin.getClass().getSuperclass().equals(this.getClass().getSuperclass())) {
			if (plugin.getName().equals("Languagy")) {
				plugin.getLogger().warning("[Languagy] Hooking plugin (" + this.getClass().getSuperclass().getSuperclass().getName() + ") via Languagy. This will be unsupported in the future.");
			} else {
				plugin.getLogger().severe("[Languagy] Plugin " + plugin.getName() + " tried to create a new Languagy instance for another plugin.");
				return;
			}
		}

		if (!debug()) plugin.getLogger().warning("Running on silent mode. Enable debug to toggle.");
		if (plugin.getDescription().getDepend().contains("Languagy") || plugin.getDescription().getSoftDepend().contains("Languagy") || plugin.getName().equals("Languagy")) {
			for (Field field : plugin.getClass().getDeclaredFields()) {
				if (debug()) plugin.getLogger().info("[Languagy] Found field " + field.getName() + " in " + plugin.getClass().toString() + ".");
				if (field.getAnnotation(LanguagyImplementation.class) != null) {
					if (plugin instanceof LanguagyPluginHook) {
						LanguagyImplementation implementation = field.getAnnotation(LanguagyImplementation.class);
						if (debug()) plugin.getLogger().info("[Languagy] Found annotation " + implementation.toString() + " on field " + field.getName() + ".");
						field.setAccessible(true);
						try {
							Translator translator = new Translator(plugin, new File(plugin.getDataFolder()
									+ File.separator
									+ implementation.defaultFolder()
									+ File.separator
									+ implementation.value().getCode() + ".yml"),
									implementation.value());
							field.set(plugin, translator);
							translator.setDebug(debug());
							Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new CacheReloadTask(translator.getHook()), 20L, 6000);
							LanguagyPluginHook lph = (LanguagyPluginHook) plugin;
							lph.onLanguagyHook();
							break;
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					} else {
						plugin.getLogger().severe("[Languagy] Unable to start because main class does not implement LanguagyPluginHook.");
						Bukkit.getPluginManager().disablePlugin(plugin);
					}
				}
			}
		}
	}

	default boolean debug() {
		return false;
	}
}
