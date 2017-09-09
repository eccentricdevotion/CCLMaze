package com.christchurchcitylibraries.maze.server;

import com.christchurchcitylibraries.maze.CCLMaze;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;

public class MazePlayerExtendedEntity implements IExtendedEntityProperties {

	private long startTime;
	private long endTime;

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound propertyData = new NBTTagCompound();
		// Write data to propertyData
		propertyData.setLong("start", startTime);
		propertyData.setLong("end", endTime);
		compound.setTag(CCLMaze.MAZE_DATA, propertyData);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		if (compound.hasKey(CCLMaze.MAZE_DATA, Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound propertyData = compound.getCompoundTag(CCLMaze.MAZE_DATA);
			// Read data from propertyData
			startTime = propertyData.getLong("start");
			endTime = propertyData.getLong("end");
		}
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub
		startTime = 0;
		endTime = 0;
	}

	public static MazePlayerExtendedEntity get(Entity p) {
		return (MazePlayerExtendedEntity) p.getExtendedProperties(CCLMaze.MAZE_DATA);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
