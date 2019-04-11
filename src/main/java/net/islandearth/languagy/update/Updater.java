package net.islandearth.languagy.update;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import net.islandearth.languagy.LanguagyPlugin;

public class Updater {
	
	protected LanguagyPlugin plugin;
	
	private final String SPIGET_VERSION_URL = "https://api.spiget.org/v2/resources/61663/versions?size=15000";
	private final String SPIGET_DESCRIPTION_URL = "https://api.spiget.org/v2/resources/61663/updates?size=15000";
	
	public Updater(LanguagyPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * @author iAmGio
	 */
	public Object[] getLastUpdate() {
		try {
			JSONArray versionsArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(SPIGET_VERSION_URL)), Charset.defaultCharset()));
			String lastVersion = ((JSONObject) versionsArray.get(versionsArray.size() - 1)).get("name").toString();
            if (Integer.parseInt(lastVersion.replaceAll("\\.","")) > Integer.parseInt(plugin.getDescription().getVersion().replaceAll("\\.",""))) {
                JSONArray updatesArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(SPIGET_DESCRIPTION_URL)), Charset.defaultCharset()));
                String updateName = ((JSONObject) updatesArray.get(updatesArray.size() - 1)).get("title").toString();
   
                Object[] update = {lastVersion, updateName};
                return update;
            }
		} catch (ParseException | IOException e) {
			return new String[0];
		}
		return new String[0];
	}
}
