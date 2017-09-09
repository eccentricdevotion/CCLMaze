package com.christchurchcitylibraries.maze.block;

import java.util.Random;

import com.christchurchcitylibraries.maze.item.MazeItems;
import com.christchurchcitylibraries.maze.server.MazePacketDispatcher;
import com.christchurchcitylibraries.maze.server.TimerServerMessage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MazeTimerSignBlock extends BlockSign {

	@SuppressWarnings("rawtypes")
	public MazeTimerSignBlock(Class p_i45426_1_, boolean p_i45426_2_) {
		super(p_i45426_1_, p_i45426_2_);
		this.setBlockUnbreakable();
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
		return MazeItems.mazeSignItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return MazeItems.mazeSignItem;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// send Server timer message
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("xPos", x);
		compound.setInteger("yPos", y);
		compound.setInteger("zPos", z);
		MazePacketDispatcher.sendToServer(new TimerServerMessage(compound));
		return true;
	}
}
