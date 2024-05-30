package warhammermod.Items.melee.specials;


import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import net.minecraft.world.item.*;
import warhammermod.Items.melee.HammerTemplate;
import warhammermod.utils.reference;

import java.util.List;

public class Ghal_Maraz extends HammerTemplate {
    public Ghal_Maraz(Item.Settings builder){
        super(ToolMaterials.NETHERITE,builder.tab(reference.warhammer),2.5F,-2.4F);
        FabricModelPredicateProviderRegistry.register(this, new Identifier("powerhit"), (stack, clientWorld, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : entity.getItemUseTime() <20? 0.0F : 1.0F;
            }
        });
    }


    public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);
        player.setCurrentHand(hand);
        return TypedActionResult.consume(itemstack);
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
        updated=8;
        PlayerEntity player = (PlayerEntity) entityLiving;
        applyeffects(stack,player,world);
        boolean hitsomething = false;
        for (LivingEntity targets : world.getNonSpectatingEntities(LivingEntity.class, entityLiving.getBoundingBox().expand(3D, 0.25D, 3D)))
        {
            if (targets != player && entityLiving.squaredDistanceTo(targets) < 9.0D)
            {
                float f = (float)player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                Vec2f vec = calculatevector(player,targets);
                targets.takeKnockback(2.5F + EnchantmentHelper.getLevel(Enchantments.KNOCKBACK, stack), (double)  vec.x,vec.y);
                targets.damage(DamageSource.playerAttack(player), f);
                hitsomething=true;
            }
        }
        stack.damage(1, player, (p_220009_1_) -> {
            p_220009_1_.sendToolBreakStatus(player.getActiveHand());
        });
        if(hitsomething) {
            world.playSound( null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 0.5F, 1.0F);
        }
    }
    int updated=-1;

    public boolean applyeffects(ItemStack stack, PlayerEntity player,World world) {

        if (!world.isClient() && !player.getItemCooldownManager().isCoolingDown(stack.getItem()))
        {
            if (player.world.random.nextFloat() < 0.8F)
            {
                if (player.world.random.nextFloat() < 0.4F)
                {
                    if (player.world.random.nextFloat() < 0.3F)
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

    public  void handleeffects(PlayerEntity player,World world,Boolean succes){
        if(succes) {
            player.sendMessage(new TranslatableTextContent("item.ghal_maraz.succes"), Util.NIL_UUID);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, player.getSoundCategory(), 0.5F, 1.6F);
        }
        else {
            player.sendMessage(new TranslatableTextContent("item.ghal_maraz.fail"),  Util.NIL_UUID);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_TOAST_OUT, player.getSoundCategory(), 0.5F, 1.5F);
        }
    }
    public void inventoryTick(ItemStack itemStack, World level, Entity entity, int i, boolean bl) {
        if(level.isClient && entity instanceof  PlayerEntity){
            if(updated==0) {
                updated=-1;
                PlayerEntity player = (PlayerEntity) entity;
                if (haseffects(player)) {
                    MinecraftClient.getInstance().particleManager.addEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
                } else
                    MinecraftClient.getInstance().particleManager.addEmitter(entity, ParticleTypes.EFFECT, 30);
            }else if(updated>0) updated--;
        }
        super.inventoryTick(itemStack,level,entity,i,bl);
    }

    public void applypotioneffect(World wprmd,PlayerEntity player,int level){
        if(level ==0){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,150,0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,300,0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,300,0));
            player.getItemCooldownManager().set(this, 600);
        }
        else if(level ==1){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,200,1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,400,1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,300,1));
            player.getItemCooldownManager().set(this, 600);
        }
        else if(level ==2){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,300,1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,500,1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,350,1));
            player.getItemCooldownManager().set(this, 600);
        }
    }



    public void appendTooltip(ItemStack p_77624_1_, World p_77624_2_, List<Text> p_77624_3_, TooltipContext p_77624_4_) {

        p_77624_3_.add((new LiteralTextContent("The Legendary Warhammer, use wisely")));

    }

    private Boolean haseffects(PlayerEntity player){
        return player.hasStatusEffect(StatusEffects.REGENERATION) && player.hasStatusEffect(StatusEffects.RESISTANCE) && player.hasStatusEffect(StatusEffects.STRENGTH);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 1000;
    }

    private Vec2f calculatevector(Entity e1, Entity e2){
        float x = (float)(e1.getPos().x - e2.getPos().x);
        float z = (float)(e1.getPos().z - e2.getPos().z);
        float r = (float)(Math.sqrt(Math.pow(x,2)+Math.pow(z,2)));
        return new Vec2f(x/r,z/r);
    }


    public UseAction getUseAction(ItemStack itemStack) {
        return UseAction.BOW;
    }

    public boolean canRepair(ItemStack itemStack, ItemStack itemStack2) {
        return false;
    }
}
