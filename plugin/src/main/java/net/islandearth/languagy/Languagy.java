package net.islandearth.languagy;

import net.islandearth.languagy.api.LanguagyCache;
import net.islandearth.languagy.extension.ExtensionManager;

public interface Languagy {

	LanguagyCache getLanguagyCache();
	
	ExtensionManager getExtensionManager();
	
}
