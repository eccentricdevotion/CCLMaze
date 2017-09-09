package com.christchurchcitylibraries.maze.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.christchurchcitylibraries.maze.CCLMaze;
import com.christchurchcitylibraries.maze.gui.MazeGuiHandler;
import com.christchurchcitylibraries.maze.item.MazeItems;
import com.christchurchcitylibraries.maze.tileentity.MazeDoorTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class QuestionDoorBlock extends BlockDoor implements ITileEntityProvider {

	public static String modid = CCLMaze.MODID;
	private final Random random = new Random();
	public static final List<Integer> COLORED = Arrays.asList(35, 159);
	@SideOnly(Side.CLIENT)
	private IIcon[] icons1;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons2;
	private int colour;

	public QuestionDoorBlock() {
		super(Material.iron);
		this.setBlockName("question_door");
		this.setBlockTextureName(CCLMaze.MODID + ":question_door");
		this.setHardness(200.0F);
		this.setResistance(6000.0F);
		this.setLightLevel(0.5F);
		this.setBlockUnbreakable();
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.icons2[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int meta) {
		if (meta != 1 && meta != 0) {
			int i1 = this.func_150012_g(world, x, y, z);
			int j1 = i1 & 3;
			boolean flag = (i1 & 4) != 0;
			boolean flag1 = false;
			boolean flag2 = (i1 & 8) != 0;

			if (flag) {
				if (j1 == 0 && meta == 2) {
					flag1 = !flag1;
				} else if (j1 == 1 && meta == 5) {
					flag1 = !flag1;
				} else if (j1 == 2 && meta == 3) {
					flag1 = !flag1;
				} else if (j1 == 3 && meta == 4) {
					flag1 = !flag1;
				}
			} else {
				if (j1 == 0 && meta == 5) {
					flag1 = !flag1;
				} else if (j1 == 1 && meta == 3) {
					flag1 = !flag1;
				} else if (j1 == 2 && meta == 4) {
					flag1 = !flag1;
				} else if (j1 == 3 && meta == 2) {
					flag1 = !flag1;
				}

				if ((i1 & 16) != 0) {
					flag1 = !flag1;
				}
			}

			return flag2 ? this.icons1[flag1 ? 1 : 0] : this.icons2[flag1 ? 1 : 0];
		} else {
			return this.icons2[0];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.icons1 = new IIcon[2];
		this.icons2 = new IIcon[2];
		this.icons1[0] = register.registerIcon(CCLMaze.MODID + ":question_door_upper");
		this.icons2[0] = register.registerIcon(CCLMaze.MODID + ":question_door_lower");
		this.icons1[1] = new IconFlipped(this.icons1[0], true, false);
		this.icons2[1] = new IconFlipped(this.icons2[0], true, false);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World worldIn, int x, int y, int z) {
		return MazeItems.questionDoorItem;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return (meta & 8) != 0 ? null : MazeItems.questionDoorItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z) {
		int c = random.nextInt(16);
		if (COLORED.contains(Block.getIdFromBlock(worldIn.getBlock(x, y + 1, z)))) {
			c = worldIn.getBlockMetadata(x, y + 1, z);
		} else if (COLORED.contains(Block.getIdFromBlock(worldIn.getBlock(x, y + 2, z + 1)))) {
			c = worldIn.getBlockMetadata(x, y + 2, z + 1);
		} else if (COLORED.contains(Block.getIdFromBlock(worldIn.getBlock(x + 1, y + 2, z)))) {
			c = worldIn.getBlockMetadata(x + 1, y + 2, z);
		} else {
			if ((worldIn.getBlockMetadata(x, y, z) & 8) != 0) {
				MazeDoorTileEntity mdteLower = ((MazeDoorTileEntity) worldIn.getTileEntity(x, y - 1, z));
				if (mdteLower != null) {
					c = mdteLower.getColour();
				}
			} else {
				MazeDoorTileEntity mdte = ((MazeDoorTileEntity) worldIn.getTileEntity(x, y, z));
				if (mdte != null) {
					c = mdte.getColour();
				}
			}
		}
		colour = DoorColourLookup.colours.get(c);
		return colour;
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World worldIn, int meta) {
		return new MazeDoorTileEntity();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new MazeDoorTileEntity();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int a) {
		super.breakBlock(world, x, y, z, block, a);
		world.removeTileEntity(x, y, z);
	}

	@Override
	public boolean onBlockEventReceived(World worldIn, int x, int y, int z, int eventID, int eventParam) {
		super.onBlockEventReceived(worldIn, x, y, z, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(x, y, z);
		return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			player.openGui(CCLMaze.instance, MazeGuiHandler.QUESTION_DOOR_GUI_SCREEN, world, x, y, z);
		}
		return true;
	}
}
