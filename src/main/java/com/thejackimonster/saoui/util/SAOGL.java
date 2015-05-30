package com.thejackimonster.saoui.util;

import com.thejackimonster.saoui.SAOMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class SAOGL {

	private SAOGL() {}

	public static final Minecraft glMinecraft() {
		return Minecraft.getMinecraft();
	}

	public static final FontRenderer glFont() {
		final Minecraft mc = glMinecraft();
		
		if (mc != null) {
			return mc.fontRendererObj;
		} else {
			return null;
		}
	}

	public static final TextureManager glTextureManager() {
		final Minecraft mc = glMinecraft();
		
		if (mc != null) {
			return mc.getTextureManager();
		} else {
			return null;
		}
	}

	public static final void glColor(float red, float green, float blue, float alpha) {
		 GlStateManager.color(red, green, blue, alpha);
	}

	public static final void glColorRGBA(int rgba) {
		final float red = (float) ((rgba >> 24) & 0xFF) / 0xFF;
		final float green = (float) ((rgba >> 16) & 0xFF) / 0xFF;
		final float blue = (float) ((rgba >> 8) & 0xFF) / 0xFF;
		final float alpha = (float) ((rgba >> 0) & 0xFF) / 0xFF;
		
		glColor(red, green, blue, alpha);
	}

	public static final int glFontColor(int rgba) {
		final int alpha = (rgba >> 0) & 0xFF;
		final int red = (rgba >> 24) & 0xFF;
		final int blue = (rgba >> 8) & 0xFF;
		final int green = (rgba >> 16) & 0xFF;
		
		return (alpha << 24) | (red << 16) | (blue << 8) | (green << 0);
	}

	public static final void glString(FontRenderer font, String string, int x, int y, int argb, boolean shadow) {
		if (font != null) {
			font.drawString(string, x, y, glFontColor(argb), shadow);
		}
	}

	public static final void glString(FontRenderer font, String string, int x, int y, int argb) {
		glString(font, string, x, y, argb, false);
	}

	public static final void glString(String string, int x, int y, int argb, boolean shadow) {
		glString(glFont(), string, x, y, argb, shadow);
	}

	public static final void glString(String string, int x, int y, int argb) {
		glString(string, x, y, argb, false);
	}

	public static final int glStringWidth(FontRenderer font, String string) {
		if (font != null) {
			return font.getStringWidth(string);
		} else {
			return 0;
		}
	}

	public static final int glStringWidth(String string) {
		return glStringWidth(glFont(), string);
	}

	public static final int glStringHeight(FontRenderer font) {
		if (font != null) {
			return font.FONT_HEIGHT;
		} else {
			return 0;
		}
	}

	public static final int glStringHeight() {
		return glStringHeight(glFont());
	}

	public static final void glBindTexture(TextureManager textureManager, ResourceLocation location) {
		if (textureManager != null) {
			textureManager.bindTexture(location);
		}
	}

	public static final void glBindTexture(ResourceLocation location) {
		glBindTexture(glTextureManager(), location);
	}

	public static final void glTexturedRect(int x, int y, float z, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
		float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads();
        tessellator.getWorldRenderer().addVertexWithUV((double)(x + 0), (double)(y + height), (double)z, (double)((float)(srcX + 0) * f), (double)((float)(srcY + srcHeight) * f1));
        tessellator.getWorldRenderer().addVertexWithUV((double)(x + width), (double)(y + height), (double)z, (double)((float)(srcX + srcWidth) * f), (double)((float)(srcY + srcHeight) * f1));
        tessellator.getWorldRenderer().addVertexWithUV((double)(x + width), (double)(y + 0), (double)z, (double)((float)(srcX + srcWidth) * f), (double)((float)(srcY + 0) * f1));
        tessellator.getWorldRenderer().addVertexWithUV((double)(x + 0), (double)(y + 0), (double)z, (double)((float)(srcX + 0) * f), (double)((float)(srcY + 0) * f1));
        tessellator.draw();
	}

	public static final void glTexturedRect(int x, int y, float z, int srcX, int srcY, int width, int height) {
		glTexturedRect(x, y, z, width, height, srcX, srcY, width, height);
	}

	public static final void glTexturedRect(int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
		glTexturedRect(x, y, 0, width, height, srcX, srcY, srcWidth, srcHeight);
	}

	public static final void glTexturedRect(int x, int y, int srcX, int srcY, int width, int height) {
		glTexturedRect(x, y, 0, srcX, srcY, width, height);
	}

	public static final void glRect(int x, int y, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads();
        tessellator.getWorldRenderer().addVertex((double)(x + 0), (double)(y + height), 0.0D);
        tessellator.getWorldRenderer().addVertex((double)(x + width), (double)(y + height), 0.0D);
        tessellator.getWorldRenderer().addVertex((double)(x + width), (double)(y + 0), 0.0D);
        tessellator.getWorldRenderer().addVertex((double)(x + 0), (double)(y + 0), 0.0D);
        tessellator.draw();
	}

	public static final void glAlpha(boolean flag) {
		if (flag) {
			GlStateManager.enableAlpha();
		} else {
			GlStateManager.disableAlpha();
		}
	}

	public static final void alphaFunc(int src, int dst) {
		GlStateManager.alphaFunc(src, dst);
	}

	public static final void glBlend(boolean flag) {
		if (flag) {
			GlStateManager.enableBlend();
		} else {
			GlStateManager.disableBlend();
		}
	}

	public static final void blendFunc(int src, int dst) {
		GlStateManager.blendFunc(src, dst);
	}

	public static final void tryBlendFuncSeparate(int a, int b, int c, int d) {
		GlStateManager.tryBlendFuncSeparate(a, b, c, d);
	}

	public static final void glDepth(boolean flag) {
		if (flag) {
			GlStateManager.enableDepth();
		} else {
			GlStateManager.disableDepth();
		}
	}

	public static final void depthMask(boolean flag) {
		GlStateManager.depthMask(flag);
	}

	public static final void glDepthTest(boolean flag) {
		if (flag) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		} else {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		}
	}

	public static final void glRescaleNormal(boolean flag) {
		if (flag) {
			GlStateManager.enableRescaleNormal();
		} else {
			GlStateManager.disableRescaleNormal();
		}
	}

	public static final void glTexture2D(boolean flag) {
		if (flag) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		} else {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}

	public static final void glCullFace(boolean flag) {
		if (flag) {
			GlStateManager.enableCull();
		} else {
			GlStateManager.disableCull();
		}
	}

	public static final void glStartUI(Minecraft mc) {
		mc.mcProfiler.startSection(SAOMod.MODID + "[ '" + SAOMod.NAME + "' ]");
	}

	public static final void glEndUI(Minecraft mc) {
		mc.mcProfiler.endSection();
	}

}
