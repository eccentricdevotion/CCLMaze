package com.christchurchcitylibraries.maze.block;

import net.minecraft.util.EnumFacing;

public class BlockPos {

	private final int x;
	private final int y;
	private final int z;

	public BlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Add the given coordinates to the coordinates of this BlockPos
	 */
	public BlockPos add(int x, int y, int z) {
		return x == 0 && y == 0 && z == 0 ? this : new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
	}

	/**
	 * Offset this BlockPos 1 block up
	 */
	public BlockPos up() {
		return this.up(1);
	}

	/**
	 * Offset this BlockPos n blocks up
	 */
	public BlockPos up(int n) {
		return this.offset(EnumFacing.UP, n);
	}

	/**
	 * Offset this BlockPos 1 block down
	 */
	public BlockPos down() {
		return this.down(1);
	}

	/**
	 * Offset this BlockPos n blocks down
	 */
	public BlockPos down(int n) {
		return this.offset(EnumFacing.DOWN, n);
	}

	/**
	 * Offset this BlockPos 1 block in northern direction
	 */
	public BlockPos north() {
		return this.north(1);
	}

	/**
	 * Offset this BlockPos n blocks in northern direction
	 */
	public BlockPos north(int n) {
		return this.offset(EnumFacing.NORTH, n);
	}

	/**
	 * Offset this BlockPos 1 block in southern direction
	 */
	public BlockPos south() {
		return this.south(1);
	}

	/**
	 * Offset this BlockPos n blocks in southern direction
	 */
	public BlockPos south(int n) {
		return this.offset(EnumFacing.SOUTH, n);
	}

	/**
	 * Offset this BlockPos 1 block in western direction
	 */
	public BlockPos west() {
		return this.west(1);
	}

	/**
	 * Offset this BlockPos n blocks in western direction
	 */
	public BlockPos west(int n) {
		return this.offset(EnumFacing.WEST, n);
	}

	/**
	 * Offset this BlockPos 1 block in eastern direction
	 */
	public BlockPos east() {
		return this.east(1);
	}

	/**
	 * Offset this BlockPos n blocks in eastern direction
	 */
	public BlockPos east(int n) {
		return this.offset(EnumFacing.EAST, n);
	}

	/**
	 * Offset this BlockPos 1 block in the given direction
	 */
	public BlockPos offset(EnumFacing facing) {
		return this.offset(facing, 1);
	}

	/**
	 * Offsets this BlockPos n blocks in the given direction
	 */
	public BlockPos offset(EnumFacing facing, int n) {
		return n == 0 ? this : new BlockPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
	}

	/**
	 * Get the X coordinate
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Get the Y coordinate
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Get the Z coordinate
	 */
	public int getZ() {
		return this.z;
	}
}