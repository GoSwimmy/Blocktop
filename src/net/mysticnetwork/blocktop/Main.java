package net.mysticnetwork.blocktop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.mysticnetwork.blocktop.commands.BlocksTopCommand;
import net.mysticnetwork.blocktop.events.BlockBreak;
import net.mysticnetwork.blocktop.events.CommandPreProcess;
import net.mysticnetwork.blocktop.events.PlayerJoin;
import net.mysticnetwork.blocktop.managers.DataManager;
import net.mysticnetwork.blocktop.managers.PlaceholderManager;
import net.mysticnetwork.blocktop.managers.SQLManager;
import net.mysticnetwork.blocktop.managers.TaskManager;

public class Main extends JavaPlugin {
	
	private static Plugin pl;
	
	public void onEnable() {
		pl = this;
		
		DataManager.setup(this);
		SQLManager.initial();
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			SQLManager.initializePlayer(all);
		}
		
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new PlaceholderManager().register();
		}
		
		getCommand("mysticblocktop").setExecutor(new BlocksTopCommand());
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new CommandPreProcess(), this);
		
		TaskManager.startTask(this);
	}

	public static Plugin getInstance() {
		return pl;
	}
	
}
