package com.christchurchcitylibraries.maze.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;

public class MazeTileEntities {

	public static void createTileEntities() {
		GameRegistry.registerTileEntity(MazeCreatorTileEntity.class, "maze_creator_tile_entity");
		GameRegistry.registerTileEntity(MazeDoorTileEntity.class, "maze_door_tile_entity");
	}
}
