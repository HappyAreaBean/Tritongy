package net.islandearth.languagy.api.language;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class TranslatorOptions {
	
	private final Translator translator;

	TranslatorOptions(Translator translator) {
		this.translator = translator;
	}
	
	/**
	 * Downloads all available files from a directory.
	 * URL should be such as https://www.islandearth.net/languages/, which will then append en_gb.yml etc to the end.
	 * Files must be a valid yml file.
	 * Files will only be copied over if file does not exist, or if the file on the server has changed.
	 * @param url url to download from
	 */
	public void externalDirectory(String url) {
		Bukkit.getScheduler().runTaskAsynchronously(translator.getPlugin(), () -> {
			translator.getPlugin().getLogger().info("[Languagy] ---------------------------------------------");
			translator.getPlugin().getLogger().info("[Languagy] PLUGIN: " + translator.getPlugin().getName());
			translator.getPlugin().getLogger().info("[Languagy] Preparing to download provided language files from " + url + ".");
			int current = 1;
			for (Language language : Language.values()) {
				String lang = translator.getFallback().getAbsoluteFile().getParentFile().toString();
				File file = new File(lang + "/" + language.getCode() + ".yml");
				String newURL = url;
				if (!newURL.endsWith("/")) newURL = url + "/";
				
				try {
					URL download = new URL(url + language.getCode() + ".yml");
					if (file.exists()) {
						FileConfiguration config = YamlConfiguration.loadConfiguration(file);
						if (getSha1(download) != null) {
							if (getSha1(download).equals(config.get("lasthash"))) {
								return;
							}
						}
					}
					
					try {
						FileUtils.copyURLToFile(download, file);
						translator.getPlugin().getLogger().warning("[ASync] [Languagy] Changes detected @ " + language.getCode() + ", replacing file! [" + current + "/" + Language.values().length + "]");
						translator.getPlugin().getLogger().info("[ASync] [Languagy] Downloading " + language.getCode() + " from " + url + ". [" + current + "/" + Language.values().length + "]");
						FileConfiguration config = YamlConfiguration.loadConfiguration(file);
						config.set("lasthash", getSha1(download));
						config.save(file);
						translator.getPlugin().getLogger().info(ChatColor.GREEN + "[ASync] [Languagy] Downloaded " + language.getCode() + " from " + url + ". [" + current + "/" + Language.values().length + "]");
					} catch (IOException e) {
						// Ignore - the address was invalid.
						if (translator.isDebug()) {
							e.printStackTrace();
							translator.getPlugin().getLogger().warning("[ASync] [Languagy] Unable to download " + language.getCode() + " from " + url + ". [" + current + "/" + Language.values().length + "]");
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				current++;	
			}
			
			translator.getPlugin().getLogger().info("[Languagy] Finished downloading from " + url + ".");
			translator.getPlugin().getLogger().info("[Languagy] ---------------------------------------------");
		});
	}
	
	/**
	 * Directly downloads a language file.
	 * URL should be such as https://www.islandearth.net/languages/, which will then append your language to the end.
	 * Files must be a valid yml file.
	 * @param language {@link Language} being provided
	 * @param url url to download from
	 */
	public void externalFile(Language language, String url) throws IllegalArgumentException {
		Bukkit.getScheduler().runTaskAsynchronously(translator.getPlugin(), () -> {
			translator.getPlugin().getLogger().info("[Languagy] ---------------------------------------------");
			translator.getPlugin().getLogger().info("[Languagy] PLUGIN: " + translator.getPlugin().getName());
			translator.getPlugin().getLogger().info("[Languagy] Preparing to download provided language files from " + url + ".");
			String lang = translator.getFallback().getAbsoluteFile().getParentFile().toString();
			File file = new File(lang + "/" + language.getCode() + ".yml");
			String newURL = url;
			if (!newURL.endsWith("/")) newURL = url + "/";
			
			try {
				URL download = new URL(url + language.getCode() + ".yml");
				if (file.exists()) {
					FileConfiguration config = YamlConfiguration.loadConfiguration(file);
					if (getSha1(download) != null) {
						if (getSha1(download).equals(config.getString("lasthash"))) {
							return;
						}
					}
				}
				
				try {
					FileUtils.copyURLToFile(download, file);
					translator.getPlugin().getLogger().warning("[ASync] [Languagy] Changes detected @ " + language.getCode() + ", replacing file! [1/1]");
					translator.getPlugin().getLogger().info("[ASync] [Languagy] Downloading " + language.getCode() + " from " + url + ". [1/1]");
					FileConfiguration config = YamlConfiguration.loadConfiguration(file);
					config.set("lasthash", getSha1(download));
					config.save(file);
					translator.getPlugin().getLogger().info(ChatColor.GREEN + "[ASync] [Languagy] Downloaded " + language.getCode() + " from " + url + ". [1/1]");
				} catch (IOException e) {
					// Ignore - the address was invalid.
					if (translator.isDebug()) {
						e.printStackTrace();
						translator.getPlugin().getLogger().warning("[ASync] [Languagy] Unable to download " + language.getCode() + " from " + url + ". [1/1]");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			translator.getPlugin().getLogger().info("[Languagy] Finished downloading from " + url + ".");
			translator.getPlugin().getLogger().info("[Languagy] ---------------------------------------------");
		});
	}
	
	@SuppressWarnings("unused")
	private String getSha1(File file) throws Exception {
		try {
			byte[] result = DigestUtils.sha1(new FileInputStream(file));
			StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < result.length; i++) {
		    	sb.append(String.format("%02x", result[i]));
		    }
			return sb.toString();
		} catch (IOException e) {
			// Ignore - file doesn't exist
		}
		return null;
	}
	
	private String getSha1(URL url) {
		try {
			byte[] result = DigestUtils.sha1(url.openStream());
			StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < result.length; i++) {
		    	sb.append(String.format("%02x", result[i]));
		    }
			return sb.toString();
		} catch (IOException e) {
			// Ignore - file doesn't exist
		}
		return null;
	}
}
