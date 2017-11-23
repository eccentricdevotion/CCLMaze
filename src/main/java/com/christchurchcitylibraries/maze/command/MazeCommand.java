package com.christchurchcitylibraries.maze.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.christchurchcitylibraries.maze.CCLMaze;
import com.christchurchcitylibraries.maze.block.StartDoorBlock;
import com.christchurchcitylibraries.maze.config.MazeConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class MazeCommand implements ICommand {

	private final List<String> aliases = new ArrayList<String>();
	private final List<String> completions = new ArrayList<String>();

	public MazeCommand() {
		aliases.add("labyrinth");
		completions.add("door");
		completions.add("ready");
		completions.add("set");
		completions.add("go");
		completions.add("question");
		completions.add("answers");
		completions.add("correct");
		completions.add("read");
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
		return "maze <door|ready|set|go|question|answers|correct|read>";
	}

	@Override
	public List<String> getCommandAliases() {
		return aliases;
	}

	@SuppressWarnings("unchecked")
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
			if (args[0].equals("ready")) {
				// freeze all non-op players
				List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
				for (EntityPlayerMP pmp : players) {
					if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(pmp.getGameProfile())) {
						pmp.setVelocity(0, 0, 0);
						if (pmp.posX != pmp.prevPosX || pmp.posY != pmp.prevPosY || pmp.posZ != pmp.prevPosZ) {
							pmp.setPositionAndUpdate(pmp.prevPosX, pmp.prevPosY, pmp.prevPosZ);
						}
						KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
						KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode(), false);
						KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), false);
						KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), false);
						if (pmp.capabilities.isFlying) {
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
						}
					}
				}
			}
			if (args[0].equals("set")) {
				// teleport to random door
				List<String> doors = new ArrayList<String>(MazeConfigHandler.doors.getCategoryNames());
				Collections.shuffle(doors);
				int i = 0;
				List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
				for (EntityPlayerMP pmp : players) {
					if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(pmp.getGameProfile())) {
						if (i > players.size() - 1) {
							i = 0;
						}
						String category = doors.get(i);
						double x = MazeConfigHandler.doors.get(category, "x", pmp.posX).getDouble();
						double y = MazeConfigHandler.doors.get(category, "y", pmp.posY).getDouble();
						double z = MazeConfigHandler.doors.get(category, "z", pmp.posZ).getDouble();
						int yaw = MazeConfigHandler.doors.get(category, "yaw", 0).getInt();
						pmp.setPositionAndRotation(x, y, z, yaw, 0);
						i++;
					}
				}
			}
			if (args[0].equals("go")) {
				// UN-freeze all non-op players
				List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
				for (EntityPlayerMP pmp : players) {
					if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(pmp.getGameProfile())) {
						KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
						KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode(), true);
						KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), true);
						KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), true);
						if (pmp.capabilities.isFlying) {
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), true);
							KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), true);
						}
					}
				}
			}
			if (args[0].equals("question")) {
				// /maze question [n] [question text?]
				if (args.length < 3) {
					sender.addChatMessage(new ChatComponentText("Too few arguments. /maze question [n] [question text?]"));
					return;
				}
				// get question number
				String category = "q&a_" + args[1];
				// get question
				StringBuilder sb = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				String question = sb.toString().trim();
				// sync to server config
				if (setConfig(category, "Question", question)) {
					sender.addChatMessage(new ChatComponentText("Config question " + args[1] + " changed!"));
				}
			}
			if (args[0].equals("read")) {
				// /maze read [n]
				// get question number
				String category = "q&a_" + args[1];
				sender.addChatMessage(new ChatComponentText("Question " + args[1] + ": " + readConfig(category)));
			}
			if (args[0].equals("answers")) {
				if (args.length < 6) {
					sender.addChatMessage(new ChatComponentText("Too few arguments. /maze answers [n] [a2] [a1] [a3] [a4]"));
					return;
				}
				// get question number
				String category = "q&a_" + args[1];
				// get answers
				String[] answers = { args[2], args[3], args[4], args[5] };
				// sync to server config
				if (setConfig(category, answers)) {
					sender.addChatMessage(new ChatComponentText("Config answers " + args[1] + " changed!"));
				}
			}
			if (args[0].equals("correct")) {
				if (args.length < 3) {
					sender.addChatMessage(new ChatComponentText("Too few arguments. /maze correct [n] [A|B|C|D]"));
					return;
				}
				// get question number
				String category = "q&a_" + args[1];
				// sync to server config
				if (setConfig(category, "CorrectAnswer", args[2])) {
					sender.addChatMessage(new ChatComponentText("Config correct " + args[1] + " changed!"));
				}
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		// only opped players
		return (sender instanceof EntityPlayer && (MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayer) sender).getGameProfile())));
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		return completions;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int i) {
		return false;
	}

	public boolean setConfig(String category, String option, String value) {
		File configDir = getConfigDir();
		String configPath = configDir.getPath();
		File file = new File(configPath, CCLMaze.MODID + ".cfg");
		System.out.println("Config path: " + file.getAbsolutePath());
		Configuration config = new Configuration(file);
		Property prop = config.get(category, option, "").setValue(value);
		config.save();
		return prop.hasChanged();
	}

	public boolean setConfig(String category, String[] values) {
		File configDir = getConfigDir();
		String configPath = configDir.getPath();
		File file = new File(configPath, CCLMaze.MODID + ".cfg");
		System.out.println("Config path: " + file.getAbsolutePath());
		Configuration config = new Configuration(file);
		Property prop = config.get(category, "Answers", "").setValues(values);
		config.save();
		return prop.hasChanged();
	}

	public String readConfig(String category) {
		File configDir = getConfigDir();
		String configPath = configDir.getPath();
		File file = new File(configPath, CCLMaze.MODID + ".cfg");
		Configuration config = new Configuration(file);
		Property prop = config.get("q&a_" + category, "Question", "", "");
		return prop.getString();
	}

	private File getConfigDir() {
		if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isDedicatedServer()) {
			return new File("config/CCLMaze");
		}
		return new File(Minecraft.getMinecraft().mcDataDir, "config/CCLMaze");
	}
}
