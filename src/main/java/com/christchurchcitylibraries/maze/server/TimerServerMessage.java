package com.christchurchcitylibraries.maze.server;

import java.util.List;

import com.christchurchcitylibraries.maze.CCLMaze;
import com.christchurchcitylibraries.maze.config.MazeConfigHandler;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class TimerServerMessage implements IMessage {

	// this will store our Maze data, allowing us to easily read and write
	private NBTTagCompound data;

	// The basic, no-argument constructor MUST be included to use the new automated
	// handling
	public TimerServerMessage() {
	}

	// if there are any class fields, be sure to provide a constructor that allows
	// for them to be initialized, and use that constructor when sending the packet
	public TimerServerMessage(NBTTagCompound data) {
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

	public static class Handler extends AbstractServerMessageHandler<TimerServerMessage> {
		@Override
		public IMessage handleServerMessage(final EntityPlayer player, final TimerServerMessage message, MessageContext ctx) {
			// teleport player
			message.setSignText(player);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private void setSignText(EntityPlayer player) {
		String playerName = "";
		World world = player.getEntityWorld();
		if (MazeConfigHandler.config.get("Quizzes", "EnableTiming", true).getBoolean()) {
			long shortestTime = Long.MAX_VALUE;
			List<EntityPlayer> players = world.playerEntities;
			for (int p = 0; p < players.size(); p++) {
				EntityPlayer mp = players.get(p);
				if (mp instanceof EntityPlayerMP && mp.getExtendedProperties(CCLMaze.MAZE_DATA) != null) {
					MazePlayerExtendedEntity data = MazePlayerExtendedEntity.get(mp);
					long playerTime = data.getEndTime() - data.getStartTime();
					if (playerTime < shortestTime) {
						shortestTime = playerTime;
						playerName = mp.getDisplayName();
					}
				}
			}
		} else {
			playerName = "Timing DISABLED";
		}
		TileEntitySign sign = (TileEntitySign) world.getTileEntity(data.getInteger("xPos"), data.getInteger("yPos"), data.getInteger("zPos"));
		sign.signText[0] = "The fastest";
		sign.signText[1] = "player through";
		sign.signText[2] = "the maze was:";
		sign.signText[3] = playerName;
		world.markBlockForUpdate(data.getInteger("xPos"), data.getInteger("yPos"), data.getInteger("zPos"));
		sign.markDirty();
	}
}
