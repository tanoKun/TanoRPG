package com.github.tanokun.tanorpg.game;

import com.github.tanokun.tanorpg.util.SaveMarker;
import com.github.tanokun.tanorpg.util.io.Config;
import com.google.gson.Gson;

import java.util.HashSet;

    public class OpenPermissionMap implements SaveMarker<com.github.tanokun.tanorpg.game.OpenPermissionMap> {
        private HashSet<String> openPermission = new HashSet<>();

        public void addPermission(String permission){
            openPermission.add(permission);
        }

        public void removePermission(String permission){
            openPermission.remove(permission);
        }

        public boolean hasPermission(String permission){
            return openPermission.contains(permission);
        }

        @Override
        public void save(Config config, String key) {
            Gson gson = new Gson();
            config.getConfig().set(key + "openPermission", gson.toJson(openPermission));
        }

        @Override
        public com.github.tanokun.tanorpg.game.OpenPermissionMap load(Config config, String key) {
            openPermission = new Gson().fromJson(config.getConfig().getString(key + "openPermission"), HashSet.class);
            return this;
        }
}
