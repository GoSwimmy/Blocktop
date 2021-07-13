package net.mysticnetwork.blocktop.events;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.md_5.bungee.api.ChatColor;
import net.mysticnetwork.blocktop.managers.DataManager;
import net.mysticnetwork.blocktop.managers.SQLManager;

public class CommandPreProcess implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void CommandProcess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if(e.getMessage().startsWith("/blockstop") || e.getMessage().startsWith("/blocktop")) {
			e.setCancelled(true);
			if (SQLManager.connect()) {
				if (SQLManager.isConnected()) {
					Connection connection = SQLManager.getConnection();
					try {
						String sql = "SELECT * FROM `blockstop` ORDER BY blocks DESC LIMIT 10";
						PreparedStatement stmt = connection.prepareStatement(sql);
						ResultSet res = stmt.executeQuery();
						int counter = 1;
						while(res.next()) {
							String format = DataManager.config.getString("options.blocktop.top-format");
							format = format.replace("%number%", counter + "");
							format = format.replace("%player%", Bukkit.getOfflinePlayer(UUID.fromString(res.getString("uuid"))).getName());
							format = format.replace("%blocks%", res.getInt("blocks") + "");
							format = ChatColor.translateAlternateColorCodes('&', format);
							p.sendMessage(format);
							counter++;
						}
						return;
					} catch (Exception a) {
						a.printStackTrace();
					}
				}
			}
		}
	}
}
