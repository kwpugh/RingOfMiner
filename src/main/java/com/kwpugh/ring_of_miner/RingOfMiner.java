package com.kwpugh.ring_of_miner;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("ring_of_miner")
public class RingOfMiner
{
	public static final String modid = "ring_of_miner";
	public static final Logger logger = LogManager.getLogger(modid);
	public static final ItemGroup ring_of_miner = new GroupRingOfMiner();

    public RingOfMiner()
    {
    	ModConfig.loadConfig(ModConfig.CONFIG, FMLPaths.CONFIGDIR.get().resolve("ring-of-miner-general.toml"));

    	ItemInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        logger.info("RingoftheMiner common setup");
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
    	logger.info("RingoftheMiner client setup");
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo("ring_of_miner", "helloworld", () -> { logger.info("Hello world from RingoftheMiner"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
    	logger.info("RingoftheMiner IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
 
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
    {
    	logger.info("RingoftheMiner server setup");
    }
}
