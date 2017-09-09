package com.christchurchcitylibraries.maze.block;

import com.christchurchcitylibraries.maze.CCLMaze;
import com.christchurchcitylibraries.maze.item.MazeItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class MazeFloorBlock extends Block {

	private IIcon icon;

	public MazeFloorBlock(Material material) {
		super(material);
		this.setCreativeTab(MazeItems.mazeTab);
		this.setBlockName("maze_floor");
		this.setBlockTextureName(CCLMaze.MODID + ":maze_floor");
		this.setHardness(200.0F);
		this.setResistance(6000.0F);
		this.setLightLevel(0.5F);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeStone);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return icon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.icon = register.registerIcon(CCLMaze.MODID + ":maze_floor");
	}
}
