package com.christchurchcitylibraries.maze;

import java.util.HashMap;

import com.christchurchcitylibraries.maze.command.MazeCommand;
import com.christchurchcitylibraries.maze.common.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = CCLMaze.MODID, name = CCLMaze.MODNAME, version = CCLMaze.VERSION, guiFactory = "com.christchurchcitylibraries.maze.config.MazeGuiFactory")

public class CCLMaze {

	@SidedProxy(clientSide = "com.christchurchcitylibraries.maze.client.ClientProxy", serverSide = "com.christchurchcitylibraries.maze.server.ServerProxy")
	public static CommonProxy proxy;

	public static final String MODID = "cclmaze";
	public static final String MODNAME = "CCL Maze";
	public static final String VERSION = "1.0.0";
	public static final String MAZE_DATA = "cclmaze_data";

	public static boolean EDU = false;
	public static final HashMap<String, Long> timedPlayers = new HashMap<String, Long>();

	@Instance
	public static CCLMaze instance = new CCLMaze();

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		// register server commands
		event.registerServerCommand(new MazeCommand());
	}
}
