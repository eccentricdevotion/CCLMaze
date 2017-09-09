package com.christchurchcitylibraries.maze.server;

import com.christchurchcitylibraries.maze.CCLMaze;
import com.christchurchcitylibraries.maze.client.MazeConfigSyncMessage;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * This class will house the SimpleNetworkWrapper instance, which I will name
 * 'dispatcher', as well as give us a logical place from which to register our
 * packets. These two things could be done anywhere, however, even in your Main
 * class, but I will be adding other functionality (see below) that gives this
 * class a bit more utility.
 *
 * While unnecessary, I'm going to turn this class into a 'wrapper' for
 * SimpleNetworkWrapper so that instead of writing
 * "MazePacketDispatcher.dispatcher.{method}" I can simply write
 * "MazePacketDispatcher.{method}" All this does is make it quicker to type and
 * slightly shorter; if you do not care about that, then make the 'dispatcher'
 * field public instead of private, or, if you do not want to add a new class
 * just for one field and one static method that you could put anywhere, feel
 * free to put them wherever.
 *
 * For further convenience, I have also added two extra sendToAllAround methods:
 * one which takes an EntityPlayer and one which takes coordinates.
 */
public class MazePacketDispatcher {
	// a simple counter will allow us to get rid of 'magic' numbers used during
	// packet registration
	private static byte packetId = 0;

	/**
	 * The SimpleNetworkWrapper instance is used both to register and send packets.
	 * Since I will be adding wrapper methods, this field is private, but you should
	 * make it public if you plan on using it directly.
	 */
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(CCLMaze.MODID);

	/**
	 * Call this during pre-init or loading and register all of your packets
	 * (messages) here
	 */
	public static final void registerPackets() {
		// Using an incrementing field instead of hard-coded numerals, I don't need to
		// think
		// about what number comes next or if I missed on should I ever rearrange the
		// order
		// of registration (for instance, if you wanted to alphabetize them... yeah...)
		// It's even easier if you create a convenient 'registerMessage' method:
		MazePacketDispatcher.registerMessage(SyncMazeToServerMessage.Handler.class, SyncMazeToServerMessage.class, Side.SERVER);
		MazePacketDispatcher.registerMessage(TeleportServerMessage.Handler.class, TeleportServerMessage.class, Side.SERVER);
		MazePacketDispatcher.registerMessage(OpenDoorServerMessage.Handler.class, OpenDoorServerMessage.class, Side.SERVER);
		MazePacketDispatcher.registerMessage(MazeConfigSyncMessage.Handler.class, MazeConfigSyncMessage.class, Side.CLIENT);
		MazePacketDispatcher.registerMessage(TimerServerMessage.Handler.class, TimerServerMessage.class, Side.SERVER);
	}

	/**
	 * Registers a message and message handler
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final void registerMessage(Class handlerClass, Class messageClass, Side side) {
		MazePacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
	}

	// ========================================================//
	// The following methods are the 'wrapper' methods; again,
	// this just makes sending a message slightly more compact
	// and is purely a matter of stylistic preference
	// ========================================================//

	/**
	 * Send this message to the specified player. See
	 * {@link SimpleNetworkWrapper#sendTo(IMessage, EntityPlayerMP)}
	 */
	public static final void sendTo(IMessage message, EntityPlayerMP player) {
		MazePacketDispatcher.dispatcher.sendTo(message, player);
	}

	/**
	 * Send this message to everyone within a certain range of a point. See
	 * {@link SimpleNetworkWrapper#sendToDimension(IMessage, NetworkRegistry.TargetPoint)}
	 */
	public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		MazePacketDispatcher.dispatcher.sendToAllAround(message, point);
	}

	/**
	 * Sends a message to everyone within a certain range of the coordinates in the
	 * same dimension.
	 */
	public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
		MazePacketDispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	/**
	 * Sends a message to everyone within a certain range of the player provided.
	 */
	public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
		MazePacketDispatcher.sendToAllAround(message, player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, range);
	}

	/**
	 * Send this message to everyone within the supplied dimension. See
	 * {@link SimpleNetworkWrapper#sendToDimension(IMessage, int)}
	 */
	public static final void sendToDimension(IMessage message, int dimensionId) {
		MazePacketDispatcher.dispatcher.sendToDimension(message, dimensionId);
	}

	/**
	 * Send this message to the server. See
	 * {@link SimpleNetworkWrapper#sendToServer(IMessage)}
	 */
	public static final void sendToServer(IMessage message) {
		MazePacketDispatcher.dispatcher.sendToServer(message);
	}
}
