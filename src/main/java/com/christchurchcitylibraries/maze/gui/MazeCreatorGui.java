package com.christchurchcitylibraries.maze.gui;

import java.util.Arrays;

import com.christchurchcitylibraries.maze.config.MazeConfigHandler;
import com.christchurchcitylibraries.maze.item.EnumDyeColor;
import com.christchurchcitylibraries.maze.server.MazePacketDispatcher;
import com.christchurchcitylibraries.maze.server.SyncMazeToServerMessage;
import com.christchurchcitylibraries.maze.tileentity.MazeCreatorTileEntity;

import cpw.mods.fml.client.config.GuiCheckBox;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class MazeCreatorGui extends GuiContainer {

	private final MazeCreatorTileEntity mcte;
	private GuiButton less;
	private GuiButton more;
	private GuiButton moreD;
	private GuiButton moreI;
	private GuiTextField size;
	private GuiTextField direction;
	private GuiTextField index;
	private GuiButton create;
	private GuiButton build;
	private GuiCheckBox random;
	private String[] directions = new String[] { "E", "N", "W", "S" };
	private int i = 0;

	public MazeCreatorGui(IInventory playerInv, MazeCreatorTileEntity mcte) {
		super(new MazeCreatorInventoryHandler(playerInv, mcte));
		this.mcte = mcte;
		this.xSize = 176;
		this.ySize = 224;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(this.less = new GuiButton(39, this.guiLeft + 99, this.guiTop + 73, 20, 20, "<"));
		this.buttonList.add(this.more = new GuiButton(40, this.guiLeft + 149, this.guiTop + 73, 20, 20, ">"));
		this.buttonList.add(this.create = new GuiButton(41, this.guiLeft + 99, this.guiTop + 22, 70, 20, StatCollector.translateToLocal("container.maze_creator.create_maze")));
		this.buttonList.add(this.build = new GuiButton(42, this.guiLeft + 99, this.guiTop + 45, 70, 20, StatCollector.translateToLocal("container.maze_creator.create_labyrinth")));
		size = new GuiTextField(this.fontRendererObj, 120, 74, 28, 18);
		size.setMaxStringLength(3);
		size.setText("" + this.mcte.getSize());
		this.buttonList.add(this.moreD = new GuiButton(45, this.guiLeft + 149, this.guiTop + 93, 20, 20, ">"));
		direction = new GuiTextField(this.fontRendererObj, 120, 94, 28, 18);
		direction.setMaxStringLength(1);
		direction.setText("E");
		this.buttonList.add(this.moreI = new GuiButton(48, this.guiLeft + 149, this.guiTop + 113, 20, 20, ">"));
		index = new GuiTextField(this.fontRendererObj, 120, 114, 28, 18);
		index.setMaxStringLength(1);
		index.setText("1");
		this.buttonList.add(this.random = new GuiCheckBox(50, this.guiLeft + 10, this.guiTop + 78, "Random", false));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.mc.getTextureManager().bindTexture(new ResourceLocation("cclmaze:textures/gui/maze_creator.png"));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// #404040 = 4210752
		String s = "Maze Creator";
		this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString("Inventory", 8, 130, 4210752);
		String f = StatCollector.translateToLocal("container.maze_creator.floor");
		this.fontRendererObj.drawString(f, 30, 58, 4210752);
		String b = StatCollector.translateToLocal("container.maze_creator.base");
		this.fontRendererObj.drawString(b, 30, 40, 4210752);
		String w = StatCollector.translateToLocal("container.maze_creator.wall");
		this.fontRendererObj.drawString(w, 30, 22, 4210752);
		String d = StatCollector.translateToLocal("container.maze_creator.direction");
		this.fontRendererObj.drawString(d, 68, 100, 4210752);
		String i = StatCollector.translateToLocal("container.maze_creator.index");
		this.fontRendererObj.drawString(i, 68, 119, 4210752);
		this.size.drawTextBox();
		this.direction.drawTextBox();
		this.index.drawTextBox();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		// Call this methods super, as it draws buttons and stuff
		super.drawScreen(mouseX, mouseY, f);

		// Add the hovering for all GuiButtons in the buttonList
		if (create.func_146115_a()) {
			String[] desc = { "Build a random maze", "of the specified size" };
			drawHoveringText(Arrays.asList(desc), mouseX, mouseY, fontRendererObj);
		}
		if (build.func_146115_a()) {
			String[] desc = { "Build a labyrinth" };
			drawHoveringText(Arrays.asList(desc), mouseX, mouseY, fontRendererObj);
		}
		if (random.func_146115_a()) {
			String[] desc = { "Build a random labyrinth" };
			drawHoveringText(Arrays.asList(desc), mouseX, mouseY, fontRendererObj);
		}
		if (more.func_146115_a()) {
			String[] desc = { "Increase maze size" };
			drawHoveringText(Arrays.asList(desc), mouseX, mouseY, fontRendererObj);
		}
		if (less.func_146115_a()) {
			String[] desc = { "Decrease maze size" };
			drawHoveringText(Arrays.asList(desc), mouseX, mouseY, fontRendererObj);
		}
		if (moreD.func_146115_a()) {
			String[] desc = { "Cycle Labyrinth entry direction" };
			drawHoveringText(Arrays.asList(desc), mouseX, mouseY, fontRendererObj);
		}
		if (moreI.func_146115_a()) {
			String[] desc = { "Cycle labyrinth index number" };
			drawHoveringText(Arrays.asList(desc), mouseX, mouseY, fontRendererObj);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == this.less) {
			int s = Integer.valueOf(this.size.getText());
			if (s > 6) { // 8
				s -= 2;
				size.setText("" + s);
				this.mcte.setSize(s);
			}
		}
		if (button == this.more) {
			int s = Integer.valueOf(this.size.getText());
			if (s < 100) {
				s += 2;
				size.setText("" + s);
				this.mcte.setSize(s);
			}
		}
		if (button == this.moreD) {
			if (i == 3) {
				i = 0;
			} else {
				i++;
			}
			direction.setText(directions[i]);
			this.mcte.setDirection(directions[i]);
		}
		if (button == this.moreI) {
			int s = Integer.valueOf(this.index.getText());
			if (s == 8) {
				s = 1;
			} else {
				s++;
			}
			index.setText("" + s);
			this.mcte.setIndex(s);
		}
		if (button == this.random) {
			this.mcte.setRandom(((GuiCheckBox) button).isChecked());
		}
		if (button == this.create || button == this.build) {
			// get settings from this GUI
			NBTTagCompound compound = new NBTTagCompound();
			// BlockPos
			compound.setInteger("xPos", this.mcte.xCoord);
			compound.setInteger("yPos", this.mcte.yCoord);
			compound.setInteger("zPos", this.mcte.zCoord);
			// size
			int s = Integer.valueOf(this.size.getText());
			compound.setInteger("size", s);
			// direction
			compound.setString("direction", this.direction.getText());
			// index
			int i = Integer.valueOf(this.index.getText());
			compound.setInteger("index", i);
			// random
			compound.setBoolean("random", this.random.isChecked());
			// wall
			ItemStack wall = this.mcte.getStackInSlot(0);
			if (wall == null) {
				// get defaults from config
				Block w = Block.getBlockFromName(MazeConfigHandler.WallBlock);
				int wc = EnumDyeColor.valueOf(MazeConfigHandler.WallColour).getItemDamage();
				wall = new ItemStack(w, 1, wc);
			}
			NBTTagCompound wallStack = new NBTTagCompound();
			wall.writeToNBT(wallStack);
			compound.setTag("wall", wallStack);
			// base
			ItemStack base = this.mcte.getStackInSlot(1);
			if (base == null) {
				// get defaults from config
				Block b = Block.getBlockFromName(MazeConfigHandler.BaseBlock);
				int bc = EnumDyeColor.valueOf(MazeConfigHandler.BaseColour).getItemDamage();
				base = new ItemStack(b, 1, bc);
			}
			NBTTagCompound baseStack = new NBTTagCompound();
			base.writeToNBT(baseStack);
			compound.setTag("base", baseStack);
			// floor
			ItemStack floor = this.mcte.getStackInSlot(2);
			if (floor == null) {
				// get defaults from config
				Block f = Block.getBlockFromName(MazeConfigHandler.FloorBlock);
				int fc = EnumDyeColor.valueOf(MazeConfigHandler.FloorColour).getItemDamage();
				floor = new ItemStack(f, 1, fc);
			}
			NBTTagCompound floorStack = new NBTTagCompound();
			floor.writeToNBT(floorStack);
			compound.setTag("floor", floorStack);
			boolean isLabyrinth = (button == this.build);
			compound.setBoolean("labyrinth", isLabyrinth);
			// clear the inventory slots
			for (int j = 0; j < this.mcte.getSizeInventory(); j++) {
				this.mcte.setInventorySlotContents(i, null);
			}
			// notify server to build maze
			MazePacketDispatcher.sendToServer(new SyncMazeToServerMessage(compound));
			this.mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		}
	}
}
