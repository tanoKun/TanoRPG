package com.github.tanokun.tanorpg.util;

import com.github.tanokun.tanorpg.TanoRPG;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class Meta {
    public static Object getMetadata(LivingEntity entity, String name) {
        for (MetadataValue v : entity.getMetadata(name)) {
            if (TanoRPG.getPlugin().getName().equals(v.getOwningPlugin())) {
                return v.value();
            }
        }
        return null;
    }

    public static void setMetadata(LivingEntity entity, String name, Object o) {
        entity.setMetadata(name, new FixedMetadataValue(TanoRPG.getPlugin(), o));
    }
}