package net.mysticnetwork.blocktop.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.mysticnetwork.blocktop.managers.BlocksTopManager;
import net.mysticnetwork.blocktop.managers.DataManager;

public class BlocksTopCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player p = (Player) sender;
			if(!p.hasPermission(DataManager.config.getString("options.command.permission"))) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config.getString("options.command.no-permission")));
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					BlocksTopManager.reloadDataManager(p);
					return false;
				}
				if(args[0].equalsIgnoreCase("reset")) {
					BlocksTopManager.resetDatabase(p);
					return false;
				}
				if(args[0].equalsIgnoreCase("refresh")) {
					BlocksTopManager.refreshDatabase();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config.getString("options.command.database-refresh")));
					return false;
				}
			}
			if(args.length == 3) {
				if(args[0].equalsIgnoreCase("admin")) {
					if(args[1].equalsIgnoreCase("remove")) {
						OfflinePlayer t = Bukkit.getOfflinePlayer(args[2]);
						BlocksTopManager.removePlayer(p, t);
						return false;
					}
				}
			}
			if(args.length == 4) {
				if(args[0].equalsIgnoreCase("admin")) {
					if(args[1].equalsIgnoreCase("add")) {
						Player t = Bukkit.getPlayer(args[2]);
						if(t == null) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config.getString("options.command.invalid-player").replace("%player%", args[2])));
						} else {
							try {
								long amount = Long.parseLong(args[3]);
								BlocksTopManager.addPlayer(p, t, amount);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						}
						return false;
					}
				}
			}
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config.getString("options.command.help-message-1")));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config.getString("options.command.help-message-2")));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config.getString("options.command.help-message-3")));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config.getString("options.command.help-message-4")));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config.getString("options.command.help-message-5")));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config.getString("options.command.help-message-6")));
		}
		return false;
	}
}
