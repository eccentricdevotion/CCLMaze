package com.christchurchcitylibraries.maze.block;

import java.util.Random;

import com.christchurchcitylibraries.maze.config.MazeConfigHandler;
import com.christchurchcitylibraries.maze.item.MazeItems;
import com.christchurchcitylibraries.maze.server.MazePacketDispatcher;
import com.christchurchcitylibraries.maze.server.TeleportServerMessage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MazeSignBlock extends BlockSign {

	@SuppressWarnings("rawtypes")
	public MazeSignBlock(Class p_i45426_1_, boolean p_i45426_2_) {
		super(p_i45426_1_, p_i45426_2_);
		this.setBlockUnbreakable();
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return MazeItems.mazeSignItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return MazeItems.mazeSignItem;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// send Server teleport message
		NBTTagCompound compound = new NBTTagCompound();
		double xPos = (double) MazeConfigHandler.TeleportX;
		double yPos = (double) MazeConfigHandler.TeleportY;
		double zPos = (double) MazeConfigHandler.TeleportZ;
		compound.setDouble("xPos", xPos + 0.5);
		compound.setDouble("yPos", yPos);
		compound.setDouble("zPos", zPos + 0.5);
		compound.setInteger("timerType", 1);
		MazePacketDispatcher.sendToServer(new TeleportServerMessage(compound));
		return true;
	}
}
