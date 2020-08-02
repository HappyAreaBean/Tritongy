package net.islandearth.languagy;

import co.aikar.commands.PaperCommandManager;
import io.papermc.lib.PaperLib;
import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.api.LanguagyCache;
import net.islandearth.languagy.api.language.Language;
import net.islandearth.languagy.api.language.LanguagyImplementation;
import net.islandearth.languagy.api.language.LanguagyPluginHook;
import net.islandearth.languagy.api.language.Translator;
import net.islandearth.languagy.commands.LanguagyCommand;
import net.islandearth.languagy.extension.ExtensionManager;
import net.islandearth.languagy.listener.InventoryListener;
import net.islandearth.languagy.listener.TranslateListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LanguagyPlugin extends JavaPlugin implements Languagy, Listener, LanguagyPluginHook {
	
	@LanguagyImplementation(Language.ENGLISH)
	private Translator translateTester;

	private LanguagyCache languagyCache;

	private static LanguagyPlugin plugin;
	
	private ExtensionManager extensionManager;
	
	@Override
	public void onEnable() {

		this.getLogger().info("READ ME:");
		this.getLogger().info("Languagy functions as a plugin, however you do not need it unless a plugin is not shading Languagy.");
		this.getLogger().info("Only use Languagy as a plugin if you need it for a plugin or utilise the extra features.");
		try {
			if (Bukkit.getPluginManager().getPlugin("Plan") != null) this.extensionManager = new ExtensionManager(this);
		} catch (Exception ignored) { }
		
		LanguagyPlugin.plugin = this;
		this.languagyCache = new LanguagyCache();
		LanguagyAPI.set(this);
		createConfig();
		registerCommands();
		this.hook(this);
		registerListeners();
		startMetrics();
		PaperLib.suggestPaper(this);
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("Disabling...");
		LanguagyPlugin.plugin = null;
		this.translateTester = null;
	}
	
	private void createConfig() {
		getConfig().options().copyDefaults(true);
		
		List<String> languages = new ArrayList<>();
		for (Language language : Language.values()) {
			languages.add(language.getCode());
		}
		
		getConfig().addDefault("Languages.Enabled", languages);
		getConfig().addDefault("Stats", true);
		getConfig().addDefault("Debug", false);
		getConfig().addDefault("Check for updates", true);
		
		saveConfig();
		
		try {
			File file = new File(getDataFolder() + "/lang/");
			file.mkdir();
			File lang = new File(getDataFolder() + "/lang/en_gb.yml");
			lang.createNewFile();
			File lang2 = new File(getDataFolder() + "/lang/nl_nl.yml");
			lang2.createNewFile();
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(lang);
			config.options().copyDefaults(true);
			config.addDefault("Example", "english (default)");
			config.addDefault("Test.test", "english (default)");
			config.save(lang);
			
			FileConfiguration config2 = YamlConfiguration.loadConfiguration(lang2);
			config2.options().copyDefaults(true);
			config2.addDefault("Example", "dutch");
			config2.save(lang2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		saveConfig();
	}
	
	private void registerCommands() {
		PaperCommandManager manager = new PaperCommandManager(this);
		manager.registerCommand(new LanguagyCommand(this));
	}
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new InventoryListener(), this);
		if (Bukkit.getPluginManager().getPlugin("Plan") != null) {
			TranslateListener tl = new TranslateListener();
			pm.registerEvents(tl, this);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, tl, 20L, 1000L);
		}
	}
	
	private void startMetrics() {
		if (getConfig().getBoolean("Stats")) {
			this.getLogger().info("[Languagy] Starting metrics! Thanks :)");
			@SuppressWarnings("unused")
			Metrics metrics = new Metrics(this, 3353);
		} else {
			this.getLogger().warning("[Languagy] Metrics is disabled! :(");
			this.getLogger().warning("[Languagy] Please enable metrics to keep me motivated!");
		}
	}

	@Override
	public LanguagyCache getLanguagyCache() {
		return languagyCache;
	}

	@Override
	public ExtensionManager getExtensionManager() {
		return extensionManager;
	}

	public Translator getTranslateTester() {
		return translateTester;
	}

	public static LanguagyPlugin getPlugin() {
		return plugin;
	}

	@EventHandler
	public void enable(PluginEnableEvent ple) {
		Plugin plugin = ple.getPlugin();
		if (!plugin.getDescription().getDepend().contains("Languagy")) return;
		if (LanguagyCache.get() != null) {
			for (HookedPlugin hookedPlugin : LanguagyCache.get().getHookedPlugins()) {
				if (hookedPlugin.getPlugin().equals(plugin)) {
					return;
				}
			}
		}

		if (plugin instanceof LanguagyPluginHook) {
			LanguagyPluginHook languagyPluginHook = (LanguagyPluginHook) plugin;
			languagyPluginHook.hook(plugin);
			String authors = String.join(",", plugin.getDescription().getAuthors());
			plugin.getLogger().warning("[Languagy] Used deprecated enable feature. Please tell the author(s) (" + authors + ") to shade Languagy into their plugin.");
		} else {
			plugin.getLogger().severe("[Languagy] Unable to start because main class does not implement LanguagyPluginHook.");
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
	}

	@Override
	public void onLanguagyHook() {
		if (this.getConfig().getBoolean("Debug")) {
			this.translateTester.setDebug(this.getConfig().getBoolean("Debug"));
			this.getLogger().info("--- TESTING DOWNLOADER ---");
			this.translateTester.getOptions().externalDirectory("https://www.islandearth.net/plugins/languagy/lang/");
		}
	}
}
