package com.christchurchcitylibraries.maze.tileentity;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public final class MazeDoorTileEntity extends TileEntity {

	private int colour;
	private int index;
	private final Random random = new Random();

	public MazeDoorTileEntity() {
		this.colour = random.nextInt(16);
		this.index = 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound NBTcompound) {
		super.readFromNBT(NBTcompound);
		colour = NBTcompound.getInteger("colour");
		index = NBTcompound.getInteger("index");
	}

	@Override
	public void writeToNBT(NBTTagCompound NBTcompound) {
		super.writeToNBT(NBTcompound);
		NBTcompound.setInteger("colour", colour);
		NBTcompound.setInteger("index", index);
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound var1 = new NBTTagCompound();
		this.writeToNBT(var1);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, var1);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}
}
