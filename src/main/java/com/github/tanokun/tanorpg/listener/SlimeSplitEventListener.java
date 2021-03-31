package com.github.tanokun.tanorpg.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;

public class SlimeSplitEventListener implements Listener {

    @EventHandler
    public void onSplit(SlimeSplitEvent event){
        event.setCount(0);
    }
}
