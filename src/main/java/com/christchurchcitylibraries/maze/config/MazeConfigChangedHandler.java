package com.christchurchcitylibraries.maze.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MazeConfigChangedHandler {

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.modID.equals("cclmaze")) {
			MazeConfigHandler.syncConfigOptions();
		}
	}
}
