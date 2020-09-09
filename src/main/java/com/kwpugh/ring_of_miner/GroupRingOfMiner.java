package com.kwpugh.ring_of_miner;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class GroupRingOfMiner extends ItemGroup
{
	public GroupRingOfMiner() 
	{
		super("ring_of_miner");
	}

	@Override
	public ItemStack createIcon() 
	{
		return new ItemStack(ItemInit.RING_OF_MINER.get());
	}	
}
