package warhammermod.Items.melee.specials;


import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import warhammermod.Items.melee.HammerTemplate;
import warhammermod.utils.reference;

import java.util.List;

public class Ghal_Maraz extends HammerTemplate {
    public Ghal_Maraz(Item.Properties builder){
        super(Tiers.NETHERITE,builder.tab(reference.warhammer),2.5F,-2.4F);
        FabricModelPredicateProviderRegistry.register(this, new ResourceLocation("powerhit"), (stack, clientWorld, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : entity.getTicksUsingItem() <20? 0.0F : 1.0F;
            }
        });
    }


    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
        updated=8;
        Player player = (Player) entityLiving;
        applyeffects(stack,player,world);
        boolean hitsomething = false;
        for (LivingEntity targets : world.getEntitiesOfClass(LivingEntity.class, entityLiving.getBoundingBox().inflate(3D, 0.25D, 3D)))
        {
            if (targets != player && entityLiving.distanceToSqr(targets) < 9.0D)
            {
                float f = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                Vec2 vec = calculatevector(player,targets);
                targets.knockback(2.5F + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, stack), (double)  vec.x,vec.y);
                targets.hurt(DamageSource.playerAttack(player), f);
                hitsomething=true;
            }
        }
        stack.hurtAndBreak(1, player, (p_220009_1_) -> {
            p_220009_1_.broadcastBreakEvent(player.getUsedItemHand());
        });
        if(hitsomething) {
            world.playSound( null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 0.5F, 1.0F);
        }
    }
    int updated=-1;

    public boolean applyeffects(ItemStack stack, Player player,Level world) {

        if (!world.isClientSide() && !player.getCooldowns().isOnCooldown(stack.getItem()))
        {
            if (player.level.random.nextFloat() < 0.8F)
            {
                if (player.level.random.nextFloat() < 0.4F)
                {
                    if (player.level.random.nextFloat() < 0.3F)
                    {
                        handleeffects(player,world,true);
                        applypotioneffect(world,player,2);
                        return true;
                    }
                    handleeffects(player,world,true);
                    applypotioneffect(world,player,1);
                    return true;
                }
                handleeffects(player,world,true);
                applypotioneffect(world,player,0);
                return true;
            }
            handleeffects(player,world,false);
            return false;
        }
        return false;
    }

    public  void handleeffects(Player player,Level world,Boolean succes){
        if(succes) {
            player.sendMessage(new TextComponent("item.ghal_maraz.succes"), Util.NIL_UUID);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, player.getSoundSource(), 0.5F, 1.6F);
        }
        else {
            player.sendMessage(new TextComponent("item.ghal_maraz.fail"),  Util.NIL_UUID);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_TOAST_OUT, player.getSoundSource(), 0.5F, 1.5F);
        }
    }
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if(level.isClientSide && entity instanceof  Player){
            if(updated==0) {
                updated=-1;
                Player player = (Player) entity;
                if (haseffects(player)) {
                    Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
                } else
                    Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.EFFECT, 30);
            }else if(updated>0) updated--;
        }
        super.inventoryTick(itemStack,level,entity,i,bl);
    }

    public void applypotioneffect(Level wprmd,Player player,int level){
        if(level ==0){
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,150,0));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,300,0));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,300,0));
            player.getCooldowns().addCooldown(this, 600);
        }
        else if(level ==1){
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,200,1));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,400,1));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,300,1));
            player.getCooldowns().addCooldown(this, 600);
        }
        else if(level ==2){
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,300,1));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,500,1));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,350,1));
            player.getCooldowns().addCooldown(this, 600);
        }
    }



    public void appendHoverText(ItemStack p_77624_1_, Level p_77624_2_, List<Component> p_77624_3_, TooltipFlag p_77624_4_) {

        p_77624_3_.add((new TextComponent("The Legendary Warhammer, use wisely")));

    }

    private Boolean haseffects(Player player){
        return player.hasEffect(MobEffects.REGENERATION) && player.hasEffect(MobEffects.DAMAGE_RESISTANCE) && player.hasEffect(MobEffects.DAMAGE_BOOST);
    }

    public int getUseDuration(ItemStack stack) {
        return 1000;
    }

    private Vec2 calculatevector(Entity e1, Entity e2){
        float x = (float)(e1.position().x - e2.position().x);
        float z = (float)(e1.position().z - e2.position().z);
        float r = (float)(Math.sqrt(Math.pow(x,2)+Math.pow(z,2)));
        return new Vec2(x/r,z/r);
    }


    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStack2) {
        return false;
    }
}
