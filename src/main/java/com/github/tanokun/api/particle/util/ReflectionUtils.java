package com.github.tanokun.api.particle.util;

import com.github.tanokun.api.particle.ParticleConstants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    private static final String NET_MINECRAFT_SERVER_PACKAGE_PATH;
    private static final String CRAFT_BUKKIT_PACKAGE_PATH;
    public static final int MINECRAFT_VERSION;

    public ReflectionUtils() {
    }

    public static String getNMSPath(String path) {
        return getNetMinecraftServerPackagePath() + "." + path;
    }

    public static Class<?> getNMSClass(String path) {
        try {
            return Class.forName(getNMSPath(path));
        } catch (Exception var2) {
            return null;
        }
    }

    public static String getCraftBukkitPath(String path) {
        return getCraftBukkitPackagePath() + "." + path;
    }

    public static Class<?> getCraftBukkitClass(String path) {
        try {
            return Class.forName(getCraftBukkitPath(path));
        } catch (Exception var2) {
            return null;
        }
    }

    public static Method getMethodOrNull(Class targetClass, String methodName, Class<?>... parameterTypes) {
        try {
            return targetClass.getMethod(methodName, parameterTypes);
        } catch (Exception var4) {
            return null;
        }
    }

    public static Field getFieldOrNull(Class targetClass, String fieldName, boolean declared) {
        try {
            return declared ? targetClass.getDeclaredField(fieldName) : targetClass.getField(fieldName);
        } catch (Exception var4) {
            return null;
        }
    }

    public static Constructor getConstructorOrNull(Class targetClass, Class... parameterTypes) {
        try {
            return targetClass.getConstructor(parameterTypes);
        } catch (Exception var3) {
            return null;
        }
    }

    public static boolean existsClass(String path) {
        try {
            Class.forName(path);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static Object readField(Class targetClass, String fieldName, Object object) {
        return targetClass != null && fieldName != null ? readField(getFieldOrNull(targetClass, fieldName, false), object) : null;
    }

    public static Object readField(Field field, Object object) {
        if (field == null) {
            return null;
        } else {
            try {
                return field.get(object);
            } catch (Exception var3) {
                return null;
            }
        }
    }

    public static Object readDeclaredField(Class targetClass, String fieldName, Object object) {
        return targetClass != null && fieldName != null ? readDeclaredField(getFieldOrNull(targetClass, fieldName, true), object) : null;
    }

    public static Object readDeclaredField(Field field, Object object) {
        if (field == null) {
            return null;
        } else {
            field.setAccessible(true);

            try {
                return field.get(object);
            } catch (Exception var3) {
                return null;
            }
        }
    }

    public static void writeDeclaredField(Class targetClass, String fieldName, Object object, Object value) {
        if (targetClass != null && fieldName != null) {
            writeDeclaredField(getFieldOrNull(targetClass, fieldName, true), object, value);
        }
    }

    public static void writeDeclaredField(Field field, Object object, Object value) {
        if (field != null) {
            field.setAccessible(true);

            try {
                field.set(object, value);
            } catch (Exception var4) {
            }

        }
    }

    public static void writeField(Class targetClass, String fieldName, Object object, Object value) {
        if (targetClass != null && fieldName != null) {
            writeField(getFieldOrNull(targetClass, fieldName, false), object, value);
        }
    }

    public static void writeField(Field field, Object object, Object value) {
        if (field != null) {
            try {
                field.set(object, value);
            } catch (Exception var4) {
            }

        }
    }

    public static String getNetMinecraftServerPackagePath() {
        return NET_MINECRAFT_SERVER_PACKAGE_PATH;
    }

    public static String getCraftBukkitPackagePath() {
        return CRAFT_BUKKIT_PACKAGE_PATH;
    }

    public static Object getMinecraftKey(String key) {
        if (key == null) {
            return null;
        } else {
            try {
                return ParticleConstants.MINECRAFT_KEY_CONSTRUCTOR.newInstance(key);
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static Object getPlayerHandle(Player player) {
        if (player != null && player.getClass() == ParticleConstants.CRAFT_PLAYER_CLASS) {
            try {
                return ParticleConstants.CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player);
            } catch (Exception var2) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Object getPlayerConnection(Player target) {
        try {
            return readField(ParticleConstants.ENTITY_PLAYER_PLAYER_CONNECTION_FIELD, getPlayerHandle(target));
        } catch (Exception var2) {
            return null;
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            ParticleConstants.PLAYER_CONNECTION_SEND_PACKET_METHOD.invoke(getPlayerConnection(player), packet);
        } catch (Exception var3) {
        }

    }

    static {
        String serverPath = Bukkit.getServer().getClass().getPackage().getName();
        String version = serverPath.substring(serverPath.lastIndexOf(".") + 1);
        NET_MINECRAFT_SERVER_PACKAGE_PATH = "net.minecraft.server." + version;
        CRAFT_BUKKIT_PACKAGE_PATH = "org.bukkit.craftbukkit." + version;
        String packageVersion = serverPath.substring(serverPath.lastIndexOf(".") + 2);
        MINECRAFT_VERSION = Integer.parseInt(packageVersion.substring(0, packageVersion.lastIndexOf("_")).replace("_", ".").substring(2));
    }
}
