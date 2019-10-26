package net.islandearth.languagy.listener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.islandearth.languagy.LanguagyPlugin;
import net.islandearth.languagy.api.HookedPlugin;
import net.islandearth.languagy.api.event.PlayerTranslateEvent;

public class TranslateListener implements Listener, Runnable {
	
	private Map<HookedPlugin, TranslateCount> amount = new HashMap<>();
	
	@EventHandler
	public void onTranslate(PlayerTranslateEvent pte) {
		Player player = pte.getPlayer();
		String lang = pte.getHookedPlugin().getFallback().getAbsoluteFile().getParentFile().toString();
		File file = new File(lang + "/" + player.getLocale() + ".yml");
		if (file.exists()) {
			if (!amount.containsKey(pte.getHookedPlugin())) {
				Map<String, Integer> putMap = new HashMap<>();
				putMap.put(player.getLocale(), 1);
				amount.put(pte.getHookedPlugin(), new TranslateCount(putMap));
			} else {
				TranslateCount count = amount.get(pte.getHookedPlugin());
				count.getAmount().replace(player.getLocale(), count.getAmount().get(player.getLocale()) + 1);
			}
		} else {
			String language = pte.getHookedPlugin().getFallback().getName().replace(".yml", "");
			if (!amount.containsKey(pte.getHookedPlugin())) {
				Map<String, Integer> putMap = new HashMap<>();
				putMap.put(language, 1);
				amount.put(pte.getHookedPlugin(), new TranslateCount(putMap));
			} else {
				TranslateCount count = amount.get(pte.getHookedPlugin());
				count.getAmount().replace(language, count.getAmount().get(language) + 1);
			}
		}
	}

	@Override
	public void run() {
		if (LanguagyPlugin.getPlugin().getConfig().getBoolean("Debug")) {
			LanguagyPlugin.getPlugin().getLogger().info("Saving translation info...");
			LanguagyPlugin.getPlugin().getLogger().info("" + amount.size());
		}
		
		amount.forEach((plugin, translateCount) -> {
			translateCount.getAmount().forEach((code, amount) -> {
				String lang = plugin.getFallback().getAbsoluteFile().getParentFile().toString();
				File file = new File(lang + "/" + code + ".yml");
				if (file.exists()) {
					YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
					config.set("translate.count", config.getInt("translate.count") + amount);
					try {
						config.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		});
		amount.clear();
	}
	
	@AllArgsConstructor
	private class TranslateCount {
		
		@Getter
		private Map<String, Integer> amount;
	}
}
