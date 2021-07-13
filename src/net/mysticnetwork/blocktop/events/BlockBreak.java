package net.mysticnetwork.blocktop.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;

import net.mysticnetwork.blocktop.managers.BlocksTopManager;

public class BlockBreak implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if(b.getLocation() != null) {
			WorldGuardPlugin pl = WorldGuardPlugin.inst();
			RegionManager rm = pl.getRegionManager(b.getLocation().getWorld());
			String rawreg = "";
			for(String regs : rm.getRegions().keySet()) {
				if(regs.startsWith("mine-")) {
					rawreg = regs;
				}
			}
			if(rm.getRegion(rawreg).contains(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ())) {
				BlocksTopManager.updatePlayer(p, 1);
			}
		}
	}
}
