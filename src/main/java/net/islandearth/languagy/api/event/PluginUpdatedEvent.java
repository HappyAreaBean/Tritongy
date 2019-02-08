package net.islandearth.languagy.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called after the plugin has updated.
 */
public class PluginUpdatedEvent extends Event {
	
	private HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
