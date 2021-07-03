package com.github.tanokun.tanorpg.util;

import com.github.tanokun.tanorpg.util.io.Config;
import org.bukkit.scheduler.BukkitRunnable;

public interface SaveMarker<V> {
    void save(Config config, String key);
    V load(Config config, String key);
}
