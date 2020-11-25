package com.kwpugh.ring_of_miner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.GravelBlock;
import net.minecraft.block.SandBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemRingMiner extends Item
{
	public ItemRingMiner(Properties properties)
	{
		super(properties);
	}

	public static final int BREAK_DELAY = 1;

	int ringMinerCooldown = GeneralModConfig.RING_MINER_COOLDOWN.get();
	boolean reverseRingMiner = GeneralModConfig.REVERSE_RING_MINER.get();
	boolean delayedBreakMode = GeneralModConfig.DELAY_BREAK_MODE.get();

	boolean shiftKeyPressed = false;

	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

        ItemStack equippedMain = player.getHeldItemMainhand();

        if(equippedMain == stack)   //Only works in the main hand
        {
        	shiftKeyPressed = player.isSneaking();

        	player.getCooldownTracker().setCooldown(this, ringMinerCooldown);

        	if(!world.isRemote)
			{
				Block block;
				List<BlockPos> poslist = new ArrayList<BlockPos>();

				for (int x = -5; x <= 5; x++)
				{
					for (int y = 0; y <= 4; y++)
					{
						for (int z = -5; z <= 5; z++)
						{
							BlockPos pos = player.getPosition().add(x, y, z);
							block = world.getBlockState(pos).getBlock();
							String blockForgeTags = block.getTags().toString();

							if (block == Blocks.STONE ||
									block.isIn(BlockTags.field_242172_aH) ||
									block.isIn(BlockTags.field_242173_aI) ||
									blockForgeTags.contains("forge:stone") ||
									blockForgeTags.contains("forge:sandstone") ||
									blockForgeTags.contains("forge:sand") ||
									blockForgeTags.contains("forge:dirt") ||
									blockForgeTags.contains("forge:gravel") ||
									block instanceof GravelBlock ||
									block instanceof SandBlock ||
									block == Blocks.DIRT ||
									block == Blocks.GRASS_PATH ||
									block == Blocks.SAND  ||
									block == Blocks.RED_SAND  ||
									block == Blocks.SANDSTONE ||
									block == Blocks.RED_SANDSTONE ||
									block == Blocks.GRAVEL ||
									block == Blocks.GRASS_BLOCK ||
									block == Blocks.COARSE_DIRT ||
									block == Blocks.PODZOL ||
									block == Blocks.MYCELIUM ||
									block == Blocks.GRANITE ||
									block == Blocks.ANDESITE ||
									block == Blocks.DIORITE  ||
									block == Blocks.SOUL_SAND ||
									block == Blocks.SOUL_SOIL ||
									block == Blocks.MOSSY_COBBLESTONE ||
									block == Blocks.MOSSY_COBBLESTONE_SLAB ||
									block == Blocks.MOSSY_COBBLESTONE_STAIRS ||
									block == Blocks.MOSSY_STONE_BRICKS ||
									block == Blocks.MOSSY_STONE_BRICK_STAIRS ||
									block == Blocks.MOSSY_STONE_BRICK_SLAB ||
									block == Blocks.STONE_BRICKS ||
									block == Blocks.STONE_BRICK_STAIRS ||
									block == Blocks.STONE_BRICK_SLAB ||
									block == Blocks.CRACKED_STONE_BRICKS ||
									block == Blocks.INFESTED_CRACKED_STONE_BRICKS ||
									block == Blocks.INFESTED_CHISELED_STONE_BRICKS||
									block == Blocks.INFESTED_COBBLESTONE ||
									block == Blocks.INFESTED_MOSSY_STONE_BRICKS ||
									block == Blocks.END_STONE ||
									block == Blocks.BASALT ||
									block == Blocks.BLACKSTONE ||
									block == Blocks.MAGMA_BLOCK ||
									block == Blocks.NETHERRACK)
							{
								poslist.add(player.getPosition().add(x, y, z));
							}
						}
					}
				}


				if(delayedBreakMode)
				{
					//Test code for block break delay
					if (!poslist.isEmpty())
					{
						List<BlockPos> reversedPosList = reverseList(poslist);
						MinecraftForge.EVENT_BUS.register(new Object()
			            {
			                int delay = BREAK_DELAY;
			                int i = 0;

			                @SubscribeEvent
			                public void onTick(TickEvent.WorldTickEvent event)
			                {
			                    if (delay-- > 0) return;
			                    delay = BREAK_DELAY;
			                    if (i < reversedPosList.size())
			                    {
			                        BlockPos breakPos = reversedPosList.get(i);
			                    	if(shiftKeyPressed)    //NOTE: shift key needs to be held down through the delayed block breaking to get drops
									{
										world.destroyBlock(breakPos, !reverseRingMiner);
									}
									else
									{
										world.destroyBlock(breakPos, reverseRingMiner);
									}

			                        i++;
			                    }
			                    else
			                    {
			                        MinecraftForge.EVENT_BUS.unregister(this);
			                    }
			                }
			            });
					}
				}
				else
				{
					//Traditional method of block break, all at once
					if (!poslist.isEmpty())
					{
						for (int i = 0; i <= poslist.size() - 1; i++)
						{
							BlockPos targetpos = poslist.get(i);
							block = world.getBlockState(targetpos).getBlock();

							if(player.isSneaking())
							{
								world.destroyBlock(targetpos, !reverseRingMiner);
							}
							else
							{
								world.destroyBlock(targetpos, reverseRingMiner);
							}
						}
					}
				}
			}
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
	}

	public static <T> List<T> reverseList(List<T> list)
	{
	    return IntStream.range(0, list.size())
	                   .mapToObj(i -> list.get(list.size() - 1 - i))
	                   .collect(Collectors.toCollection(ArrayList::new));
	}
	
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		String mode;

		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add((new TranslationTextComponent("item.ring_of_miner.ring_of_miner.line1").mergeStyle(TextFormatting.GREEN)));

		if(reverseRingMiner)
		{
			tooltip.add((new TranslationTextComponent("item.ring_of_miner.ring_of_miner.line5").mergeStyle(TextFormatting.YELLOW)));
			tooltip.add((new TranslationTextComponent("item.ring_of_miner.ring_of_miner.line6").mergeStyle(TextFormatting.YELLOW)));
		}
		else
		{
			tooltip.add((new TranslationTextComponent("item.ring_of_miner.ring_of_miner.line2").mergeStyle(TextFormatting.YELLOW)));
			tooltip.add((new TranslationTextComponent("item.ring_of_miner.ring_of_miner.line3").mergeStyle(TextFormatting.YELLOW)));
		}


		tooltip.add((new TranslationTextComponent("item.ring_of_miner.ring_of_miner.line4").mergeStyle(TextFormatting.LIGHT_PURPLE)));
		tooltip.add((new TranslationTextComponent("item.ring_of_miner.ring_of_miner.cooldown",ringMinerCooldown).mergeStyle(TextFormatting.LIGHT_PURPLE)));

		if(delayedBreakMode)
		{
			mode = "Delayed";
			tooltip.add((new TranslationTextComponent("item.ring_of_miner.ring_of_miner.mode", mode).mergeStyle(TextFormatting.LIGHT_PURPLE)));
		}
		else
		{
			mode = "Instant";
			tooltip.add((new TranslationTextComponent("item.ring_of_miner.ring_of_miner.mode", mode).mergeStyle(TextFormatting.LIGHT_PURPLE)));
		}
	}
}
