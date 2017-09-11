package com.christchurchcitylibraries.maze.server;

import com.christchurchcitylibraries.maze.CCLMaze;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class MazePlayerEntityEvent {

	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayerMP) {
			if (event.entity.getExtendedProperties(CCLMaze.MAZE_DATA) == null) {
				event.entity.registerExtendedProperties(CCLMaze.MAZE_DATA, new MazePlayerExtendedEntity());
			}
		}
	}

	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event) {
		if (event.wasDeath) {
			NBTTagCompound compound = new NBTTagCompound();
			MazePlayerExtendedEntity.get(event.original).saveNBTData(compound);
			MazePlayerExtendedEntity.get(event.entityPlayer).loadNBTData(compound);
		}
	}
}
