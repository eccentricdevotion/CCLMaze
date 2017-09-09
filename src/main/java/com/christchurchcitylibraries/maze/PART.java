package com.christchurchcitylibraries.maze;

import net.minecraft.util.EnumFacing;

public enum PART {

	START_E(EnumFacing.EAST, 0),
	START_N(EnumFacing.NORTH, 3),
	START_W(EnumFacing.WEST, 2),
	START_S(EnumFacing.SOUTH, 1),
	END_E(EnumFacing.EAST, 5),
	END_N(EnumFacing.NORTH, 2),
	END_W(EnumFacing.WEST, 4),
	END_S(EnumFacing.SOUTH, 3),
	DOOR_E(EnumFacing.EAST, 0),
	DOOR_N(EnumFacing.NORTH, 3),
	DOOR_W(EnumFacing.WEST, 2),
	DOOR_S(EnumFacing.SOUTH, 1),
	WALL(EnumFacing.UP, 1),
	EMPTY(EnumFacing.UP, 1);

	private EnumFacing facing;
	private int meta;

	private PART(EnumFacing facing, int meta) {
		this.facing = facing;
		this.meta = meta;
	}

	public EnumFacing getFacing() {
		return facing;
	}

	public int getFacingMeta() {
		return meta;
	}

	public boolean isDoor() {
		switch (this) {
		case DOOR_E:
		case DOOR_N:
		case DOOR_W:
		case DOOR_S:
		case START_E:
		case START_N:
		case START_W:
		case START_S:
			return true;
		default:
			return false;
		}
	}

	public boolean isStart() {
		switch (this) {
		case START_E:
		case START_N:
		case START_W:
		case START_S:
			return true;
		default:
			return false;
		}
	}

	public boolean isEnd() {
		switch (this) {
		case END_E:
		case END_N:
		case END_W:
		case END_S:
			return true;
		default:
			return false;
		}
	}
}
