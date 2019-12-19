package net.islandearth.languagy.api.event;

import net.islandearth.languagy.api.HookedPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncPlayerTranslateEvent extends Event {
	
	private Player player;
	private String path;
	private Object translation;
	private HookedPlugin plugin;
	
	public AsyncPlayerTranslateEvent(Player player, String path, Object translation, HookedPlugin plugin) {
		super(true);
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