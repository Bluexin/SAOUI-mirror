package com.bluexin.saoui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * Part of SAOUI
 *
 * @author Bluexin
 */
public class SAORenderPlayer extends RenderPlayer {

    private SAORenderBase trueRenderer;

    public SAORenderPlayer(Render render) {
        super(render.func_177068_d());
        this.trueRenderer = new SAORenderBase(render);
    }

    @Override
    public boolean shouldRender(Entity p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_) {
        return this.trueRenderer.shouldRender(p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f0, float f1) {
        this.trueRenderer.doRender(entity, x, y, z, f0, f1);
    }

    @Override
    public void bindTexture(ResourceLocation location) {
        this.trueRenderer.bindTexture(location);
    }

    @Override
    public void doRenderShadowAndFire(Entity p_76979_1_, double p_76979_2_, double p_76979_4_, double p_76979_6_, float p_76979_8_, float p_76979_9_) {
        this.trueRenderer.doRenderShadowAndFire(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_8_, p_76979_9_);
    }

    @Override
    public FontRenderer getFontRendererFromRenderManager() {
        return this.trueRenderer.getFontRendererFromRenderManager();
    }

    @Override
    public RenderManager func_177068_d() {
        return this.trueRenderer.func_177068_d();
    }

    @Override
    public void renderName(Entity entity, double x, double y, double z) {
        if (entity instanceof EntityLivingBase) trueRenderer.renderName(entity, x, y, z);
    }
}
