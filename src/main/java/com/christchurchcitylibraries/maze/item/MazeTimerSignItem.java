package com.christchurchcitylibraries.maze.item;

import com.christchurchcitylibraries.maze.block.MazeBlocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MazeTimerSignItem extends ItemSign {

	public MazeTimerSignItem() {
		setCreativeTab(MazeItems.mazeTab);
		this.setUnlocalizedName("maze_timer_sign_item");
		this.setTextureName("minecraft:sign");
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int meta, float mouseX, float mouseY, float mouseZ) {
		if (meta == 0) {
			return false;
		} else if (!world.getBlock(x, y, z).getMaterial().isSolid()) {
			return false;
		} else {
			if (meta == 1) {
				++y;
			}
			if (meta == 2) {
				--z;
			}
			if (meta == 3) {
				++z;
			}
			if (meta == 4) {
				--x;
			}
			if (meta == 5) {
				++x;
			}
			if (!player.canPlayerEdit(x, y, z, meta, is)) {
				return false;
			} else if (!Blocks.standing_sign.canPlaceBlockAt(world, x, y, z)) {
				return false;
			} else if (world.isRemote) {
				return true;
			} else {
				if (meta == 1) {
					int i1 = MathHelper.floor_double((double) ((player.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
					world.setBlock(x, y, z, Blocks.standing_sign, i1, 3);
				} else {
					world.setBlock(x, y, z, MazeBlocks.mazeTimerSign, meta, 3);
				}
				--is.stackSize;
//				TileEntitySign tileentitysign = (TileEntitySign) world.getTileEntity(x, y, z);
//				if (tileentitysign != null) {
//					player.displayGUIEditSign(tileentitysign);
//				}
				return true;
			}
		}
	}
}
