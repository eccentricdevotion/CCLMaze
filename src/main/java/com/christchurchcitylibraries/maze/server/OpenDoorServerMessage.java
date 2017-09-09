package com.christchurchcitylibraries.maze.server;

import java.util.Timer;
import java.util.TimerTask;

import com.christchurchcitylibraries.maze.config.MazeConfigHandler;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class OpenDoorServerMessage implements IMessage {
	// this will store our Maze data, allowing us to easily read and write
	private NBTTagCompound data;
	private Timer timer = new Timer();
	private TimerTask closer;

	// The basic, no-argument constructor MUST be included to use the new automated
	// handling
	public OpenDoorServerMessage() {
	}

	// if there are any class fields, be sure to provide a constructor that allows
	// for them to be initialized, and use that constructor when sending the packet
	public OpenDoorServerMessage(NBTTagCompound data) {
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		// luckily, ByteBufUtils provides an easy way to read the NBT
		data = ByteBufUtils.readTag(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		// ByteBufUtils provides a convenient method for writing the compound
		ByteBufUtils.writeTag(buffer, data);
	}

	public static class Handler extends AbstractServerMessageHandler<OpenDoorServerMessage> {
		@Override
		public IMessage handleServerMessage(final EntityPlayer player, final OpenDoorServerMessage message, MessageContext ctx) {
			// teleport player
			message.openDoor(player);
			return null;
		}
	}

	private void openDoor(EntityPlayer player) {
		World world = player.getEntityWorld();
		// BlockPos
		BlockDoor door = (BlockDoor) world.getBlock(data.getInteger("xPos"), data.getInteger("yPos"), data.getInteger("zPos"));
		door.func_150014_a(world, data.getInteger("xPos"), data.getInteger("yPos"), data.getInteger("zPos"), true);
		closer = new CloseDoorTimerTask(door, world, data.getInteger("xPos"), data.getInteger("yPos"), data.getInteger("zPos"));
		timer.schedule(closer, MazeConfigHandler.DoorCloseTime * 1000);
	}

	private class CloseDoorTimerTask extends TimerTask {
		private BlockDoor door;
		private World world;
		private int x;
		private int y;
		private int z;

		CloseDoorTimerTask(BlockDoor door, World world, int x, int y, int z) {
			this.door = door;
			this.world = world;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void run() {
			door.func_150014_a(world, x, y, z, false);
		}
	}
}
