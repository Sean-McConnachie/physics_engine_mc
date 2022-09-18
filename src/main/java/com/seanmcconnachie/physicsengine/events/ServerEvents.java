package com.seanmcconnachie.physicsengine.events;

import com.seanmcconnachie.physicsengine.PhysicsEngine;
import com.seanmcconnachie.physicsengine.physics.MovingStructure;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerEvents {
    private int previousLength = -1;
    public static void loadMovementStructures()
    {
        try {
            File file = new File("movement_structures.ser");
            System.out.println("FILE EXISTS: " + file.exists());
            if (file.exists()) {
                List<MovingStructure> movingStructures = new ArrayList<>();
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                movingStructures = (ArrayList) in.readObject();
                System.out.println("Moving structures length: " + movingStructures.size());
                PhysicsEngine.movingStructures = movingStructures;
                in.close();
                fileIn.close();
            }
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void saveMovementStructures()
    {
        try {
            File file = new File("movement_structures.ser");
            file.createNewFile();
            FileOutputStream fileOut =
                    new FileOutputStream(file, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(PhysicsEngine.movingStructures);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onServerSave(LevelEvent.Save event) {
        // Save anything in movementStructures to file
        saveMovementStructures();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (PhysicsEngine.movingStructures.size() != previousLength) {
            System.out.println("movementStructures updated: " + PhysicsEngine.movingStructures.size());
            previousLength = PhysicsEngine.movingStructures.size();
        }
        for (MovingStructure movingStructure : PhysicsEngine.movingStructures) {
            movingStructure.update(event);
        }
    }
}
