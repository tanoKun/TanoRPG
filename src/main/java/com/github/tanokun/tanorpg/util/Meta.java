package com.github.tanokun.tanorpg.util;

import com.github.tanokun.tanorpg.TanoRPG;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.MetadataValue;

public class Meta {
    public static Object getMetadata(LivingEntity entity, String custom_entity) {
        for(MetadataValue v : entity.getMetadata(custom_entity)){
            if (TanoRPG.getPlugin().getName().equals(v.getOwningPlugin())){
                return v.value();
            }
        }
        return null;
    }
}
