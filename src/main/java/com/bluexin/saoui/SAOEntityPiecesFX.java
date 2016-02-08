package com.bluexin.saoui;

import com.bluexin.saoui.util.SAOResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayDeque;
import java.util.Queue;


@SideOnly(Side.CLIENT)
public class SAOEntityPiecesFX extends EntityFX {

    public static Queue<SAOEntityPiecesFX> queuedRenders = new ArrayDeque<>();

    float ParticleScale;
    float time;
    float particleX;
    float particleY;
    float particleZ;
    float f0;
    float f1;

    public SAOEntityPiecesFX(World world, double xCoord, double yCoord, double zCoord, float redValue, float greenValue, float blueValue) {
        this(world, xCoord, yCoord, zCoord, redValue, greenValue, blueValue, 1.0F);
    }

    private SAOEntityPiecesFX(World world, double xCoord, double yCoord, double zCoord, float redValue, float greenValue, float blueVale, float scale) {
        super(world, xCoord, yCoord, zCoord, 0.0D, 0.0D, 0.0D);
        this.motionX = (double) ((float) (Math.random() * 2.0D - 1.0D) * 0.05F);
        this.motionY = (double) ((float) (Math.random() * 2.0D - 1.0D) * 0.05F);
        this.motionZ = (double) ((float) (Math.random() * 2.0D - 1.0D) * 0.05F);
        this.particleRed = redValue;
        this.particleGreen = greenValue;
        this.particleBlue = blueVale;
        this.particleScale *= scale;
        this.ParticleScale = this.particleScale;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int) ((float) this.particleMaxAge * scale);
        this.noClip = false;
    }

    public static void dispatchQueuedRenders(Tessellator tessellator) {
        SAORenderDispatcher.particleFxCount = 0;

        Minecraft.getMinecraft().renderEngine.bindTexture(SAOResources.particleLarge);

        tessellator.getWorldRenderer().startDrawingQuads();
        for (SAOEntityPiecesFX particle : queuedRenders)
            particle.renderQueued(tessellator);
        tessellator.draw();
        GlStateManager.disableBlend();

        queuedRenders.clear();
    }

    @Override
    public void renderParticle(WorldRenderer renderer, Entity e, float time, float x, float y, float z, float f0, float f1) {
        this.time = time;
        this.particleX = x;
        this.particleY = y;
        this.particleZ = z;
        this.f0 = f0;
        this.f1 = f1;

        queuedRenders.add(this);
    }

    private void renderQueued(Tessellator tessellator) {
        float particle = ((float) this.particleAge + time) / (float) this.particleMaxAge * 32.0F;

        if (particle < 0.0F) {
            particle = 0.0F;
        }

        if (particle > 1.0F) {
            particle = 1.0F;
        }

        this.particleScale = this.ParticleScale * particle;
        float scale = 0.1F * this.particleScale;
        float xPos = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) time - interpPosX);
        float yPos = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) time - interpPosY);
        float zPos = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) time - interpPosZ);
        float colorIntensity = 1.0F;

        tessellator.getWorldRenderer().setColorOpaque_F(this.particleRed * colorIntensity, this.particleGreen * colorIntensity, this.particleBlue * colorIntensity);//, 1.0F);

        tessellator.getWorldRenderer().addVertexWithUV((double) (xPos - this.particleX * scale - f0 * scale), (double) (yPos - this.particleY * scale), (double) (zPos - this.particleZ * scale - f1 * scale), 0D, 1D);
        tessellator.getWorldRenderer().addVertexWithUV((double) (xPos - this.particleX * scale + f0 * scale), (double) (yPos + this.particleY * scale), (double) (zPos - this.particleZ * scale + f1 * scale), 1D, 1D);
        tessellator.getWorldRenderer().addVertexWithUV((double) (xPos + this.particleX * scale + f0 * scale), (double) (yPos + this.particleY * scale), (double) (zPos + this.particleZ * scale + f1 * scale), 1D, 0D);
        tessellator.getWorldRenderer().addVertexWithUV((double) (xPos + this.particleX * scale - f0 * scale), (double) (yPos - this.particleY * scale), (double) (zPos + this.particleZ * scale - f1 * scale), 0D, 0D);
    }

    @Override
    public void setParticleIcon(TextureAtlasSprite icon) {
    }

    /**
     * Called to update the entity's position/logic.
     */

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }

        this.motionY += 0.004D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.8999999761581421D;
        this.motionY *= 0.8999999761581421D;
        this.motionZ *= 0.8999999761581421D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

}
