package com.christchurchcitylibraries.maze.config;

import java.io.File;
import java.util.HashMap;

import com.christchurchcitylibraries.maze.common.CommonProxy;

import net.minecraftforge.common.config.Configuration;

public class MazePlayerConfigLoader {

	private static final HashMap<String, Configuration> configs = new HashMap<String, Configuration>();

	public static Configuration get(String player) {
		if (configs.containsKey(player)) {
			return configs.get(player);
		} else {
			Configuration c = makeConfig(player);
			configs.put(player, c);
			return c;
		}
	}

	private static Configuration makeConfig(String player) {
		try {
			File file = new File(CommonProxy.getConfigDir(), player + ".cfg");
			Configuration config = new Configuration(file);
			String category = "position";
			config.addCustomCategoryComment(category, "Question door teleport location");
			config.getFloat("x", category, MazeConfigHandler.TeleportX + 0.5f, 0, 30000, "The player's x coordinate");
			config.getFloat("y", category, MazeConfigHandler.TeleportY, 0, 30000, "The player's y coordinate");
			config.getFloat("z", category, MazeConfigHandler.TeleportZ + 0.5f, 0, 30000, "The player's z coordinate");
			config.save();
			return config;
		} catch (Exception e) {
			return null;
		}
	}
}
