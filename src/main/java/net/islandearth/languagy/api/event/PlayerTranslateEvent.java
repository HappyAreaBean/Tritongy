package net.islandearth.languagy.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.islandearth.languagy.api.HookedPlugin;

public class PlayerTranslateEvent extends Event {
	
	private HandlerList handlers = new HandlerList();
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
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
