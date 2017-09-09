package com.christchurchcitylibraries.maze.item;

import com.christchurchcitylibraries.maze.block.MazeBlocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public final class MazeItems {

	public static Item questionDoorItem;
	public static Item startDoorItem;
	public static Item mazeSignItem;
	public static Item mazeTimerSignItem;

	public static final CreativeTabs mazeTab = new CreativeTabs("Maze") {
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(MazeBlocks.mazeBlock);
		}
	};

	public static void createItems() {
		GameRegistry.registerItem(questionDoorItem = new QuestionDoorItem(), "question_door_item");
		GameRegistry.registerItem(startDoorItem = new StartDoorItem(), "start_door_item");
		GameRegistry.registerItem(mazeSignItem = new MazeSignItem(), "maze_sign_item");
		GameRegistry.registerItem(mazeTimerSignItem = new MazeTimerSignItem(), "maze_timer_sign_item");
	}
}
