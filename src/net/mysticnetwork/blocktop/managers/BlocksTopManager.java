package net.mysticnetwork.blocktop.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class BlocksTopManager {

	public static HashMap<UUID, Long> blocks = new HashMap<>();

	public static void updatePlayer(Player p, int i) {
		if (!blocks.containsKey(p.getUniqueId())) {
			if (SQLManager.connect()) {
				if (SQLManager.isConnected()) {
					Connection connection = SQLManager.getConnection();
					try {
						String sql = "SELECT * FROM `blockstop` WHERE uuid = ?";
						PreparedStatement stmt = connection.prepareStatement(sql);
						stmt.setString(1, p.getUniqueId().toString());
						ResultSet res = stmt.executeQuery();
						if (res.next()) {
							blocks.put(p.getUniqueId(), Long.parseLong(res.getInt("blocks")+""));
						} else {
							blocks.put(p.getUniqueId(), Long.parseLong(0+""));
						}
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		blocks.put(p.getUniqueId(), blocks.get(p.getUniqueId()) + 1);
	}

	public static void refreshDatabase() {
		if (DataManager.config.getBoolean("options.refresh.notif")) {
			String format = DataManager.config.getString("options.refresh.notif-message");
			format = format.replace("%total%", blocks.size() + "");
			format = ChatColor.translateAlternateColorCodes('&', format);
			Bukkit.getConsoleSender().sendMessage(format);
		}
		for (UUID uuid : blocks.keySet()) {
			if (SQLManager.connect()) {
				if (SQLManager.isConnected()) {
					Connection connection = SQLManager.getConnection();
					try {
						String sql = "SELECT * FROM `blockstop` WHERE uuid = ?";
						PreparedStatement stmt = connection.prepareStatement(sql);
						stmt.setString(1, uuid.toString());
						ResultSet res = stmt.executeQuery();
						if (res.next()) {
							try {
								String roundstable = "UPDATE `blockstop` SET blocks = ? WHERE uuid = ?";
								PreparedStatement roundstablestmt = connection.prepareStatement(roundstable);
								roundstablestmt.setLong(1, blocks.get(uuid));
								roundstablestmt.setString(2, uuid.toString());
								roundstablestmt.executeUpdate();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							try {
								String roundstable = "INSERT INTO `blockstop` (uuid, blocks) VALUES (?, ?)";
								PreparedStatement roundstablestmt = connection.prepareStatement(roundstable);
								roundstablestmt.setString(1, uuid.toString());
								roundstablestmt.setLong(2, blocks.get(uuid));
								roundstablestmt.executeUpdate();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void removePlayer(Player p, OfflinePlayer t) {
		if (SQLManager.connect()) {
			if (SQLManager.isConnected()) {
				Connection connection = SQLManager.getConnection();
				try {
					String sql = "SELECT * FROM `blockstop` WHERE uuid = ?";
					PreparedStatement stmt = connection.prepareStatement(sql);
					stmt.setString(1, p.getUniqueId().toString());
					ResultSet res = stmt.executeQuery();
					if (res.next()) {
						try {
							String roundstable = "DELETE FROM `blockstop` WHERE uuid = ?";
							PreparedStatement roundstablestmt = connection.prepareStatement(roundstable);
							roundstablestmt.setString(1, p.getUniqueId().toString());
							roundstablestmt.executeUpdate();
							blocks.remove(p.getUniqueId());
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config
									.getString("options.command.player-removed").replace("%player%", t.getName())));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config
								.getString("options.command.invalid-player").replace("%player%", t.getName())));
					}
					return;
				} catch (Exception e) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config
							.getString("options.command.invalid-player").replace("%player%", t.getName())));
					e.printStackTrace();
				}
			}
		}
	}

	public static int getBlocks(Player p) {
		if (SQLManager.connect()) {
			if (SQLManager.isConnected()) {
				Connection connection = SQLManager.getConnection();
				try {
					String sql = "SELECT * FROM `blockstop` WHERE uuid = ?";
					PreparedStatement stmt = connection.prepareStatement(sql);
					stmt.setString(1, p.getUniqueId().toString());
					ResultSet res = stmt.executeQuery();
					if (res.next()) {
						try {
							return res.getInt("blocks");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		}
		return 0;
	}

	public static void resetDatabase(Player p) {
		if (SQLManager.connect()) {
			if (SQLManager.isConnected()) {
				Connection connection = SQLManager.getConnection();
				try {
					String sql = "DELETE FROM `blockstop`";
					PreparedStatement stmt = connection.prepareStatement(sql);
					stmt.executeUpdate();
					blocks.clear();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							DataManager.config.getString("options.command.database-reset")));
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void reloadDataManager(Player p) {
		DataManager.reloadConfig();
		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				DataManager.config.getString("options.command.config-reloaded")));
	}

	public static void addPlayer(Player p, Player t, long amount) {
		if (SQLManager.connect()) {
			if (SQLManager.isConnected()) {
				Connection connection = SQLManager.getConnection();
				try {
					String sql = "SELECT * FROM `blockstop` WHERE uuid = ?";
					PreparedStatement stmt = connection.prepareStatement(sql);
					stmt.setString(1, p.getUniqueId().toString());
					ResultSet res = stmt.executeQuery();
					if (res.next()) {
						try {
							String roundstable = "UPDATE `blockstop` SET blocks = ? WHERE uuid = ?";
							PreparedStatement roundstablestmt = connection.prepareStatement(roundstable);
							roundstablestmt.setLong(1, amount + res.getInt("blocks"));
							roundstablestmt.setString(2, p.getUniqueId().toString());
							roundstablestmt.executeUpdate();
							blocks.put(p.getUniqueId(), amount + res.getInt("blocks"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									DataManager.config.getString("options.command.amount-added")
											.replace("%player%", t.getName())
											.replace("%amount%", amount + "")));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config
								.getString("options.command.invalid-player").replace("%player%", t.getName())));
					}
					return;
				} catch (Exception e) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.config
							.getString("options.command.invalid-player").replace("%player%", t.getName())));
					e.printStackTrace();
				}
			}
		}
	}
}
