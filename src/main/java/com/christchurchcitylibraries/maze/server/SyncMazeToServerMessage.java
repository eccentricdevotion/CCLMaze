package com.christchurchcitylibraries.maze.server;

import com.christchurchcitylibraries.maze.LabyrinthBuilder;
import com.christchurchcitylibraries.maze.Labyrinths;
import com.christchurchcitylibraries.maze.MazeBuilder;
import com.christchurchcitylibraries.maze.MazeGenerator;
import com.christchurchcitylibraries.maze.block.BlockPos;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

// sent using the following code from KeyInputEvent:
// MazePacketDispatcher.sendToServer(new SyncMazeToServerMessage(nbtData));

public class SyncMazeToServerMessage implements IMessage {

	// this will store our Maze data, allowing us to easily read and write
	private NBTTagCompound data;

	// The basic, no-argument constructor MUST be included to use the new automated
	// handling
	public SyncMazeToServerMessage() {
	}

	// if there are any class fields, be sure to provide a constructor that allows
	// for them to be initialized, and use that constructor when sending the packet
	public SyncMazeToServerMessage(NBTTagCompound data) {
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

	public static class Handler extends AbstractServerMessageHandler<SyncMazeToServerMessage> {
		@Override
		public IMessage handleServerMessage(final EntityPlayer player, final SyncMazeToServerMessage message, MessageContext ctx) {
			message.processMazeData(player);
			return null;
		}
	}

	public void processMazeData(EntityPlayer player) {
		// World
		World world = player.getEntityWorld();
		// BlockPos
		BlockPos pos = new BlockPos(data.getInteger("xPos"), data.getInteger("yPos"), data.getInteger("zPos"));
		// wall
		ItemStack wall = ItemStack.loadItemStackFromNBT(data.getCompoundTag("wall"));
		// base
		ItemStack base = ItemStack.loadItemStackFromNBT(data.getCompoundTag("base"));
		// floor
		ItemStack floor = ItemStack.loadItemStackFromNBT(data.getCompoundTag("floor"));
		if (data.getBoolean("labyrinth")) {
			LabyrinthBuilder lb;
			if (data.getBoolean("random")) {
				lb = new LabyrinthBuilder(Labyrinths.getRandomLabyrinth(), world, pos, floor, base, wall);
			} else {
				DIRECTION d = DIRECTION.valueOf(data.getString("direction"));
				int index = data.getInteger("index") - 1;
				switch (d) {
				case N:
					index += 8;
					break;
				case W:
					index += 16;
					break;
				case S:
					index += 24;
					break;
				default:
					break;
				}
				lb = new LabyrinthBuilder(Labyrinths.getLabyrinth(index), world, pos, floor, base, wall);
			}
//			for (int i = 0; i < 8; i++) {
//				LabyrinthBuilder lb = new LabyrinthBuilder(Labyrinths.getLabyrinth(i), world, pos.add(0, 0, (17 * i)), floor, base, wall);
//				// entry on EAST side
//				LabyrinthBuilder lb = new LabyrinthBuilder(Labyrinths.getLabyrinth(i + 8), world, pos.add(0, 0, (17 * i)), floor, base, wall);
//				// entry on NORTH side
//				LabyrinthBuilder lb = new LabyrinthBuilder(Labyrinths.getLabyrinth(i + 16), world, pos.add(0, 0, (17 * i)), floor, base, wall);
//				// entry on WEST side
//				LabyrinthBuilder lb = new LabyrinthBuilder(Labyrinths.getLabyrinth(i + 24), world, pos.add(0, 0, (17 * i)), floor, base, wall);
//				// entry on SOUTH side
//				LabyrinthBuilder lb = new LabyrinthBuilder(Labyrinths.getLabyrinth(7), world, pos, floor, base, wall);
			lb.build();
//			}
		} else {
			MazeGenerator mg = new MazeGenerator(data.getInteger("size") / 2);
			mg.makeMaze();
//			mg.printMaze();
			MazeBuilder mb = new MazeBuilder(mg.getMaze(), world, pos, floor, base, wall);
			mb.build();
		}
	}

	public enum DIRECTION {
		E,
		N,
		W,
		S;
	}
}