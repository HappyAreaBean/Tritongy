package net.islandearth.languagy.update;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.UnknownDependencyException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import net.islandearth.languagy.LanguagyPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Updater {
	
	protected LanguagyPlugin plugin;

	private final String VERSION_URL = "https://api.spiget.org/v2/resources/61663/versions?size=15000";
	private final String DESCRIPTION_URL = "https://api.spiget.org/v2/resources/61663/updates?size=15000";
	
	public Updater(LanguagyPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * @author iAmGio
	 */
	public Object[] getLastUpdate() {
		try {
			JSONArray versionsArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(VERSION_URL))));
			String lastVersion = ((JSONObject) versionsArray.get(versionsArray.size() - 1)).get("name").toString();
            if (Integer.parseInt(lastVersion.replaceAll("\\.","")) > Integer.parseInt(plugin.getDescription().getVersion().replaceAll("\\.",""))) {
                JSONArray updatesArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(DESCRIPTION_URL))));
                String updateName = ((JSONObject) updatesArray.get(updatesArray.size() - 1)).get("title").toString();
   
                Object[] update = {lastVersion, updateName};
                return update;
            }
		} catch (ParseException | IOException e) {
			return new String[0];
		}
		return new String[0];
	}
	
	public void update(Player admin) {
		try {
			sendActionBar(admin, "Downloading updater plugin...");
			FileUtils.copyURLToFile(new URL("https://gitlab.com/SamB440/samb440.gitlab.io/raw/master/plugins/UpdaterDummy.jar"), new File("plugins/UpdaterDummy.jar"));
			sendActionBar(admin, "Updater plugin downloaded.");
			try {
				sendActionBar(admin, "Reloading plugin and removing Updater.");
				Bukkit.getPluginManager().loadPlugin(new File("plugins/UpdaterDummy.jar"));
				Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("LanguagyUpdater"));
			} catch (UnknownDependencyException | InvalidPluginException | InvalidDescriptionException e) {
				sendActionBar(admin, "An error occurred whilst downloading the updater. Please check the console.");
				e.printStackTrace();
			}
		} catch (IOException e) {
			sendActionBar(admin, "An error occurred. Please check the console.");
			e.printStackTrace();
		}
	}
	
    private void sendActionBar(Player player, String message) {
    	player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
