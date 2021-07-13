package net.mysticnetwork.blocktop.managers;

import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderManager extends PlaceholderExpansion {

	public String getIdentifier() {
		return "mbt";
	}

	public String getPlugin() {
		return null;
	}

	public String getAuthor() {
		return "Sage";
	}

	public String getVersion() {
		return "1.0";
	}

	public String onPlaceholderRequest(Player player, String identifier) {
		if (player == null) {
			return "";
		}
		if (identifier.equalsIgnoreCase("mined")) {
			return BlocksTopManager.getBlocks(player) + "";
		}
		if (identifier.equalsIgnoreCase("mined_formatted")) {
			return format(BlocksTopManager.getBlocks(player));
		}
		return null;
	}

	public static String format(long value) {
		NavigableMap<Long, String> suffixes = new TreeMap<>();
		suffixes.put(1_000L, "k");
		suffixes.put(1_000_000L, "M");
		suffixes.put(1_000_000_000L, "G");
		suffixes.put(1_000_000_000_000L, "T");
		suffixes.put(1_000_000_000_000_000L, "P");
		suffixes.put(1_000_000_000_000_000_000L, "E");
		if (value == Long.MIN_VALUE)
			return format(Long.MIN_VALUE + 1);
		if (value < 0)
			return "-" + format(-value);
		if (value < 1000)
			return Long.toString(value);
		Entry<Long, String> e = suffixes.floorEntry(value);
		Long divideBy = e.getKey();
		String suffix = e.getValue();
		long truncated = value / (divideBy / 10);
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}
}
