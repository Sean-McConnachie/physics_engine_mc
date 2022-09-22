package com.seanmcconnachie.physicsengine.simpledata;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Coordinates;

public record TwoFloats(float x, float y) implements java.io.Serializable {
    public TwoFloats(CommandSourceStack source, Coordinates rotation) {
        this(rotation.getRotation(source).x, rotation.getRotation(source).y);
    }
}
