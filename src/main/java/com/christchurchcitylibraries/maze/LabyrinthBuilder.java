package com.christchurchcitylibraries.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.christchurchcitylibraries.maze.block.BlockPos;
import com.christchurchcitylibraries.maze.block.MazeBlocks;
import com.christchurchcitylibraries.maze.tileentity.MazeDoorTileEntity;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public final class LabyrinthBuilder {

	private final PART[][] labyrinth;
	private final World world;
	private final BlockPos pos;
	private final ItemStack floorItem;
	private final ItemStack baseItem;
	private final ItemStack wallItem;
	private final Block floor;
	private final Block base;
	private final Block wall;
	private final Block border;
	private final Block nobuild;
	private Block door;
	private int doors = 0;
	private final int size;
	private final boolean forTurtles;
	private List<Integer> indexes = new ArrayList<Integer>();

	public LabyrinthBuilder(PART[][] maze, World world, BlockPos pos, ItemStack floor, ItemStack base, ItemStack wall, boolean turtles) {
		this.labyrinth = maze;
		this.world = world;
		this.pos = pos;
		this.floorItem = floor;
		this.baseItem = base;
		this.wallItem = wall;
		this.floor = Block.getBlockFromItem(this.floorItem.getItem());
		this.base = Block.getBlockFromItem(this.baseItem.getItem());
		this.wall = Block.getBlockFromItem(this.wallItem.getItem());
		this.size = this.labyrinth.length;
		for (int x = 0; x < 10; x++) {
			this.indexes.add(x);
		}
		Collections.shuffle(this.indexes);
		this.border = Block.getBlockById(3724);
		this.nobuild = Block.getBlockById(3720);
		this.forTurtles = turtles;
	}

	public void build() {
		// check for mcEDU
		int down = (CCLMaze.EDU) ? -2 : -1;
		// set maze creator to AIR and remove tile entity
		world.setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
		world.removeTileEntity(pos.getX(), pos.getY(), pos.getZ());
		for (int f = down; f < 3; f++) {
			for (int w = 0; w < size; w++) {
				for (int h = 0; h < size; h++) {
					world.setBlockToAir(pos.getX() + h, pos.getY() + f, pos.getZ() + w);
					if (f == -2) {
						// place border / build disallow blocks
						world.setBlock(pos.getX() + h, pos.getY() + f, pos.getZ() + w, Labyrinths.eduBlocks[w][h] ? border : nobuild);
					}
					PART part = labyrinth[h][w];
					if (f == -1) {
						world.setBlock(pos.getX() + h, pos.getY() + f, pos.getZ() + w, floor, floorItem.getItemDamage(), 2);
					} else if (part.equals(PART.WALL)) {
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
						world.setBlock(pos.getX() + h, pos.getY() + f, pos.getZ() + w, state, data, 2);
					} else {
						world.setBlockToAir(pos.getX() + h, pos.getY() + f, pos.getZ() + w);
					}
				}
			}
		}
		for (int w = 0; w < size; w++) {
			for (int h = 0; h < size; h++) {
				PART part = labyrinth[h][w];
				// determine facing direction
				int facing = part.getFacingMeta();
				if (part.isEnd()) {
					// end, set sign
					world.setBlock(pos.getX() + h, pos.getY() + 1, pos.getZ() + w, MazeBlocks.mazeSign, facing, 3);
					TileEntitySign sign = (TileEntitySign) world.getTileEntity(pos.getX() + h, pos.getY() + 1, pos.getZ() + w);
					sign.signText[0] = "Well done!";
					sign.signText[1] = "You completed";
					sign.signText[2] = "the maze.";
					sign.signText[3] = "Click to exit.";
					sign.markDirty();
				}
				if (part.isDoor() && !forTurtles) {
					door = (part.isStart()) ? MazeBlocks.startDoorBlock : MazeBlocks.questionDoorBlock;
					ItemDoor.placeDoorBlock(world, pos.getX() + h, pos.getY(), pos.getZ() + w, facing, door);
					world.setBlock(pos.getX() + h, pos.getY() + 2, pos.getZ() + w, wall, wallItem.getItemDamage(), 2);
					MazeDoorTileEntity mdteLower = (MazeDoorTileEntity) world.getTileEntity(pos.getX() + h, pos.getY(), pos.getZ() + w);
					MazeDoorTileEntity mdteUpper = (MazeDoorTileEntity) world.getTileEntity(pos.getX() + h, pos.getY() + 1, pos.getZ() + w);
					if (wall == MazeBlocks.wallBlock) {
						mdteLower.setColour(wallItem.getItemDamage());
						mdteUpper.setColour(wallItem.getItemDamage());
					}
					if (!part.isStart()) {
						int x = indexes.get(doors);
						mdteLower.setIndex(x);
						mdteUpper.setIndex(x);
						doors++;
					}
					world.markBlockForUpdate(pos.getX() + h, pos.getY(), pos.getZ() + w);
					mdteLower.markDirty();
					world.markBlockForUpdate(pos.getX() + h, pos.getY() + 1, pos.getZ() + w);
					mdteUpper.markDirty();
				}
			}
		}
	}
}
