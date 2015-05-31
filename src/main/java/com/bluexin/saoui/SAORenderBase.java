package com.bluexin.saoui;

import com.bluexin.saoui.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class SAORenderBase extends RenderPlayer {

    private static final int HEALTH_COUNT = 32;
    private static final double HEALTH_ANGLE = 0.35F;
    private static final double HEALTH_RANGE = 0.975F;
    private static final float HEALTH_OFFSET = 0.75F;
    private static final float HEALTH_OFFSET_PLAYER = -0.125F;
    private static final double HEALTH_HEIGHT = 0.21F;

    private static final double PIECES_X_OFFSET = 0.02;
    private static final double PIECES_Y_OFFSET = -0.02;
    private static final int PIECES_COUNT = 150;
    private static final double PIECES_SPEED = 1.4;
    private static final double PIECES_GRAVITY = 0.4;

    private final Render parent;

    public SAORenderBase(Render render) {
        super(render.func_177068_d());
        parent = render;
    }

    @Override
	public void func_177138_b(AbstractClientPlayer player) {
        if (parent instanceof RenderPlayer) {
            ((RenderPlayer) parent).func_177138_b(player);
        }
    }

    @Override
	public void func_177139_c(AbstractClientPlayer player) {
        if (parent instanceof RenderPlayer) {
            ((RenderPlayer) parent).func_177139_c(player);
        }
    }

    @Override
    public boolean shouldRender(Entity p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_) {
        return parent.shouldRender(p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_);
    }

    @Override
	public void doRender(Entity entity, double x, double y, double z, float f0, float f1) {
        final Minecraft mc = Minecraft.getMinecraft();

        boolean dead = false, deadStart = false, deadExactly = false;

        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase living = (EntityLivingBase) entity;

            dead = SAOMod.getHealth(mc, living, SAOMod.UNKNOWN_TIME_DELAY) <= 0;
            deadStart = (living.deathTime == 1);
            deadExactly = (living.deathTime >= 18);

            if (deadStart) {
                living.deathTime++;
            }
        } else if (entity instanceof EntityItem) {
            final EntityItem item = (EntityItem) entity;

            deadStart = (item.getAge() + 16 >= item.lifespan);
            deadExactly = (item.getAge() >= item.lifespan);
        }

        parent.doRender(entity, x, y, z, f0, f1);

        if ((entity instanceof EntityLivingBase) && (!dead) && (!entity.isInvisibleToPlayer(mc.thePlayer))) {
            if (SAOOption.COLOR_CURSOR.value) {
                doRenderColorCursor(mc, entity, x, y, z, 64);
            }

            if ((SAOOption.HEALTH_BARS.value) && (!entity.equals(mc.thePlayer))) {
                doRenderHealthBar(mc, entity, x, y, z, f0, f1);
            }
        }

        if (SAOOption.PARTICLES.value) {
            if (deadStart) {
                SAOSound.playAtEntity(entity, SAOSound.PARTICLES_DEATH);
            }

            if (deadExactly) {
                doSpawnDeathParticles(mc, entity);

                entity.setDead();
            }
        }
    }

    @Override
	public void bindTexture(ResourceLocation location) {
        parent.bindTexture(location);
    }

    @Override
	public void doRenderShadowAndFire(Entity p_76979_1_, double p_76979_2_, double p_76979_4_, double p_76979_6_, float p_76979_8_, float p_76979_9_) {
        parent.doRenderShadowAndFire(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_8_, p_76979_9_);
    }

    @Override
	public FontRenderer getFontRendererFromRenderManager() {
        return parent.getFontRendererFromRenderManager();
    }

    @Override
	public RenderManager func_177068_d() {
        return parent.func_177068_d();
    }

    private void doRenderColorCursor(Minecraft mc, Entity entity, double x, double y, double z, int distance) {
        double d3 = entity.getDistanceSqToEntity(renderManager.livingPlayer);

        if (d3 <= (double) (distance * distance)) {
            float f = 1.6F;
            float f1 = 0.016666668F * f;

            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + entity.height + 1.1F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();

            SAOGL.glDepthTest(true);

            SAOGL.glBlend(true);
            SAOGL.tryBlendFuncSeparate(770, 771, 1, 0);

            SAOGL.glBindTexture(SAOResources.entities);

            Tessellator tessellator = Tessellator.getInstance();

            tessellator.getWorldRenderer().startDrawingQuads();

            SAOColorState.getColorState(mc, entity, SAOMod.UNKNOWN_TIME_DELAY).glColor();

            tessellator.getWorldRenderer().addVertexWithUV(-9, -1, 0.0D, 0F, 0.25F);
            tessellator.getWorldRenderer().addVertexWithUV(-9, 17, 0.0D, 0F, 0.375F);
            tessellator.getWorldRenderer().addVertexWithUV(9, 17, 0.0D, 0.125F, 0.375F);
            tessellator.getWorldRenderer().addVertexWithUV(9, -1, 0.0D, 0.125F, 0.25F);
            tessellator.draw();

            SAOGL.glBlend(false);
            SAOGL.glColor(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    private void doRenderHealthBar(Minecraft mc, Entity entity, double x, double y, double z, float f0, float f1) {
        SAOGL.glBindTexture(SAOResources.entities);

        Tessellator tessellator = Tessellator.getInstance();

        SAOGL.glDepthTest(true);
        SAOGL.glCullFace(false);
        SAOGL.glBlend(true);

        SAOGL.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        final int hitPoints = (int) (getHealthFactor(mc, entity, SAOMod.UNKNOWN_TIME_DELAY) * HEALTH_COUNT);
        useColor(mc, entity, SAOMod.UNKNOWN_TIME_DELAY);

        tessellator.getWorldRenderer().startDrawing(GL11.GL_TRIANGLE_STRIP);

        for (int i = 0; i <= hitPoints; i++) {
            final double value = (double) (i + HEALTH_COUNT - hitPoints) / HEALTH_COUNT;
            final double rad = Math.toRadians(renderManager.playerViewY - 135) + (value - 0.5) * Math.PI * HEALTH_ANGLE;

            final double x0 = x + entity.width * HEALTH_RANGE * Math.cos(rad);
            final double y0 = y + entity.height * HEALTH_OFFSET;
            final double z0 = z + entity.width * HEALTH_RANGE * Math.sin(rad);

            final double uv_value = value - (double) (HEALTH_COUNT - hitPoints) / HEALTH_COUNT;

            tessellator.getWorldRenderer().addVertexWithUV(x0, y0 + HEALTH_HEIGHT, z0, (1.0 - uv_value), 0);
            tessellator.getWorldRenderer().addVertexWithUV(x0, y0, z0, (1.0 - uv_value), 0.125);
        }

        tessellator.draw();

        SAOGL.glColor(1, 1, 1, 1);
        tessellator.getWorldRenderer().startDrawing(GL11.GL_TRIANGLE_STRIP);

        for (int i = 0; i <= HEALTH_COUNT; i++) {
            final double value = (double) i / HEALTH_COUNT;
            final double rad = Math.toRadians(renderManager.playerViewY - 135) + (value - 0.5) * Math.PI * HEALTH_ANGLE;

            final double x0 = x + entity.width * HEALTH_RANGE * Math.cos(rad);
            final double y0 = y + entity.height * HEALTH_OFFSET;
            final double z0 = z + entity.width * HEALTH_RANGE * Math.sin(rad);

            tessellator.getWorldRenderer().addVertexWithUV(x0, y0 + HEALTH_HEIGHT, z0, (1.0 - value), 0.125);
            tessellator.getWorldRenderer().addVertexWithUV(x0, y0, z0, (1.0 - value), 0.25);
        }

        tessellator.draw();

        SAOGL.glCullFace(true);
    }

    private void doSpawnDeathParticles(Minecraft mc, Entity entity) {
        final World world = entity.worldObj;

        if (world != null) {
            final float[][] colors = {
                    {1F / 0xFF * 0x9A, 1F / 0xFF * 0xFE, 1F / 0xFF * 0x2E},
                    {1F / 0xFF * 0x01, 1F / 0xFF * 0xFF, 1F / 0xFF * 0xFF},
                    {1F / 0xFF * 0x08, 1F / 0xFF * 0x08, 1F / 0xFF * 0x8A}
            };

            final float size = entity.width * entity.height;
            final int pieces = (int) Math.max(Math.min((size * 64), 128), 8);

            for (int i = 0; i < pieces; i++) {
                final float[] color = colors[i % 3];

                final double x0 = entity.width * (Math.random() * 2 - 1) * 0.75;
                final double y0 = entity.height * (Math.random());
                final double z0 = entity.width * (Math.random() * 2 - 1) * 0.75;

                mc.effectRenderer.addEffect(new SAOEntityPiecesFX(
                        world,
                        entity.posX + x0, entity.posY + y0, entity.posZ + z0,
                        color[0], color[1], color[2]
                ));
            }
        }
    }

    private void useColor(Minecraft mc, Entity entity, float time) {
        if (entity instanceof EntityLivingBase) {
            SAOHealthStep.getStep(mc, (EntityLivingBase) entity, time).glColor((EntityLivingBase) entity);
        } else {
            SAOHealthStep.GOOD.glColor();
        }
    }

    private float getHealthFactor(Minecraft mc, Entity entity, float time) {
        final float normalFactor = SAOMod.getHealth(mc, entity, time) / SAOMod.getMaxHealth(entity);
        final float delta = 1.0F - normalFactor;

        return normalFactor + (delta * delta / 2) * normalFactor;
    }

}
