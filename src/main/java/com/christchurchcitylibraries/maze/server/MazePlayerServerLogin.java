package com.christchurchcitylibraries.maze.server;

import com.christchurchcitylibraries.maze.client.MazeConfigSyncMessage;
import com.christchurchcitylibraries.maze.config.MazeConfigHandler;
import com.christchurchcitylibraries.maze.config.Question;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class MazePlayerServerLogin {

	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onPlayerLoginEvent(PlayerLoggedInEvent event) {
		System.out.println("[CCLMaze] PlayerLoggedInEvent");
		if (!event.player.worldObj.isRemote) {
			System.out.println("[CCLMaze] Syncing config to player: " + event.player.getDisplayName());
			NBTTagCompound compound = new NBTTagCompound();
			compound.setInteger("Attempts", MazeConfigHandler.Attempts);
			compound.setString("BaseBlock", MazeConfigHandler.BaseBlock);
			compound.setString("BaseColour", MazeConfigHandler.BaseColour);
			compound.setInteger("DoorCloseTime", MazeConfigHandler.DoorCloseTime);
			compound.setBoolean("EnableTiming", MazeConfigHandler.EnableTiming);
			compound.setString("FloorBlock", MazeConfigHandler.FloorBlock);
			compound.setString("FloorColour", MazeConfigHandler.FloorColour);
			Question[] questions = MazeConfigHandler.questions;
			for (int q = 0; q < 10; q++) {
				compound.setString("Question" + q, questions[q].getQuestion());
				compound.setString("Answers" + q, implode(questions[q].getAnswers()));
				compound.setString("Correct" + q, questions[q].getCorrect());
			}
			compound.setInteger("TeleportX", MazeConfigHandler.TeleportX);
			compound.setInteger("TeleportY", MazeConfigHandler.TeleportY);
			compound.setInteger("TeleportZ", MazeConfigHandler.TeleportZ);
			compound.setString("WallBlock", MazeConfigHandler.WallBlock);
			compound.setString("WallColour", MazeConfigHandler.WallColour);
			MazePacketDispatcher.sendTo(new MazeConfigSyncMessage(compound), (EntityPlayerMP) event.player);
		}
	}

	public static final String implode(String[] answers) {
		StringBuilder out = new StringBuilder();
		boolean first = true;
		for (String a : answers) {
			if (first) {
				first = false;
			} else {
				out.append("~");
			}
			out.append(a);
		}
		return out.toString();
	}
}
