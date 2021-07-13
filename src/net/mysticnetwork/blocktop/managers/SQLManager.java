package net.mysticnetwork.blocktop.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SQLManager {

	public static Connection con;

	// connect
	public static boolean connect() {
		FileConfiguration c = DataManager.config;
		String host = c.getString("mysql.host");
		String port = c.getString("mysql.port");
		String database = c.getString("mysql.database");
		String username = c.getString("mysql.username");
		String password = c.getString("mysql.password");
		String ssl = c.getString("mysql.ssl");
		String autoreconnect = c.getString("mysql.autoreconnect");
		if (!isConnected()) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database
						+ "?autoReconnect=" + autoreconnect + "&useSSL=" + ssl, username, password);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	// disconnect
	public static void disconnect() {
		if (isConnected()) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// isConnected
	public static boolean isConnected() {
		return (con == null ? false : true);
	}

	// getConnection
	public static Connection getConnection() {
		return con;
	}
	
	public static void initial() {
		if (SQLManager.connect()) {
			if (SQLManager.isConnected()) {
				Connection connection = SQLManager.getConnection();
				try {
					String sql = "CREATE TABLE IF NOT EXISTS `blockstop` (`id` INT(255) NOT NULL AUTO_INCREMENT,	`uuid` VARCHAR(255), `blocks` BIGINT(255), PRIMARY KEY (`id`));";
					PreparedStatement stmt = connection.prepareStatement(sql);
					stmt.executeUpdate();
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void initializePlayer(Player p) {
		if (SQLManager.connect()) {
			if (SQLManager.isConnected()) {
				Connection connection = SQLManager.getConnection();
				try {
					String sql = "SELECT * FROM `blockstop` WHERE uuid = ?";
					PreparedStatement stmt = connection.prepareStatement(sql);
					stmt.setString(1, p.getUniqueId().toString());
					ResultSet res = stmt.executeQuery();
					if (!res.next()) {
						try {
							String roundstable = "INSERT INTO `blockstop` (uuid, blocks) VALUES (?, ?)";
							PreparedStatement roundstablestmt = connection.prepareStatement(roundstable);
							roundstablestmt.setString(1, p.getUniqueId().toString());
							roundstablestmt.setInt(2, 0);
							roundstablestmt.executeUpdate();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
