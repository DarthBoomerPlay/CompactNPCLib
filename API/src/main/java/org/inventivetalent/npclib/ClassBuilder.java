package org.inventivetalent.npclib;

import javassist.ClassPool;
import org.bukkit.Bukkit;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;
import org.inventivetalent.reflection.util.AccessUtil;

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.List;

public class ClassBuilder {

	protected static Class<?> nmsPacketPlayOutPlayerInfo;
	protected static Class<?> nmsPlayerInfoData;
	protected static Class<?> nmsEnumPlayerInfoAction;
	protected static Class<?> nmsEnumGamemode;
	static           Class<?> generatedChannel;
	static           Class<?> generatedPlayerConnection;
	static Class<?> NetworkManager = Reflection.nmsClassResolver.resolveSilent("NetworkManager");

	public static Object buildPacketPlayOutBed(int id, int x, int y, int z) {
		try {
			Class<?> packetClass = Reflection.nmsClassResolver.resolve("PacketPlayOutBed");
			Object bedPacket = packetClass.newInstance();
			AccessUtil.setAccessible(packetClass.getDeclaredField("a")).set(bedPacket, id);
			if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_8_R1)) {
				AccessUtil.setAccessible(packetClass.getDeclaredField("b")).set(bedPacket, x);
				AccessUtil.setAccessible(packetClass.getDeclaredField("c")).set(bedPacket, y);
				AccessUtil.setAccessible(packetClass.getDeclaredField("d")).set(bedPacket, z);
			} else {
				AccessUtil.setAccessible(packetClass.getDeclaredField("b")).set(bedPacket, Reflection.nmsClassResolver.resolve("BlockPosition").getConstructor(int.class, int.class, int.class).newInstance(x, y, z));
			}
			return bedPacket;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object buildPacketPlayOutAnimation(int entID, int animID) {
		try {
			Class<?> packetClass = Reflection.nmsClassResolver.resolve("PacketPlayOutAnimation");
			Object animationPacket = packetClass.newInstance();
			AccessUtil.setAccessible(packetClass.getDeclaredField("a")).set(animationPacket, entID);
			AccessUtil.setAccessible(packetClass.getDeclaredField("b")).set(animationPacket, animID);

			return animationPacket;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object buildPacketPlayOutEntityStatus(int entityId, byte animationId) {
		try {
			Class<?> packetClass = Reflection.nmsClassResolver.resolve("PacketPlayOutEntityStatus");
			Object animationPacket = packetClass.newInstance();
			AccessUtil.setAccessible(packetClass.getDeclaredField("a")).set(animationPacket, entityId);
			AccessUtil.setAccessible(packetClass.getDeclaredField("b")).set(animationPacket, animationId);

			return animationPacket;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object buildPacketPlayOutEntityTeleport(int id, double x, double y, double z, float yaw, float pitch, boolean onGround, boolean heightCorrection) {
		try {
			Class<?> packetClass = Reflection.nmsClassResolver.resolve("PacketPlayOutEntityTeleport");
			Object teleportPacket;
			if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_8_R1)) {
				teleportPacket = packetClass.getConstructor(int.class, int.class, int.class, int.class, byte.class, byte.class, boolean.class, boolean.class)
						.newInstance(id, floor(x * 32.0D), floor(y * 32.0D), floor(z * 32.0D), (byte) (int) (yaw * 256F / 360F), (byte) (int) (pitch * 256F / 360F), onGround, heightCorrection);
			} else if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
				teleportPacket = packetClass.getConstructor(int.class, int.class, int.class, int.class, byte.class, byte.class, boolean.class)
						.newInstance(id, floor(x * 32.0D), floor(y * 32.0D), floor(z * 32.0D), (byte) (int) (yaw * 256F / 360F), (byte) (int) (pitch * 256F / 360F), onGround);
			} else {
				teleportPacket = packetClass.newInstance();
				AccessUtil.setAccessible(packetClass.getDeclaredField("a")).set(teleportPacket, id);
				AccessUtil.setAccessible(packetClass.getDeclaredField("b")).set(teleportPacket, x);
				AccessUtil.setAccessible(packetClass.getDeclaredField("c")).set(teleportPacket, y);
				AccessUtil.setAccessible(packetClass.getDeclaredField("d")).set(teleportPacket, z);
				AccessUtil.setAccessible(packetClass.getDeclaredField("e")).set(teleportPacket, (byte) (int) (yaw * 256F / 360F));
				AccessUtil.setAccessible(packetClass.getDeclaredField("f")).set(teleportPacket, (byte) (int) (pitch * 256F / 360F));
				AccessUtil.setAccessible(packetClass.getDeclaredField("g")).set(teleportPacket, onGround);
			}

			return teleportPacket;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static int floor(double d1) {
		int i = (int) d1;
		return d1 >= i ? i : i - 1;
	}

	static Class<?> PacketListener = Reflection.nmsClassResolver.resolveSilent("PacketListener");
	//
	//	public static Object buildWatchableObject(int type, int index, Object value) throws Exception {
	//		return NMSClass.nmsWatchableObject.getConstructor(int.class, int.class, Object.class).newInstance(type, index, value);
	//	}
	//
	//	public static Object setDataWatcherValue(Object dataWatcher, int index, Object value) throws Exception {
	//		int type = getDataWatcherValueType(value);
	//
	//		Object map = AccessUtil.setAccessible(NMSClass.nmsDataWatcher.getDeclaredField("dataValues")).get(dataWatcher);
	//		NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("put", int.class, Object.class).invoke(map, index, buildWatchableObject(type, index, value));
	//
	//		return dataWatcher;
	//	}
	//
	//	public static int getDataWatcherValueType(Object value) {
	//		int type = 0;
	//		if (value instanceof Number) {
	//			if (value instanceof Byte) {
	//				type = 0;
	//			} else if (value instanceof Short) {
	//				type = 1;
	//			} else if (value instanceof Integer) {
	//				type = 2;
	//			} else if (value instanceof Float) {
	//				type = 3;
	//			}
	//		} else if (value instanceof String) {
	//			type = 4;
	//		}
	//		// Should not be needed
	//		// else if (value != null && value.getClass().equals(NMSClass.ItemStack)) {
	//		// type = 5;
	//		// } else if (value != null && (value.getClass().equals(NMSClass.ChunkCoordinates) || value.getClass().equals(NMSClass.BlockPosition))) {
	//		// type = 6;
	//		// } else if (value != null && value.getClass().equals(NMSClass.Vector3f)) {
	//		// type = 7;
	//		// }
	//
	//		return type;
	//	}

	//	public static Object getDataWatcher(org.bukkit.entity.Entity ent) {
	//		try {
	//			return NMSClass.nmsEntity.getDeclaredMethod("getDataWatcher").invoke(Reflection.getHandle(ent));
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}
	static         FieldResolver  NetworkManagerFieldResolver  = new FieldResolver(NetworkManager);
	static         MethodResolver NetworkManagerMethodResolver = new MethodResolver(NetworkManager);
	private static boolean        initialized                  = false;

	static {
		try {
			ClassPool classPool = ClassPool.getDefault();
			generatedChannel = ClassGenerator.generateChannel(classPool);
			generatedPlayerConnection = ClassGenerator.generatePlayerConnection(classPool);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static {
		if (!initialized) {
			try {
				nmsPacketPlayOutPlayerInfo = Reflection.nmsClassResolver.resolve("PacketPlayOutPlayerInfo");
				if (Minecraft.VERSION.newerThan(Minecraft.Version.v1_8_R1)) {
					//						if (NPCLib.getServerVersion() <= 181) {
					//							nmsPlayerInfoData = Reflection.getNMSClassWithException("PlayerInfoData");
					//						} else {
					//							nmsPlayerInfoData = Reflection.getNMSClassWithException("PacketPlayOutPlayerInfo$PlayerInfoData");
					//						}
					nmsPlayerInfoData = Reflection.nmsClassResolver.resolve("PlayerInfoData", "PacketPlayOutPlayerInfo$PlayerInfoData");
					//						if (NPCLib.getServerVersion() <= 181) {
					//							nmsEnumPlayerInfoAction = Reflection.getNMSClassWithException("EnumPlayerInfoAction");
					//						} else {
					//							nmsEnumPlayerInfoAction = Reflection.getNMSClassWithException("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
					//						}
					nmsEnumPlayerInfoAction = Reflection.nmsClassResolver.resolve("EnumPlayerInfoAction", "PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
				}
				//					if (NPCLib.getServerVersion() <= 181) {
				//						nmsEnumGamemode = Reflection.getNMSClassWithException("EnumGamemode");
				//					} else {
				//						nmsEnumGamemode = Reflection.getNMSClassWithException("WorldSettings$EnumGamemode");
				//					}
				nmsEnumGamemode = Reflection.nmsClassResolver.resolve("EnumGamemode", "WorldSettings$EnumGamemode");
				initialized = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Object buildPlayerConnection(Object networkManager, Object entity) throws Exception {
		final Object playerConnection = generatedPlayerConnection.getDeclaredConstructor(Reflection.nmsClassResolver.resolve("MinecraftServer"), networkManager.getClass(), Reflection.nmsClassResolver.resolve("EntityPlayer")).newInstance(new MethodResolver(Bukkit.getServer().getClass()).resolve("getServer").invoke(Bukkit.getServer()), networkManager, entity);
		NetworkManagerMethodResolver.resolve(new ResolverQuery("setPacketListener", PacketListener), new ResolverQuery("a", PacketListener)).invoke(networkManager, playerConnection);
		return playerConnection;
	}

	public static Object buildNetworkManager(boolean clientBound) throws Exception {
		Object networkManager = Reflection.nmsClassResolver.resolve("NetworkManager").getConstructor(Reflection.nmsClassResolver.resolve("EnumProtocolDirection")).newInstance(Reflection.nmsClassResolver.resolve("EnumProtocolDirection").getEnumConstants()[clientBound ? 0 : 1]);

		Field channelField = NetworkManagerFieldResolver.resolveByFirstType(Reflection.classResolver.resolve("io.netty.channel.Channel"));
		Field addressField = NetworkManagerFieldResolver.resolveByFirstType(SocketAddress.class);

		Object parentChannel = generatedChannel.newInstance();

		try {
			Field protocolVersionField = Reflection.nmsClassResolver.resolve("NetworkManager").getDeclaredField("c");
			Object attribute = getNMUtilClass("io.netty.util.AttributeMap").getDeclaredMethod("attr", getNMUtilClass("io.netty.util.AttributeKey")).invoke(parentChannel, protocolVersionField.get(null));
			getNMUtilClass("io.netty.util.Attribute").getDeclaredMethod("set", Object.class).invoke(attribute, 5);// TODO version based protocol
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		channelField.set(networkManager, parentChannel);
		addressField.set(networkManager, new SocketAddress() {
			private static final long serialVersionUID = 1108301788933825435L;
		});

		return networkManager;
	}

	@SuppressWarnings({
							  "rawtypes",
							  "unchecked" })
	public static Object buildPlayerInfoPacket(int action, Object profile, int ping, int gamemodeOrdinal, String name) {
		try {
			Object packet = nmsPacketPlayOutPlayerInfo.newInstance();

			if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_8_R1)) {
				AccessUtil.setAccessible(nmsPacketPlayOutPlayerInfo.getDeclaredField("action")).set(packet, action);
				AccessUtil.setAccessible(nmsPacketPlayOutPlayerInfo.getDeclaredField("player")).set(packet, profile);
				AccessUtil.setAccessible(nmsPacketPlayOutPlayerInfo.getDeclaredField("gamemode")).set(packet, gamemodeOrdinal);
				AccessUtil.setAccessible(nmsPacketPlayOutPlayerInfo.getDeclaredField("ping")).set(packet, ping);
				AccessUtil.setAccessible(nmsPacketPlayOutPlayerInfo.getDeclaredField("username")).set(packet, name);
			} else {
				AccessUtil.setAccessible(nmsPacketPlayOutPlayerInfo.getDeclaredField("a")).set(packet, nmsEnumPlayerInfoAction.getEnumConstants()[action]);
				List list = (List) AccessUtil.setAccessible(nmsPacketPlayOutPlayerInfo.getDeclaredField("b")).get(packet);

				Object data;
				// if (NPCLib.getServerVersion() <= 181) {
				data = nmsPlayerInfoData.getConstructor(nmsPacketPlayOutPlayerInfo, getNMUtilClass("com.mojang.authlib.GameProfile"), int.class, nmsEnumGamemode, Reflection.nmsClassResolver.resolve("IChatBaseComponent")).newInstance(packet, profile, ping, nmsEnumGamemode.getEnumConstants()[gamemodeOrdinal], buildChatComponent(name));
				// } else {
				// data = nmsPlayerInfoData.getConstructor(getNMUtilClass("com.mojang.authlib.GameProfile"), int.class, nmsEnumGamemode,
				// Reflection.getNMSClass("IChatBaseComponent")).newInstance(profile, ping, nmsEnumGamemode.getEnumConstants()[gamemodeOrdinal], buildChatComponent(name));
				// }
				list.add(data);
			}
			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object buildChatComponent(String string) {
		Object comp = null;
		try {
			Object[] components = (Object[]) Reflection.obcClassResolver.resolve("util.CraftChatMessage").getMethod("fromString", String.class).invoke(null, string);
			if (components.length > 0) {
				comp = components[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comp;
	}

	public static Class<?> getNMUtilClass(String name) throws ClassNotFoundException {
		if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_8_R1)) { return Class.forName("net.minecraft.util." + name); } else { return Class.forName(name); }
	}
}
