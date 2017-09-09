package com.christchurchcitylibraries.maze.gui;

import com.christchurchcitylibraries.maze.block.BlockPos;
import com.christchurchcitylibraries.maze.tileentity.MazeCreatorTileEntity;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MazeGuiHandler implements IGuiHandler {

	public static final int MAZE_CREATOR_GUI_CONTAINER = 0;
	public static final int START_DOOR_GUI_SCREEN = 1;
	public static final int QUESTION_DOOR_GUI_SCREEN = 2;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == MAZE_CREATOR_GUI_CONTAINER) {
			return new MazeCreatorInventoryHandler(player.inventory, (MazeCreatorTileEntity) world.getTileEntity(x, y, z));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == MAZE_CREATOR_GUI_CONTAINER) {
			return new MazeCreatorGui(player.inventory, (MazeCreatorTileEntity) world.getTileEntity(x, y, z));
		}
		if (ID == START_DOOR_GUI_SCREEN) {
			return new StartDoorGuiScreen(player, world, new BlockPos(x, y, z));
		}
		if (ID == QUESTION_DOOR_GUI_SCREEN) {
			return new QuestionDoorGuiScreen(player, world, new BlockPos(x, y, z));
		}
		return null;
	}
}
