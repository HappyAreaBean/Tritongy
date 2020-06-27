package net.islandearth.languagy.api;

import net.islandearth.languagy.extension.ExtensionManager;

import java.util.List;

public interface Languagy {

	List<HookedPlugin> getHookedPlugins();
	
	ExtensionManager getExtensionManager();
	
}
