package com.omegajak.compressedbuilding.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class RenderCompactor extends TileEntitySpecialRenderer {
	
	private IModelCustom model;
	
	public RenderCompactor(IModelCustom model) {
		this.model = model;
	}
	
	public static final ResourceLocation texture = new ResourceLocation("compressedbuilding", "textures/models/Compactor.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		model.renderAll();
		
		GL11.glPopMatrix();
	}
}
