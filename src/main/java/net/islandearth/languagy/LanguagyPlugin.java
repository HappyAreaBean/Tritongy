package net.islandearth.languagy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import io.papermc.lib.PaperLib;
import lombok.Getter;
import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.api.Languagy;
import net.islandearth.languagy.api.LanguagyAPI;
import net.islandearth.languagy.commands.LanguagyCommand;
import net.islandearth.languagy.extension.ExtensionManager;
import net.islandearth.languagy.language.Language;
import net.islandearth.languagy.language.LanguagyImplementation;
import net.islandearth.languagy.language.LanguagyPluginHook;
import net.islandearth.languagy.language.Translator;
import net.islandearth.languagy.listener.InventoryListener;
import net.islandearth.languagy.listener.JoinListener;
import net.islandearth.languagy.listener.TranslateListener;
import net.islandearth.languagy.version.VersionChecker;
import net.islandearth.languagy.version.VersionChecker.Version;

public class LanguagyPlugin extends JavaPlugin implements Languagy, Listener, LanguagyPluginHook {
	
	private Logger log = Bukkit.getLogger();
	
	@LanguagyImplementation(fallbackFile = "plugins/Languagy/lang/en_gb.yml")
	@Getter
	private Translator translateTester;
	
	private List<HookedPlugin> hookedPlugins;
	
	@Getter
	private static LanguagyPlugin plugin;
	
	@Getter
	private VersionChecker version;
	
	@Getter
	private ExtensionManager extensionManager;
	
	@Override
	public void onEnable() {
		log.info("Loading...");
		this.version = new VersionChecker();
		List<String> supported = new ArrayList<>();
		for (Version version : Version.values()) {
			if (version != Version.UNSUPPORTED) {
				supported.add(version.getId());
			}
		}
		
		if (!version.checkVersion()) {
			log.info(" ");
			log.info(ChatColor.RED + "You are using an unsupported version!");
			log.info(ChatColor.RED + "Your current version is: " + version.getCurrentVersion().getId());
			log.info(ChatColor.RED + "The latest version is: " + version.getLatestVersion().getId());
			log.info(ChatColor.GREEN + "This plugin supports: " + StringUtils.join(supported, ','));
			log.info(" ");
		} else {
			log.info(" ");
			log.info(ChatColor.GREEN + "You are running version " + version.getCurrentVersion().getId() + ".");
			log.info(" ");
		}
		
		
		try {
			if (Bukkit.getPluginManager().getPlugin("Plan") != null) this.extensionManager = new ExtensionManager(this);
		} catch (Exception e) {
			
		}
		
		LanguagyPlugin.plugin = this;
		if (hookedPlugins == null) this.hookedPlugins = new ArrayList<>();
		LanguagyAPI.set(this);
		createConfig();
		if (!getConfig().getBoolean("Debug")) this.getLogger().warning("Running on silent mode. Enable debug to toggle.");
		registerCommands();
		registerListeners();
		startMetrics();
		PaperLib.suggestPaper(this);
	}
	
	@Override
	public void onDisable() {
		log.info("Disabling...");
		LanguagyPlugin.plugin = null;
		this.version = null;
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
		
		saveConfig();
		
		try {
			File file = new File(getDataFolder() + "/lang/");
			if (!file.exists()) file.mkdir();
			File lang = new File(getDataFolder() + "/lang/en_gb.yml");
			if (!lang.exists()) lang.createNewFile();
			File lang2 = new File(getDataFolder() + "/lang/nl_nl.yml");
			if (!lang2.exists()) lang2.createNewFile();
			
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
		pm.registerEvents(new JoinListener(this), this);
		TranslateListener tl = new TranslateListener();
		pm.registerEvents(tl, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, tl, 20L, 1000L);
	}
	
	private void startMetrics() {
		if (getConfig().getBoolean("Stats")) {
			this.getLogger().info("[Languagy] Starting metrics! Thanks :)");
			@SuppressWarnings("unused")
			Metrics metrics = new Metrics(this);
			/*Map<String, int[]> languages = new HashMap<>();
			for(Language language : Language.values())
			{
				if(getConfig().getStringList("Languages.Enabled").contains(language.toString())) 
				{
					int[] vals = {0, 1};
					languages.put(language.getCode(), vals);
				} else {
					int[] vals = {1, 0};
					languages.put(language.getCode(), vals);
				}
			}

			metrics.addCustomChart(new Metrics.AdvancedBarChart("enabled_languages", () -> languages));*/
		} else {
			this.getLogger().warning("[Languagy] Metrics is disabled! :(");
			this.getLogger().warning("[Languagy] Please enable metrics to keep me motivated!");
		}
	}

	@Override
	public List<HookedPlugin> getHookedPlugins() {
		return hookedPlugins;
	}

	@EventHandler
	public void enable(PluginEnableEvent ple) {
		Plugin plugin = ple.getPlugin();
		if (plugin.getDescription().getDepend().contains("Languagy") || plugin.getDescription().getSoftDepend().contains("Languagy") || plugin.getName().equals("Languagy")) {
			for (Field field : plugin.getClass().getDeclaredFields()) {
				if (getConfig().getBoolean("Debug")) plugin.getLogger().info("[Languagy] Found field " + field.getName() + " in " + plugin.getClass().toString() + ".");
				if (field.getAnnotation(LanguagyImplementation.class) != null) {
					if (LanguagyPluginHook.class.isInstance(plugin)) {
						LanguagyImplementation implementation = field.getAnnotation(LanguagyImplementation.class);
						if (getConfig().getBoolean("Debug")) plugin.getLogger().info("[Languagy] Found annotation " + implementation.toString() + " on field " + field.getName() + ".");
						field.setAccessible(true);
						try {
							field.set(plugin, new Translator(plugin, new File(implementation.fallbackFile())));
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

	@Override
	public void onLanguagyHook() {
		this.translateTester.setDebug(this.getConfig().getBoolean("Debug"));
		this.getLogger().info("--- TESTING DOWNLOADER ---");
		this.translateTester.getOptions().externalDirectory("https://www.islandearth.net/plugins/languagy/lang/");
	}
}
