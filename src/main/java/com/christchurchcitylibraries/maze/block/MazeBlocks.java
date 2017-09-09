package com.christchurchcitylibraries.maze.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntitySign;

public final class MazeBlocks {

	public static Block mazeBlock;
	public static Block questionDoorBlock = new QuestionDoorBlock();
	public static Block startDoorBlock = new StartDoorBlock();
	public static Block wallBlock;
	public static Block floorBlock;
	public static Block mazeSign;
	public static Block mazeTimerSign;

	public static void createBlocks() {
		GameRegistry.registerBlock(mazeBlock = new MazeCreatorBlock(Material.iron), "maze_creator");
		GameRegistry.registerBlock(questionDoorBlock = new QuestionDoorBlock(), "question_door");
		GameRegistry.registerBlock(startDoorBlock = new StartDoorBlock(), "start_door");
		GameRegistry.registerBlock(floorBlock = new MazeFloorBlock(Material.clay), "maze_floor");
		GameRegistry.registerBlock(wallBlock = new MazeWallBlock(Material.cloth), MazeBlockMetaItem.class, "maze_wall");
		GameRegistry.registerBlock(mazeSign = new MazeSignBlock(TileEntitySign.class, false), "maze_sign");
		GameRegistry.registerBlock(mazeTimerSign = new MazeTimerSignBlock(TileEntitySign.class, false), "maze_timer_sign");
	}
}
