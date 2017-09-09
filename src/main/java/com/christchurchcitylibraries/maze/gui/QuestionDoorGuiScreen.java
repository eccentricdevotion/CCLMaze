package com.christchurchcitylibraries.maze.gui;

import com.christchurchcitylibraries.maze.block.BlockPos;
import com.christchurchcitylibraries.maze.config.MazeConfigHandler;
import com.christchurchcitylibraries.maze.config.MazePlayerConfigLoader;
import com.christchurchcitylibraries.maze.config.Question;
import com.christchurchcitylibraries.maze.server.MazePacketDispatcher;
import com.christchurchcitylibraries.maze.server.OpenDoorServerMessage;
import com.christchurchcitylibraries.maze.server.TeleportServerMessage;
import com.christchurchcitylibraries.maze.tileentity.MazeDoorTileEntity;

import cpw.mods.fml.client.config.GuiCheckBox;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class QuestionDoorGuiScreen extends GuiScreen {

	private final EntityPlayer player;
	private final World world;
	private final BlockPos pos;
	private final Configuration playerConfig;
	private GuiCheckBox answerA;
	private GuiCheckBox answerB;
	private GuiCheckBox answerC;
	private GuiCheckBox answerD;
	private GuiButton enterButton;
	private String question = "";
	private String answer = "";
	private String given = "Z";
	private String error = "";
	private int attempts = 0;

	public QuestionDoorGuiScreen(EntityPlayer player, World world, BlockPos pos) {
		this.player = player;
		this.world = world;
		this.pos = pos;
		this.playerConfig = getConfig(player.getDisplayName());
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		String open = StatCollector.translateToLocal("screen.question_door.open");
		this.fontRendererObj.drawString(open, 115, 24, 6648628); // lime
		this.fontRendererObj.drawString(question, 115, 48, 16777215); // #ffffff
		this.fontRendererObj.drawString(error, 115, this.height / 2 + 48, 9255982); // #ffffff
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		// get the question and answers associated with this door
		MazeDoorTileEntity mdte = (MazeDoorTileEntity) world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
		int index = mdte.getIndex();
		Question data = MazeConfigHandler.questions[index];
		this.question = data.getQuestion();
		this.answer = data.getCorrect();
		String[] answers = data.getAnswers();
		this.buttonList.add(this.answerA = new GuiCheckBox(0, this.width / 2 - 100, this.height / 2 - 48, answers[0], false));
		this.buttonList.add(this.answerB = new GuiCheckBox(1, this.width / 2 - 100, this.height / 2 - 24, answers[1], false));
		this.buttonList.add(this.answerC = new GuiCheckBox(2, this.width / 2 - 100, this.height / 2, answers[2], false));
		this.buttonList.add(this.answerD = new GuiCheckBox(3, this.width / 2 - 100, this.height / 2 + 24, answers[3], false));
		this.buttonList.add(this.enterButton = new GuiButton(4, this.width / 2 - 100, this.height / 2 + 72, StatCollector.translateToLocal("screen.question_door.check")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		this.error = "";
		if (button == this.answerA) {
			// uncheck other buttons
			this.answerB.setIsChecked(false);
			this.answerC.setIsChecked(false);
			this.answerD.setIsChecked(false);
			given = "A";
		}
		if (button == this.answerB) {
			// uncheck other buttons
			this.answerA.setIsChecked(false);
			this.answerC.setIsChecked(false);
			this.answerD.setIsChecked(false);
			given = "B";
		}
		if (button == this.answerC) {
			// uncheck other buttons
			this.answerA.setIsChecked(false);
			this.answerB.setIsChecked(false);
			this.answerD.setIsChecked(false);
			given = "C";
		}
		if (button == this.answerD) {
			// uncheck other buttons
			this.answerA.setIsChecked(false);
			this.answerB.setIsChecked(false);
			this.answerC.setIsChecked(false);
			given = "D";
		}
		if (button == this.enterButton) {
			if (MazeConfigHandler.Attempts > 0 && attempts >= MazeConfigHandler.Attempts && playerConfig != null) {
				// teleport player back to previous door
				NBTTagCompound compound = new NBTTagCompound();
				compound.setDouble("xPos", playerConfig.get("position", "x", MazeConfigHandler.TeleportX + 0.00000000000001).getDouble());
				compound.setDouble("yPos", playerConfig.get("position", "y", MazeConfigHandler.TeleportY + 0.00000000000001).getDouble());
				compound.setDouble("zPos", playerConfig.get("position", "z", MazeConfigHandler.TeleportZ + 0.00000000000001).getDouble());
				compound.setInteger("timerType", 2);
				MazePacketDispatcher.sendToServer(new TeleportServerMessage(compound));
				this.mc.displayGuiScreen(null);
				if (this.mc.currentScreen == null) {
					this.mc.setIngameFocus();
				}
				return;
			}
			attempts++;
			boolean correct = (given.equals(answer));
			if (!answerA.isChecked() && !answerB.isChecked() && !answerC.isChecked() && !answerD.isChecked()) {
				this.error = "You need to select an answer!";
				return;
			}
			if (correct) {
				double dy = 0.0;
				if ((world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ()) & 8) != 0) {
					dy = 1.0;
				}
				// send Server teleport message
				NBTTagCompound compound = new NBTTagCompound();
				compound.setDouble("xPos", pos.getX() + 0.5);
				compound.setDouble("yPos", pos.getY() - dy);
				compound.setDouble("zPos", pos.getZ() + 0.5);
				MazePacketDispatcher.sendToServer(new OpenDoorServerMessage(compound));
				if (playerConfig != null) {
					// update previous door coords
					playerConfig.get("position", "x", MazeConfigHandler.TeleportX + 0.5f).set(player.posX);
					playerConfig.get("position", "y", MazeConfigHandler.TeleportY).set(player.posY - 1.620000004768372);
					playerConfig.get("position", "z", MazeConfigHandler.TeleportZ + 0.5f).set(player.posZ);
					playerConfig.save();
				}
				// close GUI
				this.mc.displayGuiScreen(null);
				if (this.mc.currentScreen == null) {
					this.mc.setIngameFocus();
				}
			} else {
				this.error = "Wrong answer!";
			}
		}
	}

	private Configuration getConfig(String name) {
		return MazePlayerConfigLoader.get(name);
	}
}
