package com.github.tanokun.tanorpg.util.particle.data.texture;

import com.github.tanokun.tanorpg.util.particle.ParticleConstants;
import com.github.tanokun.tanorpg.util.particle.PropertyType;
import com.github.tanokun.tanorpg.util.particle.util.ReflectionUtils;
import org.bukkit.inventory.ItemStack;

public class ItemTexture extends ParticleTexture {
    private final ItemStack itemStack;

    public ItemTexture(ItemStack itemStack) {
        super(itemStack == null ? null : itemStack.getType(), (byte)0);
        this.itemStack = itemStack;
    }

    public Object toNMSData() {
        if (this.getMaterial() != null && this.getData() >= 0 && this.getEffect() != null && this.getEffect().hasProperty(PropertyType.REQUIRES_ITEM)) {
            if (ReflectionUtils.MINECRAFT_VERSION < 13) {
                return super.toNMSData();
            } else {
                try {
                    return ParticleConstants.PARTICLE_PARAM_ITEM_CONSTRUCTOR.newInstance(this.getEffect().getNMSObject(), toNMSItemStack(this.getItemStack()));
                } catch (Exception var2) {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public static Object toNMSItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        } else {
            try {
                return ParticleConstants.CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD.invoke((Object)null, itemStack);
            } catch (Exception var2) {
                return null;
            }
        }
    }
}
