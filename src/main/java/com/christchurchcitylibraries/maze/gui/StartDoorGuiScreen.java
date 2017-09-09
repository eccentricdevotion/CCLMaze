package com.christchurchcitylibraries.maze.gui;

import com.christchurchcitylibraries.maze.block.BlockPos;
import com.christchurchcitylibraries.maze.config.MazeConfigHandler;
import com.christchurchcitylibraries.maze.server.MazePacketDispatcher;
import com.christchurchcitylibraries.maze.server.TeleportServerMessage;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class StartDoorGuiScreen extends GuiScreen {

	private final EntityPlayer player;
	private final World world;
	private final BlockPos pos;
	private GuiButton enterButton;

	public StartDoorGuiScreen(EntityPlayer player, World world, BlockPos pos) {
		this.player = player;
		this.world = world;
		this.pos = pos;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		String welcome = StatCollector.translateToLocal("screen.start_door.welcome") + " " + player.getDisplayName();
		this.fontRendererObj.drawString(welcome, (this.width / 2 - (this.fontRendererObj.getStringWidth(welcome) / 2)), 35, 6736896); // #66cc00
		String instruct = StatCollector.translateToLocal("screen.start_door.instruction");
		this.fontRendererObj.drawString(instruct, (this.width / 2 - (this.fontRendererObj.getStringWidth(instruct) / 2)), 55, 16777215); // #ffffff
		String tries = String.format(StatCollector.translateToLocal("screen.start_door.attempts"), MazeConfigHandler.Attempts);
		this.fontRendererObj.drawString(tries, (this.width / 2 - (this.fontRendererObj.getStringWidth(tries) / 2)), 75, 16777215); // #ffffff
		String warning = StatCollector.translateToLocal("screen.start_door.warning");
		this.fontRendererObj.drawString(warning, (this.width / 2 - (this.fontRendererObj.getStringWidth(warning) / 2)), 95, 16777215); // #ffffff
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		this.buttonList.add(this.enterButton = new GuiButton(0, this.width / 2 - 100, 125, StatCollector.translateToLocal("screen.start_door.enter")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == this.enterButton) {
			double dy = 0.0;
			if ((world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ()) & 8) != 0) {
				dy = 1.0;
			}
			// send Server teleport message
			NBTTagCompound compound = new NBTTagCompound();
			compound.setDouble("xPos", pos.getX() + 0.5);
			compound.setDouble("yPos", pos.getY() - dy);
			compound.setDouble("zPos", pos.getZ() + 0.5);
			compound.setInteger("timerType", 0);
			MazePacketDispatcher.sendToServer(new TeleportServerMessage(compound));
			this.mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		}
	}
}
