package com.christchurchcitylibraries.maze.server;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TeleportServerMessage implements IMessage {

	// this will store our Maze data, allowing us to easily read and write
	private NBTTagCompound data;

	// The basic, no-argument constructor MUST be included to use the new automated
	// handling
	public TeleportServerMessage() {
	}

	// if there are any class fields, be sure to provide a constructor that allows
	// for them to be initialized, and use that constructor when sending the packet
	public TeleportServerMessage(NBTTagCompound data) {
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

	public static class Handler extends AbstractServerMessageHandler<TeleportServerMessage> {
		@Override
		public IMessage handleServerMessage(final EntityPlayer player, final TeleportServerMessage message, MessageContext ctx) {
			// teleport player
			message.teleport(player);
			return null;
		}
	}

	private void teleport(EntityPlayer player) {
		player.setPositionAndUpdate(data.getDouble("xPos"), data.getDouble("yPos"), data.getDouble("zPos"));
		switch (data.getInteger("timerType")) {
		case 0:
			MazePlayerExtendedEntity.get(player).setStartTime(System.currentTimeMillis());
			break;
		case 1:
			MazePlayerExtendedEntity.get(player).setEndTime(System.currentTimeMillis());
			break;
		default:
			break;
		}
	}
}
