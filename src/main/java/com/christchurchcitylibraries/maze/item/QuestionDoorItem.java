package com.christchurchcitylibraries.maze.item;

import com.christchurchcitylibraries.maze.CCLMaze;
import com.christchurchcitylibraries.maze.block.MazeBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class QuestionDoorItem extends ItemDoor {

	public static String modid = CCLMaze.MODID;

	public QuestionDoorItem() {
		super(Material.iron);
		this.setUnlocalizedName("question_door_item");
		this.setCreativeTab(MazeItems.mazeTab);
		this.setTextureName(CCLMaze.MODID + ":question_door_item");
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
		if (par7 != 1) {
			return false;
		} else {
			++y;
			Block block;

			block = MazeBlocks.questionDoorBlock;

			if (player.canPlayerEdit(x, y, z, par7, itemStack) && player.canPlayerEdit(x, y + 1, z, par7, itemStack)) {
				if (!block.canPlaceBlockAt(world, x, y, z)) {
					return false;
				} else {
					int i = MathHelper.floor_double((double) ((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
					placeDoorBlock(world, x, y, z, i, block);
					--itemStack.stackSize;
					return true;
				}
			} else {
				return false;
			}
		}
	}
}
