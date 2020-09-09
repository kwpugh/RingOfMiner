package com.kwpugh.ring_of_miner;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit
{
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, RingOfMiner.modid);
	
	public static final RegistryObject<Item> RING_OF_MINER = ITEMS.register("ring_of_miner", () -> new ItemRingMiner(new Item.Properties().maxStackSize(1).group(RingOfMiner.ring_of_miner)));
}
