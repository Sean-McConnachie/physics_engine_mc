package com.seanmcconnachie.physicsengine.events;

import com.seanmcconnachie.physicsengine.commands.HomeCommands;
import com.seanmcconnachie.physicsengine.commands.PhysicsCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import com.seanmcconnachie.physicsengine.PhysicsEngine;

@Mod.EventBusSubscriber(modid = PhysicsEngine.MOD_ID)
public class ModEvents {

    public static float i = 0;

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        HomeCommands.register(event.getDispatcher());
        PhysicsCommands.register(event.getDispatcher(), event.getBuildContext());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloneEvent(PlayerEvent.Clone event) {
        if(!event.getOriginal().getCommandSenderWorld().isClientSide()) {
            event.getEntity().getPersistentData().putIntArray(PhysicsEngine.MOD_ID + "homepos",
                    event.getOriginal().getPersistentData().getIntArray(PhysicsEngine.MOD_ID + "homepos"));
        }
    }
}