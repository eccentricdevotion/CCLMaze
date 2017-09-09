package com.christchurchcitylibraries.maze.block;

import java.util.List;

import com.christchurchcitylibraries.maze.CCLMaze;
import com.christchurchcitylibraries.maze.item.MazeItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class MazeWallBlock extends Block {

	public static String modid = CCLMaze.MODID;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	protected MazeWallBlock(Material material) {
		super(material);
		this.setCreativeTab(MazeItems.mazeTab);
		this.setBlockName("maze_wall");
		this.setBlockTextureName(CCLMaze.MODID + ":maze_wall");
		this.setHardness(200.0F);
		this.setResistance(6000.0F);
		this.setLightLevel(0.5F);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta > 15) {
			meta = 5;
		}
		return icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.icons = new IIcon[16];
		this.icons[0] = register.registerIcon(CCLMaze.MODID + ":maze_wall_white");
		this.icons[1] = register.registerIcon(CCLMaze.MODID + ":maze_wall_orange");
		this.icons[2] = register.registerIcon(CCLMaze.MODID + ":maze_wall_magenta");
		this.icons[3] = register.registerIcon(CCLMaze.MODID + ":maze_wall_light_blue");
		this.icons[4] = register.registerIcon(CCLMaze.MODID + ":maze_wall_yellow");
		this.icons[5] = register.registerIcon(CCLMaze.MODID + ":maze_wall_lime");
		this.icons[6] = register.registerIcon(CCLMaze.MODID + ":maze_wall_pink");
		this.icons[7] = register.registerIcon(CCLMaze.MODID + ":maze_wall_gray");
		this.icons[8] = register.registerIcon(CCLMaze.MODID + ":maze_wall_silver");
		this.icons[9] = register.registerIcon(CCLMaze.MODID + ":maze_wall_cyan");
		this.icons[10] = register.registerIcon(CCLMaze.MODID + ":maze_wall_purple");
		this.icons[11] = register.registerIcon(CCLMaze.MODID + ":maze_wall_blue");
		this.icons[12] = register.registerIcon(CCLMaze.MODID + ":maze_wall_brown");
		this.icons[13] = register.registerIcon(CCLMaze.MODID + ":maze_wall_green");
		this.icons[14] = register.registerIcon(CCLMaze.MODID + ":maze_wall_red");
		this.icons[15] = register.registerIcon(CCLMaze.MODID + ":maze_wall_black");
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 16; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
