package net.islandearth.languagy.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

public class ListenerPlayerLocaleEvent implements Listener {
	
	@EventHandler
	public void onLocale(PlayerLocaleChangeEvent plce)
	{
		Bukkit.broadcastMessage(plce.getLocale());
	}

}
