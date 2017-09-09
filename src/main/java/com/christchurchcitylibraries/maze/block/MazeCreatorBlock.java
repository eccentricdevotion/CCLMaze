package com.christchurchcitylibraries.maze.block;

import java.util.Random;

import com.christchurchcitylibraries.maze.CCLMaze;
import com.christchurchcitylibraries.maze.gui.MazeGuiHandler;
import com.christchurchcitylibraries.maze.item.MazeItems;
import com.christchurchcitylibraries.maze.tileentity.MazeCreatorTileEntity;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MazeCreatorBlock extends BlockContainer {

	public static String modid = CCLMaze.MODID;

	protected MazeCreatorBlock(Material material) {
		super(material);
		this.setCreativeTab(MazeItems.mazeTab);
		this.setBlockName("maze_creator");
		this.setBlockTextureName(CCLMaze.MODID + ":maze_creator");
		this.setHardness(200.0F);
		this.setResistance(6000.0F);
		this.setLightLevel(0.5F);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new MazeCreatorTileEntity();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
		if (stack.hasDisplayName()) {
			((MazeCreatorTileEntity) worldIn.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.openGui(CCLMaze.instance, MazeGuiHandler.MAZE_CREATOR_GUI_CONTAINER, world, x, y, z);
		}
		return true;
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World worldIn, int meta) {
		return new MazeCreatorTileEntity();
	}

	@Override
	public int getRenderType() {
		return 0;
	}
}
