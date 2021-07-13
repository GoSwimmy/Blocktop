package net.mysticnetwork.blocktop.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.mysticnetwork.blocktop.managers.SQLManager;

public class PlayerJoin implements Listener {

	@EventHandler
	public void join(PlayerJoinEvent e) {
		SQLManager.initializePlayer(e.getPlayer());
	}
	
}
