package com.omegajak.compressedbuilding.creativeTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.omegajak.compressedbuilding.blocks.Blocks;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class CreativeTab extends CreativeTabs {
	
	public static ItemStack stack;
	
	public CreativeTab(String name) {
		super(name);
		LanguageRegistry.instance().addStringLocalization("itemGroup." + name, "en_US", name);
	}
	
	@Override
	public ItemStack getIconItemStack() {
		if (stack == null) {
			stack = new ItemStack(Blocks.squareTemplate);
			stack.setItemDamage(4 << 8);
		}
		return stack;
	}

	@Override
	public Item getTabIconItem() {
		return null;
	}
	
}
