package com.github.tanokun.tanorpg.util.particle.data.texture;


import com.github.tanokun.tanorpg.util.particle.ParticleConstants;
import com.github.tanokun.tanorpg.util.particle.PropertyType;
import com.github.tanokun.tanorpg.util.particle.util.ReflectionUtils;
import org.bukkit.Material;

import java.lang.reflect.Field;

public class BlockTexture extends ParticleTexture {
    public BlockTexture(Material material) {
        super(material, (byte)0);
    }

    public BlockTexture(Material material, byte data) {
        super(material, data);
    }

    public Object toNMSData() {
        if (this.getMaterial() != null && this.getMaterial().isBlock() && this.getEffect() != null && this.getEffect().hasProperty(PropertyType.REQUIRES_BLOCK)) {
            if (ReflectionUtils.MINECRAFT_VERSION < 13) {
                return super.toNMSData();
            } else {
                Object block = this.getBlockData(this.getMaterial());
                if (block == null) {
                    return null;
                } else {
                    try {
                        return ParticleConstants.PARTICLE_PARAM_BLOCK_CONSTRUCTOR.newInstance(this.getEffect().getNMSObject(), block);
                    } catch (Exception var3) {
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
    }

    public Object getBlockData(Material material) {
        try {
            Field blockField = ReflectionUtils.getFieldOrNull(ParticleConstants.BLOCKS_CLASS, material.name(), false);
            if (blockField == null) {
                return null;
            } else {
                Object block = ReflectionUtils.readField(blockField, (Object)null);
                return ParticleConstants.BLOCK_GET_BLOCK_DATA_METHOD.invoke(block);
            }
        } catch (Exception var4) {
            return null;
        }
    }
}
