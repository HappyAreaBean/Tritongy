package net.islandearth.languagy.api;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class HookedPlugin {

	@Getter
	private JavaPlugin plugin;
	
	@Getter @Setter
	private Material display;
	
	@Getter
	private File fallbackFolder;
}
