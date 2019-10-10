package net.islandearth.languagy.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.islandearth.languagy.api.HookedPlugin;

public class PluginHookEvent extends Event {
	
	private HookedPlugin plugin;
	
	public PluginHookEvent(HookedPlugin plugin) {
		this.plugin = plugin;
	}
	
	public HookedPlugin getHookedPlugin() {
		return plugin;
	}

	private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
