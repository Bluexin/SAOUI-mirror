package com.bluexin.saoui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class SAOEventHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean isPlaying = false;

    @SubscribeEvent
    public void livingAttack(LivingAttackEvent e) {
        this.livingHit(e.entityLiving, e.source.getEntity());
    }

    @SubscribeEvent
    public void livingHurt(LivingHurtEvent e) {
        this.livingHit(e.entityLiving, e.source.getEntity());
    }

    private void livingHit(EntityLivingBase target, Entity source) {
        if (target instanceof EntityPlayer && source instanceof EntityPlayer) {
            if (target.getHealth() <= 0) {
                SAOMod.onKillPlayer((EntityPlayer) source);
            } else {
                SAOMod.onDamagePlayer((EntityPlayer) source);
            }
        }
    }

    @SubscribeEvent
    public void livingDeath(LivingDeathEvent e) {
        if (e.entityLiving instanceof EntityPlayer && e.source.getEntity() instanceof EntityPlayer)
            SAOMod.onKillPlayer((EntityPlayer) e.source.getEntity());
    }

    @SubscribeEvent
    public void livingDrop(LivingDropsEvent e) {
        if (e.entityLiving instanceof EntityPlayer && e.source.getEntity() instanceof EntityPlayer)
            SAOMod.onKillPlayer((EntityPlayer) e.source.getEntity());
    }

    @SubscribeEvent
    public void playerAttackEntity(AttackEntityEvent e) {
        if (e.target instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) e.target;

            if (player.getHealth() <= 0) SAOMod.onKillPlayer(e.entityPlayer);
            else SAOMod.onDamagePlayer(e.entityPlayer);
        }
    }

    @SubscribeEvent
    public void playerDrops(PlayerDropsEvent e) {
        if (e.source.getEntity() instanceof EntityPlayer) SAOMod.onKillPlayer((EntityPlayer) e.source.getEntity());
    }

    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent e) {
        if (!SAOMod.verChecked && e.entity.worldObj.isRemote && e.entity instanceof EntityPlayer) {
            VersionChecker vc = new VersionChecker((EntityPlayer) e.entity);
            vc.run();
        }
    }

    @SubscribeEvent
    public void colorstateupdate(LivingUpdateEvent e) {
        long time = System.currentTimeMillis();
        long lasttime = time;

        long delay;

        time = System.currentTimeMillis();
        delay = Math.abs(time - lasttime);
        lasttime = time;
        if (e.entityLiving != null) SAOMod.colorStates.values().stream().forEach(cursor -> cursor.update(delay));
    }

    @SubscribeEvent
    public void abilityCheck(ClientTickEvent e) {
        if (mc.thePlayer == null) {
            SAOMod.IS_SPRINTING = false;
            SAOMod.IS_SNEAKING = false;
        } else if (mc.inGameHasFocus) {
            if (SAOMod.IS_SPRINTING) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            if (SAOMod.IS_SNEAKING) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
    }

    @SubscribeEvent
    public void chatEvent2(ClientChatReceivedEvent evt) {
        System.out.println("Got a ClientChatReceivedEvent containing " + evt.message.getFormattedText() + " as type " + evt.type + ".\nEquals ♠? " + evt.message.getFormattedText().equals("♠"));
        // TODO: check other ones too
        // evt.message.getFormattedText();evt.message.getUnformattedText();evt.message.getUnformattedTextForChat();
    }

    /*
    @SubscribeEvent
    public void lowHealth(TickEvent.PlayerTickEvent e)
    {
    	isPlaying = SAOSound.isSfxPlaying(SAOSound.LOW_HEALTH);
    	if (!isPlaying){
    		isPlaying = true;
    		if (!(e.player.getHealth() <= 0)){
    			if (e.player.getHealth() <= e.player.getMaxHealth() * 0.3F && !e.player.isDead){
    				SAOSound.play(mc, SAOSound.LOW_HEALTH);
    			}
    		}
    	}
    	
    }*/
    
}
