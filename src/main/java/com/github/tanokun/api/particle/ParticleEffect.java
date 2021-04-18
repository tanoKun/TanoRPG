package com.github.tanokun.api.particle;

import com.github.tanokun.api.particle.data.ParticleData;
import com.github.tanokun.api.particle.data.color.NoteColor;
import com.github.tanokun.api.particle.data.color.ParticleColor;
import com.github.tanokun.api.particle.data.color.RegularColor;
import com.github.tanokun.api.particle.data.texture.BlockTexture;
import com.github.tanokun.api.particle.data.texture.ItemTexture;
import com.github.tanokun.api.particle.util.ReflectionUtils;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum ParticleEffect {
    ASH((version) -> {
        return version < 16 ? "NONE" : "ash";
    }, new PropertyType[0]),
    BARRIER((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "BARRIER" : "barrier");
    }, new PropertyType[0]),
    BLOCK_CRACK((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "BLOCK_CRACK" : "block");
    }, new PropertyType[]{PropertyType.REQUIRES_BLOCK}),
    BLOCK_DUST((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "BLOCK_DUST" : "falling_dust");
    }, new PropertyType[]{PropertyType.DIRECTIONAL, PropertyType.REQUIRES_BLOCK}),
    BUBBLE_COLUMN_UP((version) -> {
        return version < 13 ? "NONE" : "bubble_column_up";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    BUBBLE_POP((version) -> {
        return version < 13 ? "NONE" : "bubble_pop";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    CAMPFIRE_COSY_SMOKE((version) -> {
        return version < 14 ? "NONE" : "campfire_cosy_smoke";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    CAMPFIRE_SIGNAL_SMOKE((version) -> {
        return version < 14 ? "NONE" : "campfire_signal_smoke";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    CLOUD((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "CLOUD" : "cloud");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    COMPOSTER((version) -> {
        return version < 14 ? "NONE" : "composter";
    }, new PropertyType[0]),
    CRIMSON_SPORE((version) -> {
        return version < 16 ? "NONE" : "crimson_spore";
    }, new PropertyType[0]),
    CRIT((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "CRIT" : "crit");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    CRIT_MAGIC((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "CRIT_MAGIC" : "enchanted_hit");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    CURRENT_DOWN((version) -> {
        return version < 13 ? "NONE" : "current_down";
    }, new PropertyType[0]),
    DAMAGE_INDICATOR((version) -> {
        return version < 9 ? "NONE" : (version < 13 ? "DAMAGE_INDICATOR" : "damage_indicator");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    DOLPHIN((version) -> {
        return version < 13 ? "NONE" : "dolphin";
    }, new PropertyType[0]),
    DRAGON_BREATH((version) -> {
        return version < 9 ? "NONE" : (version < 13 ? "DRAGON_BREATH" : "dragon_breath");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    DRIP_LAVA((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "DRIP_WATER" : "dripping_lava");
    }, new PropertyType[0]),
    DRIP_WATER((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "DRIP_WATER" : "dripping_water");
    }, new PropertyType[0]),
    DRIPPING_HONEY((version) -> {
        return version < 15 ? "NONE" : "dripping_honey";
    }, new PropertyType[0]),
    DRIPPING_OBSIDIAN_TEAR((version) -> {
        return version < 16 ? "NONE" : "dripping_obsidian_tear";
    }, new PropertyType[0]),
    ENCHANTMENT_TABLE((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "ENCHANTMENT_TABLE" : "enchant");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    END_ROD((version) -> {
        return version < 9 ? "NONE" : (version < 13 ? "END_ROD" : "end_rod");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    EXPLOSION_HUGE((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "EXPLOSION_HUGE" : "explosion_emitter");
    }, new PropertyType[0]),
    EXPLOSION_LARGE((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "EXPLOSION_LARGE" : "explosion");
    }, new PropertyType[0]),
    EXPLOSION_NORMAL((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "EXPLOSION_NORMAL" : "poof");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    FALLING_DUST((version) -> {
        return version < 10 ? "NONE" : (version < 13 ? "FALLING_DUST" : "falling_dust");
    }, new PropertyType[]{PropertyType.REQUIRES_BLOCK}),
    FALLING_HONEY((version) -> {
        return version < 15 ? "NONE" : "falling_honey";
    }, new PropertyType[0]),
    FALLING_NECTAR((version) -> {
        return version < 15 ? "NONE" : "falling_nectar";
    }, new PropertyType[0]),
    FALLING_OBSIDIAN_TEAR((version) -> {
        return version < 16 ? "NONE" : "falling_obsidian_tear";
    }, new PropertyType[0]),
    FIREWORKS_SPARK((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "FIREWORKS_SPARK" : "firework");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    FLAME((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "FLAME" : "flame");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    FLASH((version) -> {
        return version < 14 ? "NONE" : "flash";
    }, new PropertyType[0]),
    FOOTSTEP((version) -> {
        return version > 8 && version < 13 ? "FOOTSTEP" : "NONE";
    }, new PropertyType[0]),
    HEART((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "HEART" : "heart");
    }, new PropertyType[0]),
    ITEM_CRACK((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "ITEM_CRACK" : "item");
    }, new PropertyType[]{PropertyType.DIRECTIONAL, PropertyType.REQUIRES_ITEM}),
    LANDING_HONEY((version) -> {
        return version < 15 ? "NONE" : "landing_honey";
    }, new PropertyType[0]),
    LANDING_OBSIDIAN_TEAR((version) -> {
        return version < 16 ? "NONE" : "landing_obsidian_tear";
    }, new PropertyType[0]),
    LAVA((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "LAVA" : "lava");
    }, new PropertyType[0]),
    MOB_APPEARANCE((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "MOB_APPEARANCE" : "elder_guardian");
    }, new PropertyType[0]),
    NAUTILUS((version) -> {
        return version < 13 ? "NONE" : "nautilus";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    NOTE((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "NOTE" : "note");
    }, new PropertyType[]{PropertyType.COLORABLE}),
    PORTAL((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "PORTAL" : "portal");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    REDSTONE((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "REDSTONE" : "dust");
    }, new PropertyType[]{PropertyType.COLORABLE}),
    REVERSE_PORTAL((version) -> {
        return version < 16 ? "NONE" : "reverse_portal";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    SLIME((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SLIME" : "item_slime");
    }, new PropertyType[0]),
    SMOKE_LARGE((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SMOKE_LARGE" : "large_smoke");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    SMOKE_NORMAL((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SMOKE_NORMAL" : "smoke");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    SNEEZE((version) -> {
        return version < 14 ? "NONE" : "sneeze";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    SNOWBALL((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SNOWBALL" : "item_snowball");
    }, new PropertyType[0]),
    SNOW_SHOVEL((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SNOW_SHOVEL" : "poof");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    SOUL((version) -> {
        return version < 16 ? "NONE" : "soul";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    SOUL_FIRE_FLAME((version) -> {
        return version < 16 ? "NONE" : "soul_fire_flame";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    SPELL((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SPELL" : "effect");
    }, new PropertyType[0]),
    SPELL_INSTANT((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SPELL_INSTANT" : "instant_effect");
    }, new PropertyType[0]),
    SPELL_MOB((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SPELL_MOB" : "entity_effect");
    }, new PropertyType[]{PropertyType.COLORABLE}),
    SPELL_MOB_AMBIENT((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SPELL_MOB_AMBIENT" : "ambient_entity_effect");
    }, new PropertyType[]{PropertyType.COLORABLE}),
    SPELL_WITCH((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SPELL_WITCH" : "witch");
    }, new PropertyType[0]),
    SPIT((version) -> {
        return version < 11 ? "NONE" : (version < 13 ? "SPIT" : "spit");
    }, new PropertyType[0]),
    SQUID_INK((version) -> {
        return version < 13 ? "NONE" : "squid_ink";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    SUSPENDED((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SUSPENDED" : "underwater");
    }, new PropertyType[]{PropertyType.REQUIRES_WATER}),
    SUSPENDED_DEPTH((version) -> {
        return version > 8 && version < 13 ? "SUSPENDED_DEPTH" : "NONE";
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    SWEEP_ATTACK((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "SWEEP_ATTACK" : "sweep_attack");
    }, new PropertyType[]{PropertyType.RESIZEABLE}),
    TOTEM((version) -> {
        return version < 11 ? "NONE" : (version < 13 ? "TOTEM" : "totem_of_undying");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    TOWN_AURA((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "TOWN_AURA" : "mycelium");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    VILLAGER_ANGRY((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "VILLAGER_ANGRY" : "angry_villager");
    }, new PropertyType[0]),
    VILLAGER_HAPPY((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "VILLAGER_HAPPY" : "happy_villager");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    WARPED_SPORE((version) -> {
        return version < 16 ? "NONE" : "warped_spore";
    }, new PropertyType[0]),
    WATER_BUBBLE((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "WATER_BUBBLE" : "bubble");
    }, new PropertyType[]{PropertyType.DIRECTIONAL, PropertyType.REQUIRES_WATER}),
    WATER_DROP((version) -> {
        return version > 8 && version < 13 ? "WATER_DROP" : "NONE";
    }, new PropertyType[0]),
    WATER_SPLASH((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "WATER_SPLASH" : "splash");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    WATER_WAKE((version) -> {
        return version < 8 ? "NONE" : (version < 13 ? "WATER_WAKE" : "fishing");
    }, new PropertyType[]{PropertyType.DIRECTIONAL}),
    WHITE_ASH((version) -> {
        return version < 16 ? "NONE" : "white_ash";
    }, new PropertyType[0]);

    private final IntFunction<String> fieldNameMapper;
    private final PropertyType[] properties;
    public static final ParticleEffect[] VALUES = values();
    public static final Map<ParticleEffect, Object> NMS_EFFECTS = Maps.newHashMap();

    private ParticleEffect(IntFunction<String> fieldNameMapper, PropertyType... properties) {
        this.fieldNameMapper = fieldNameMapper;
        this.properties = properties;
    }

    public String getFieldName() {
        return (String) this.fieldNameMapper.apply(ReflectionUtils.MINECRAFT_VERSION);
    }

    public boolean hasProperty(PropertyType propertyType) {
        return propertyType != null && Arrays.asList(this.properties).contains(propertyType);
    }

    public boolean isCorrectData(ParticleData data) {
        if (data == null) {
            return true;
        } else if (data instanceof ParticleColor) {
            return this.isCorrectColor((ParticleColor) data);
        } else if (data instanceof BlockTexture) {
            return this.hasProperty(PropertyType.REQUIRES_BLOCK);
        } else {
            return data instanceof ItemTexture && this.hasProperty(PropertyType.REQUIRES_ITEM);
        }
    }

    public boolean isCorrectColor(ParticleColor color) {
        boolean var10000;
        label25:
        {
            if (this.hasProperty(PropertyType.COLORABLE)) {
                if (this.equals(NOTE)) {
                    if (color instanceof NoteColor) {
                        break label25;
                    }
                } else if (color instanceof RegularColor) {
                    break label25;
                }
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    public Object getNMSObject() {
        if (NMS_EFFECTS.containsKey(this)) {
            return NMS_EFFECTS.get(this);
        } else {
            String fieldName = this.getFieldName();
            if ("NONE".equals(fieldName)) {
                return null;
            } else if (ReflectionUtils.MINECRAFT_VERSION < 13) {
                return Arrays.stream(ParticleConstants.PARTICLE_ENUM.getEnumConstants()).filter((effect) -> {
                    return effect.toString().equals(fieldName);
                }).findFirst().orElse((Object) null);
            } else {
                try {
                    return ParticleConstants.REGISTRY_GET_METHOD.invoke(ParticleConstants.PARTICLE_TYPE_REGISTRY, ReflectionUtils.getMinecraftKey(fieldName));
                } catch (Exception var3) {
                    return null;
                }
            }
        }
    }

    public void display(Location location, ParticleColor color, Player... players) {
        this.display(location, 0.0F, 0.0F, 0.0F, 1.0F, 0, color, (Player[]) players);
    }

    public void display(Location location, Color color, Player... players) {
        this.display(location, (ParticleColor) (new RegularColor(color)), (Player[]) players);
    }

    public void display(Location location, ParticleColor color, Predicate filter) {
        this.display(location, 0.0F, 0.0F, 0.0F, 1.0F, 0, color, (Predicate) filter);
    }

    public void display(Location location, Color color, Predicate filter) {
        this.display(location, (ParticleColor) (new RegularColor(color)), (Predicate) filter);
    }

    public void display(Location location, ParticleColor color, Collection<? extends Player> players) {
        this.display(location, 0.0F, 0.0F, 0.0F, 1.0F, 0, color, (Collection) players);
    }

    public void display(Location location, Color color, Collection<? extends Player> players) {
        this.display(location, (ParticleColor) (new RegularColor(color)), (Collection) players);
    }

    public void display(Location location, ParticleColor color) {
        this.display(location, 0.0F, 0.0F, 0.0F, 1.0F, 0, color);
    }

    public void display(Location location, Color color) {
        this.display(location, (ParticleColor) (new RegularColor(color)));
    }

    public void display(Location location, Player... players) {
        this.display(location, 0.0F, 0.0F, 0.0F, 0.0F, 1, (ParticleData) null, (Player[]) players);
    }

    public void display(Location location, Predicate filter) {
        this.display(location, 0.0F, 0.0F, 0.0F, 0.0F, 1, (ParticleData) null, (Predicate) filter);
    }

    public void display(Location location, Collection<? extends Player> players) {
        this.display(location, 0.0F, 0.0F, 0.0F, 0.0F, 1, (ParticleData) null, (Collection) players);
    }

    public void display(Location location) {
        this.display(location, 0.0F, 0.0F, 0.0F, 0.0F, 1, (ParticleData) null, (Collection) Bukkit.getOnlinePlayers());
    }

    public void display(Location location, Vector vector, float speed, int amount, ParticleData data, Player... players) {
        this.display(location, (float) vector.getX(), (float) vector.getY(), (float) vector.getZ(), speed, amount, data, players);
    }

    public void display(Location location, Vector vector, float speed, int amount, ParticleData data, Predicate filter) {
        this.display(location, (float) vector.getX(), (float) vector.getY(), (float) vector.getZ(), speed, amount, data, filter);
    }

    public void display(Location location, Vector vector, float speed, int amount, ParticleData data, Collection<? extends Player> players) {
        this.display(location, (float) vector.getX(), (float) vector.getY(), (float) vector.getZ(), speed, amount, data, players);
    }

    public void display(Location location, Vector vector, float speed, int amount, ParticleData data) {
        this.display(location, (float) vector.getX(), (float) vector.getY(), (float) vector.getZ(), speed, amount, data);
    }

    public void display(Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data, Player... players) {
        ArrayList<Player> playerList = (ArrayList) Arrays.stream(players).collect(Collectors.toCollection(ArrayList::new));
        this.display(location, offsetX, offsetY, offsetZ, speed, amount, data, (Collection) playerList);
    }

    public void display(Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data, Predicate<Player> filter) {
        ArrayList<Player> players = (ArrayList) Bukkit.getOnlinePlayers().stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
        this.display(location, offsetX, offsetY, offsetZ, speed, amount, data, (Collection) players);
    }

    public void display(Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data) {
        this.display(location, offsetX, offsetY, offsetZ, speed, amount, data, Bukkit.getOnlinePlayers());
    }

    public void display(Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData data, Collection<? extends Player> players) {
        if (this.isCorrectData(data)) {
            if (data != null) {
                data.setEffect(this);
            }

            ParticlePacket packet = new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, data);
            Object nmsPacket = packet.createPacket(location);
            players.forEach((player) -> {
                ReflectionUtils.sendPacket(player, nmsPacket);
            });
        }
    }

    static {
        Arrays.stream(VALUES).filter((effect) -> {
            return !"NONE".equals(effect.getFieldName());
        }).forEach((effect) -> {
            NMS_EFFECTS.put(effect, effect.getNMSObject());
        });
    }
}