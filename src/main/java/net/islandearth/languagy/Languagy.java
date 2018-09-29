package net.islandearth.languagy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.islandearth.languagy.commands.LanguagyCommand;
import net.islandearth.languagy.language.Language;
import net.islandearth.languagy.language.Translator;
import net.islandearth.languagy.metrics.Metrics;

public class Languagy extends JavaPlugin {
	
	private Logger log = Bukkit.getLogger();
	@Getter private Translator translateTester;
	
	@Override
	public void onEnable()
	{
		log.info("Loading...");
		createConfig();
		registerCommands();
		startMetrics();
		runTest();
	}
	
	@Override
	public void onDisable()
	{
		log.info("Disabling...");
	}
	
	private void createConfig()
	{
		getConfig().options().copyDefaults(true);
		
		List<String> languages = new ArrayList<>();
		for(Language language : Language.values())
		{
			languages.add(language.toString());
		}
		
		getConfig().addDefault("Languages.Enabled", languages);
		getConfig().addDefault("Stats", true);
		
		saveConfig();
		
		try {
			File file = new File(getDataFolder() + "/lang/");
			if(!file.exists()) file.mkdir();
			File lang = new File(getDataFolder() + "/lang/en_gb.yml");
			if(!lang.exists()) lang.createNewFile();
			File lang2 = new File(getDataFolder() + "/lang/en_us.yml");
			if(!lang2.exists()) lang2.createNewFile();
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(lang);
			config.options().copyDefaults(true);
			config.addDefault("Example", "english (default)");
			config.save(lang);
			
			FileConfiguration config2 = YamlConfiguration.loadConfiguration(lang2);
			config2.options().copyDefaults(true);
			config2.addDefault("Example", "american");
			config2.save(lang2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		getConfig().addDefault("Languages.Enabled", Arrays.asList(Language.values()));
		getConfig().addDefault("Effects.Quest Available.Colour", "AQUA");
		
		saveConfig();
	}
	
	private void registerCommands()
	{
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
	
	private void startMetrics()
	{
		if(getConfig().getBoolean("Stats"))
		{
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
			Bukkit.getLogger().warning("[Languagy] Please enable metrics to keep me motivated! ;(");
		}
	}
	
	private void runTest()
	{
		Bukkit.getLogger().info("Running translate tester...");
		this.translateTester = new Translator(this, new File(getDataFolder() + "/lang/en_gb.yml"));
	}
}
