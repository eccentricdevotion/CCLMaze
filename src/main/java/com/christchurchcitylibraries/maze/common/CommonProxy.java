package com.christchurchcitylibraries.maze.common;

import java.io.File;

import com.christchurchcitylibraries.maze.CCLMaze;
import com.christchurchcitylibraries.maze.block.MazeBlocks;
import com.christchurchcitylibraries.maze.config.MazeConfigChangedHandler;
import com.christchurchcitylibraries.maze.config.MazeConfigHandler;
import com.christchurchcitylibraries.maze.crafting.MazeCrafting;
import com.christchurchcitylibraries.maze.gui.MazeGuiHandler;
import com.christchurchcitylibraries.maze.item.MazeItems;
import com.christchurchcitylibraries.maze.server.MazePacketDispatcher;
import com.christchurchcitylibraries.maze.server.MazePlayerEntityEvent;
import com.christchurchcitylibraries.maze.server.MazePlayerServerLogin;
import com.christchurchcitylibraries.maze.tileentity.MazeTileEntities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	private static File configDir;

	public static File getConfigDir() {
		return configDir;
	}

	public void preInit(FMLPreInitializationEvent e) {
		MazeBlocks.createBlocks();
		MazeItems.createItems();
		MazeTileEntities.createTileEntities();
		configDir = new File(e.getModConfigurationDirectory() + "/CCLMaze");
		configDir.mkdirs();
		MazeConfigHandler.initOptions(new File(configDir.getPath(), CCLMaze.MODID + ".cfg"));
	}

	public void init(FMLInitializationEvent e) {
		MazeCrafting.initCrafting();
		MazeConfigChangedHandler configChanged = new MazeConfigChangedHandler();
		MinecraftForge.EVENT_BUS.register(configChanged);
		FMLCommonHandler.instance().bus().register(configChanged);
		MazePlayerServerLogin serverLogin = new MazePlayerServerLogin();
		MinecraftForge.EVENT_BUS.register(serverLogin);
		FMLCommonHandler.instance().bus().register(serverLogin);
		MazePlayerEntityEvent playerEvent = new MazePlayerEntityEvent();
		MinecraftForge.EVENT_BUS.register(playerEvent);
		FMLCommonHandler.instance().bus().register(playerEvent);
		NetworkRegistry.INSTANCE.registerGuiHandler(CCLMaze.instance, new MazeGuiHandler());
		MazePacketDispatcher.registerPackets();
	}

	public void postInit(FMLPostInitializationEvent e) {
		CCLMaze.EDU = Loader.isModLoaded("MinecraftEDU");
	}

	/**
	 * Returns a side-appropriate EntityPlayer for use during message handling
	 */
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}
}
