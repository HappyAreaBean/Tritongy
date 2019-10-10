package net.islandearth.languagy.api;

import java.util.List;

import net.islandearth.languagy.extension.ExtensionManager;
import net.islandearth.languagy.version.VersionChecker;

public interface Languagy {

	List<HookedPlugin> getHookedPlugins();
	
	VersionChecker getVersion();
	
	ExtensionManager getExtensionManager();
	
}
