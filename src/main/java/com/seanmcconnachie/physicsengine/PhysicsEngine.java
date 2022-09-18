package com.seanmcconnachie.physicsengine;

import com.mojang.logging.LogUtils;
import com.seanmcconnachie.physicsengine.events.ServerEvents;
import com.seanmcconnachie.physicsengine.physics.MovingStructure;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(PhysicsEngine.MOD_ID)
public class PhysicsEngine
{
    public static final String MOD_ID = "physicsengine";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static List<MovingStructure> movingStructures = new ArrayList<>();

    public PhysicsEngine()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        ServerEvents.loadMovementStructures();
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
