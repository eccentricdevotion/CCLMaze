package com.christchurchcitylibraries.maze.command;

import java.util.ArrayList;
import java.util.List;

import com.christchurchcitylibraries.maze.block.StartDoorBlock;
import com.christchurchcitylibraries.maze.config.MazeConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MazeCommand implements ICommand {

	private final List<String> aliases = new ArrayList<String>();
	private final List<String> completions = new ArrayList<String>();

	public MazeCommand() {
		aliases.add("labyrinth");
		completions.add("door");
		completions.add("ready");
		completions.add("set");
		completions.add("go");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "maze";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "maze <door>";
	}

	@Override
	public List<String> getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		if (world.isRemote) {
			System.out.println("Processing on Client side");
			MovingObjectPosition omo = Minecraft.getMinecraft().objectMouseOver;
			int ox = omo.blockX;
			int oy = omo.blockY;
			int oz = omo.blockZ;

			System.out.println("Client objectMouseOver: " + ox + "," + oy + "," + oz + " " + world.getBlock(ox, oy, oz).getLocalizedName());
		} else {
			System.out.println("Processing on Server side");
			if (args.length == 0) {
				sender.addChatMessage(new ChatComponentText("Invalid argument"));
				return;
			}
			if (args[0].equals("door")) {
				// get player
				EntityPlayer player = (EntityPlayer) sender;
				// get door
				Vec3 vec3 = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
				Vec3 lookVec = player.getLook(1.0F);
				Vec3 addedVector = vec3.addVector(lookVec.xCoord * 5.0D, lookVec.yCoord * 5.0D, lookVec.zCoord * 5.0D);
				MovingObjectPosition movingObjPos = world.rayTraceBlocks(vec3, addedVector);
				if (movingObjPos != null && movingObjPos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					int x = movingObjPos.blockX;
					int y = movingObjPos.blockY;
					int z = movingObjPos.blockZ;
					Block block = world.getBlock(x, y, z);

					if (block instanceof StartDoorBlock) {
						int data = world.getBlockMetadata(x, y, z);
						float dx = 0.0f;
						float dy = 0.0f;
						float dz = 0.0f;
						if ((data & 8) != 0) {
							dy = 1.0f;
							data = world.getBlockMetadata(x, y - 1, z);
						}
						int yaw = 0;
						switch (data) {
						case 0:
							// east
							yaw = -90;
							dx = -1.5f;
							break;
						case 1:
							// south
							dz = -1.5f;
							break;
						case 2:
							// west
							yaw = 90;
							dx = 1.5f;
							break;
						default:
							// north - 0
							yaw = 180;
							dz = 1.5f;
							break;
						}
						MazeConfigHandler.addDoor(x + dx, y - dy, z + dz, yaw);
						sender.addChatMessage(new ChatComponentText("Adding door to doors.cfg"));
					}
				}
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		return completions;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int i) {
		return false;
	}
}
