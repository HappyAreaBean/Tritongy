package net.islandearth.languagy.api;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class HookedPlugin {

	@Getter
	private Plugin plugin;
	
	@Getter 
	@Setter
	private Material display;
	
	@Getter
	private File fallbackFolder;
	
	@Getter
	private File fallback;
}
