package com.kwpugh.ring_of_miner;

import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralModConfig
{   
	public static ForgeConfigSpec.IntValue RING_MINER_COOLDOWN;
    public static ForgeConfigSpec.BooleanValue REVERSE_RING_MINER;
    public static ForgeConfigSpec.BooleanValue DELAY_BREAK_MODE;
    
    public static void init(ForgeConfigSpec.Builder SERVER_BUILDER)
    {      
        SERVER_BUILDER.comment("Misc Ring Settings").push("misc_ring_settings");

        RING_MINER_COOLDOWN = SERVER_BUILDER.comment("Number of ticks duration for the Ring of the Miner cooldown [0-240, default: 80]").defineInRange("ringMinerCooldown", 80, 0, 120);
        REVERSE_RING_MINER = SERVER_BUILDER.comment("Reverse the drop/no drops feature on Ring of Miner [true / false]").define("reverseRingMiner", false);
        DELAY_BREAK_MODE = SERVER_BUILDER.comment("Uses a delayed break mode to reduce stutter and potential lag [true / false]").define("delayedBreakMode", false);
        
        SERVER_BUILDER.pop();
    }
}