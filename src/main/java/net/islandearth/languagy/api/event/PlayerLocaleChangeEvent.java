package net.islandearth.languagy.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import lombok.Getter;
import net.islandearth.languagy.language.Language;

public class PlayerLocaleChangeEvent extends PlayerEvent {
	
	private static final HandlerList handlers = new HandlerList();
	@Getter private Language language;

	public PlayerLocaleChangeEvent(Player arg0, Language language) {
		super(arg0);
		this.language = language;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
