package net.islandearth.languagy.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.islandearth.languagy.api.HookedPlugin;

public class PlayerTranslateEvent extends Event {
	
	private Player player;
	private String path;
	private Object translation;
	private HookedPlugin plugin;
	
	public PlayerTranslateEvent(Player player, String path, Object translation, HookedPlugin plugin) {
		this.player = player;
		this.path = path;
		this.translation = translation;
		this.plugin = plugin;
	}
	
	private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
	
	public Player getPlayer() {
		return player;
	}
	
	public String getPath() {
		return path;
	}
	
	/**
	 * 
	 * @return translated message, can either be a list or string
	 */
	public Object getTranslation() {
		return translation;
	}
	
	public HookedPlugin getHookedPlugin() {
		return plugin;
	}
}
