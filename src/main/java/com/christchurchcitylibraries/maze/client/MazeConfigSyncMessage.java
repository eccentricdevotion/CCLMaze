package com.christchurchcitylibraries.maze.client;

import com.christchurchcitylibraries.maze.config.MazeConfigHandler;
import com.christchurchcitylibraries.maze.config.Question;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MazeConfigSyncMessage implements IMessage {

	// this will store our config data, allowing us to easily read and write
	private NBTTagCompound data;

	public MazeConfigSyncMessage() {
	}

	public MazeConfigSyncMessage(NBTTagCompound data) {
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

	public static class Handler extends AbstractClientMessageHandler<MazeConfigSyncMessage> {
		@Override
		public IMessage handleClientMessage(final EntityPlayer player, final MazeConfigSyncMessage message, MessageContext ctx) {
			message.sync();
			return null;
		}
	}

	private void sync() {
		MazeConfigHandler.config.get("Quizzes", "Attempts", 2).set(data.getInteger("Attempts"));
		MazeConfigHandler.config.get("Creation", "BaseBlock", "minecraft:stained_hardened_clay").set(data.getString("BaseBlock"));
		MazeConfigHandler.config.get("Creation", "BaseColour", "GREEN").set(data.getString("BaseColour"));
		MazeConfigHandler.config.get("Quizzes", "DoorCloseTime", 5).set(data.getInteger("DoorCloseTime"));
		MazeConfigHandler.config.get("Quizzes", "EnableTiming", true).set(data.getBoolean("EnableTiming"));
		MazeConfigHandler.config.get("Creation", "FloorBlock", "minecraft:clay").set(data.getString("FloorBlock"));
		MazeConfigHandler.config.get("Creation", "FloorColour", "WHITE").set(data.getString("FloorColour"));
		Question[] questions = new Question[10];
		for (int q = 0; q < 10; q++) {
			Question question = new Question(data.getString("Question" + q), data.getString("Answers" + q).split("~"), data.getString("Correct" + q));
			questions[q] = question;
		}
		MazeConfigHandler.questions = questions;
		MazeConfigHandler.syncQuestions();
		MazeConfigHandler.config.get("Quizzes", "TeleportX", 0).set(data.getInteger("TeleportX"));
		MazeConfigHandler.config.get("Quizzes", "TeleportY", 64).set(data.getInteger("TeleportY"));
		MazeConfigHandler.config.get("Quizzes", "TeleportZ", 0).set(data.getInteger("TeleportZ"));
		MazeConfigHandler.config.get("Creation", "WallBlock", "minecraft:stained_hardened_clay").set(data.getString("WallBlock"));
		MazeConfigHandler.config.get("Creation", "WallColour", "LIME").set(data.getString("WallColour"));
		MazeConfigHandler.config.save();
//		MazeConfigHandler.syncConfigOptions();
	}
}
