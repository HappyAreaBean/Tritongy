package net.islandearth.languagy.extension;

import java.util.HashMap;
import java.util.Map;

import net.islandearth.languagy.LanguagyPlugin;

public class ExtensionManager {

	private Map<String, AbstractExtension> extensions = new HashMap<>();
	
	private LanguagyPlugin plugin;
	
	public ExtensionManager(LanguagyPlugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean hasExtension(String name) {
		return extensions.containsKey(name);
	}
	
	public AbstractExtension getExtension(String name) {
		return extensions.get(name);
	}
	
	public void registerExtension(AbstractExtension extension) {
		extensions.put(extension.getName(), extension);
	}
}
