package net.islandearth.languagy.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.islandearth.languagy.api.HookedPlugin;

public class PluginHookEvent extends Event {
	
	private HandlerList handlers = new HandlerList();
	private HookedPlugin plugin;
	
	public PluginHookEvent(HookedPlugin plugin) {
		this.plugin = plugin;
	}
	
	public HookedPlugin getHookedPlugin() {
		return plugin;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
