package com.christchurchcitylibraries.maze.crafting;

import com.christchurchcitylibraries.maze.block.MazeBlocks;
import com.christchurchcitylibraries.maze.item.MazeItems;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class MazeCrafting {

	public static void initCrafting() {
		GameRegistry.addRecipe(new ItemStack(MazeBlocks.mazeBlock), "L L", "W W", "DDD", 'L', new ItemStack(Blocks.leaves, 1, 0), 'W', new ItemStack(Blocks.log, 1, 0), 'D', Blocks.dirt);
		GameRegistry.addRecipe(new ItemStack(MazeItems.questionDoorItem), "BB ", "BB ", "BB ", 'B', new ItemStack(MazeBlocks.mazeBlock, 1, 0));
		GameRegistry.addRecipe(new ItemStack(MazeItems.startDoorItem), "CC ", "CC ", "CC ", 'C', Blocks.clay);
	}
}
