package com.omegajak.compressedbuilding.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.omegajak.compressedbuilding.lib.BlockInfo;

public class ConfigHandler {
	public static void init(File file) {
		Configuration config = new Configuration(file);
		
		config.load();
		
		BlockInfo.SQTEMPLATE_ID = config.get(BlockInfo.BLOCK_CATEGORY, BlockInfo.SQTEMPLATE_KEY, BlockInfo.SQTEMPLATE_DEFAULT).getInt();
		BlockInfo.COMPACTOR_ID = config.get(BlockInfo.BLOCK_CATEGORY, BlockInfo.COMPACTOR_KEY, BlockInfo.COMPACTOR_DEFAULT).getInt();
		
//		ItemInfo.WAND_ID = config.getItem(ItemInfo.WAND_KEY, ItemInfo.WAND_DEFAULT).getInt() - 256;
		
		config.save();
	}
}
