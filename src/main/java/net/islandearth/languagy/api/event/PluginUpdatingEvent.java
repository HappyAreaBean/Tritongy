package net.islandearth.languagy.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when the plugin is about to update.
 */
public class PluginUpdatingEvent extends Event {
	
	private HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
