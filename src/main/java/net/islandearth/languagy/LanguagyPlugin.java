package net.islandearth.languagy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.api.Languagy;
import net.islandearth.languagy.api.LanguagyAPI;
import net.islandearth.languagy.api.event.PluginUpdatedEvent;
import net.islandearth.languagy.commands.LanguagyCommand;
import net.islandearth.languagy.language.Language;
import net.islandearth.languagy.language.Translator;
import net.islandearth.languagy.listener.InventoryListener;
import net.islandearth.languagy.listener.JoinListener;
import net.islandearth.languagy.metrics.Metrics;
import net.islandearth.languagy.version.VersionChecker;
import net.islandearth.languagy.version.VersionChecker.Version;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LanguagyPlugin extends JavaPlugin implements Languagy {
	
	private Logger log = Bukkit.getLogger();
	
	@Getter 
	private Translator translateTester;
	
	private List<HookedPlugin> hookedPlugins;
	
	@Getter
	private static LanguagyPlugin plugin;
	
	@Getter
	private VersionChecker version;
	
	@Override
	public void onEnable() {
		log.info("Loading...");
		removeUpdater();
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
		
		LanguagyPlugin.plugin = this;
		if (hookedPlugins == null) this.hookedPlugins = new ArrayList<>();
		LanguagyAPI.set(this);
		createConfig();
		registerCommands();
		registerListeners();
		startMetrics();
		runTest();
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
		try {
			Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			commandMap.register("Languagy", new LanguagyCommand(this));
		} catch (NoSuchFieldException | 
				SecurityException | 
				IllegalArgumentException | 
				IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new JoinListener(this), this);
	}
	
	private void startMetrics() {
		if (getConfig().getBoolean("Stats")) {
			Bukkit.getLogger().info("[Languagy] Starting metrics! Thanks :)");
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
			Bukkit.getLogger().warning("[Languagy] Metrics is disabled! :(");
			Bukkit.getLogger().warning("[Languagy] Please enable metrics to keep me motivated!");
		}
	}
	
	private void runTest() {
		Bukkit.getLogger().info("Running translate tester...");
		this.translateTester = new Translator(this, new File(getDataFolder() + "/lang/en_gb.yml"));
	}

	@Override
	public List<HookedPlugin> getHookedPlugins() {
		return hookedPlugins;
	}
	
	private void removeUpdater() {
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
			File updater = new File("plugins/UpdaterDummy.jar");
			if (updater.exists()) {
				log.warning("Removing updater plugin!");
				sendActionBar(ChatColor.YELLOW + "Removing updater plugin!");
				sendMessage(ChatColor.GREEN + "It seems you updated Languagy recently, but a full restart is recommended to prevent any problems.");
				try {
					Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("Updater"));
					updater.delete();
					Bukkit.getPluginManager().callEvent(new PluginUpdatedEvent());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 40L);
	}
	
	private void sendActionBar(String message) {
		for (Player admin : Bukkit.getOnlinePlayers()) {
			if (admin.isOp()) {
				admin.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
			}
		}
	}
	
	private void sendMessage(String message) {
		for (Player admin : Bukkit.getOnlinePlayers()) {
			if (admin.isOp()) {
				admin.playSound(admin.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
				admin.sendMessage(message);
			}
		}
	}
}
