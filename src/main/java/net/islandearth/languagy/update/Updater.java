package net.islandearth.languagy.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import net.islandearth.languagy.LanguagyPlugin;

public class Updater {
	
	protected LanguagyPlugin plugin;
	
	public Updater(LanguagyPlugin plugin) {
		this.plugin = plugin;
	}
	
	public String getLatestVersion() {
		try {
			StringBuilder result = new StringBuilder();
			URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=61663");
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			return result.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			plugin.getLogger().info("Failed to check for an update on Spigot.");
        }
		return null;
	}
}
