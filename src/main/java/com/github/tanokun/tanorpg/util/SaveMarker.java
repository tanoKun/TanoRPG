package com.github.tanokun.tanorpg.util;

import com.github.tanokun.tanorpg.util.io.Config;

public interface SaveMarker<V> {
    void save(Config config, String key);

    V load(Config config, String key);
}
