package com.christchurchcitylibraries.maze;

import com.christchurchcitylibraries.maze.block.BlockPos;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public final class MazeBuilder {

	private final boolean[][] maze;
	private World world;
	private BlockPos pos;
	private final ItemStack floorItem;
	private final ItemStack baseItem;
	private final ItemStack wallItem;
	private final Block floor;
	private final Block base;
	private final Block wall;
	private final int size;

	public MazeBuilder(boolean[][] maze, World world, BlockPos pos, ItemStack floor, ItemStack base, ItemStack wall) {
		this.maze = maze;
		this.world = world;
		this.pos = pos;
		this.floorItem = floor;
		this.baseItem = base;
		this.wallItem = wall;
		this.floor = Block.getBlockFromItem(this.floorItem.getItem());
		this.base = Block.getBlockFromItem(this.baseItem.getItem());
		this.wall = Block.getBlockFromItem(this.wallItem.getItem());
		this.size = this.maze.length;
	}

	public void build() {
		// set maze creator to AIR and remove tile entity
		world.setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
		world.removeTileEntity(pos.getX(), pos.getY(), pos.getZ());
		for (int f = -1; f < 3; f++) {
			for (int w = 0; w < size; w++) {
				for (int h = 0; h < size; h++) {
					world.setBlockToAir(pos.getX() + w, pos.getY() + f, pos.getZ() + h);
					if (maze[w][h]) {
						Block state;
						int data;
						switch (f) {
						case -1:
							state = floor;
							data = floorItem.getItemDamage();
							break;
						case 0:
							state = base;
							data = baseItem.getItemDamage();
							break;
						default:
							state = wall;
							data = wallItem.getItemDamage();
							break;
						}
						world.setBlock(pos.getX() + w, pos.getY() + f, pos.getZ() + h, state, data, 2);
					} else if (f == -1) {
						world.setBlock(pos.getX() + w, pos.getY() + f, pos.getZ() + h, floor, floorItem.getItemDamage(), 2);
					} else {
						world.setBlockToAir(pos.getX() + w, pos.getY() + f, pos.getZ() + h);
					}
				}
			}
		}
	}
}
