package net.mysticnetwork.blocktop.managers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TaskManager {

	public static void startTask(Plugin pl) {
		long timer = DataManager.config.getInt("options.refresh.interval") * 20;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
			@Override
			public void run() {
				BlocksTopManager.refreshDatabase();
			}
		}, 0, timer);
	}

}
