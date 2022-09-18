package com.seanmcconnachie.physicsengine.physics;

import net.minecraftforge.event.TickEvent;

public interface MovementBase {
    void update(TickEvent.ServerTickEvent event);
}
