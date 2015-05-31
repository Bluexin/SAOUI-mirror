package com.bluexin.saoui;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class SAOEntityPiecesFX extends EntityFX {

    private float smokeParticleScale;
    private static final String __OBFID = "CL_00000924";

    public SAOEntityPiecesFX(World p_i1225_1_, double p_i1225_2_, double p_i1225_4_, double p_i1225_6_, float p_i1225_8_, float p_i1225_10_, float p_i1225_12_) {
        this(p_i1225_1_, p_i1225_2_, p_i1225_4_, p_i1225_6_, p_i1225_8_, p_i1225_10_, p_i1225_12_, 1.0F);
    }

    private SAOEntityPiecesFX(World p_i1226_1_, double p_i1226_2_, double p_i1226_4_, double p_i1226_6_, float p_i1226_8_, float p_i1226_10_, float p_i1226_12_, float p_i1226_14_) {
        super(p_i1226_1_, p_i1226_2_, p_i1226_4_, p_i1226_6_, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.particleRed = p_i1226_8_;
        this.particleGreen = p_i1226_10_;
        this.particleBlue = p_i1226_12_;
        this.particleScale *= 0.75F;
        this.particleScale *= p_i1226_14_;
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int) ((float) this.particleMaxAge * p_i1226_14_);
        this.noClip = false;
    }

    @Override
    public void func_180434_a(WorldRenderer renderer, Entity e, float time, float x, float y, float z, float f0, float f1) {
        float particle = ((float) this.particleAge + time) / (float) this.particleMaxAge * 32.0F;

        if (particle < 0.0F) {
            particle = 0.0F;
        }

        if (particle > 1.0F) {
            particle = 1.0F;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

        this.particleScale = this.smokeParticleScale * particle;

        super.func_180434_a(renderer, e, time, x, y, z, f0, f1);
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

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.motionY -= 0.002D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

}
