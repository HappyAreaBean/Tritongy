package net.islandearth.languagy.packets;

import org.bukkit.Bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import net.islandearth.languagy.Languagy;

public class PacketListenerClientInfo extends PacketAdapter {
	
	public PacketListenerClientInfo(Languagy plugin)
	{
		super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.SETTINGS);
	}
	
	@Override
	public void onPacketSending(final PacketEvent pe)
	{
		if(pe.isCancelled()) return;
		PacketContainer packet = pe.getPacket();
		String locale = packet.getStrings().read(0);
		Bukkit.broadcastMessage(locale);
		//Bukkit.getPluginManager().callEvent(new PlayerLocaleChangeEvent());
	}
}
