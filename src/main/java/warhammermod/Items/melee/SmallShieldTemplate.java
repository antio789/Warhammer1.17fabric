package warhammermod.Items.melee;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import warhammermod.utils.reference;

import java.util.UUID;

public class SmallShieldTemplate extends ShieldItem {
    private Boolean issprinting=false;

    private static final UUID BLOCKING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3B-4F1C-8813-96EA6097278D");
    private static final AttributeModifier BLOCKING_SPEED_BOOST = new AttributeModifier(BLOCKING_SPEED_BOOST_ID, "blocking speed boost", 2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final UUID SPRINT_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3C-4F1D-8814-96EA6197278D");
    private static final AttributeModifier SPRINT_SPEED_BOOST = (new AttributeModifier(SPRINT_SPEED_BOOST_ID, "sprint speed boost", 3.5D, AttributeModifier.Operation.MULTIPLY_TOTAL));


    public SmallShieldTemplate(Properties builder){
        super(builder.durability(1008).tab(reference.warhammer));
        FabricModelPredicateProviderRegistry.register(this, new ResourceLocation("blocking"), (p_239421_0_, p_239421_1_, p_239421_2_,i) -> {
            return p_239421_2_ != null && p_239421_2_.isUsingItem() && p_239421_2_.getUseItem() == p_239421_0_ ? 1.0F : 0.0F;
        });
    }

    private Boolean needupdate = false;
    public void onUseTick(Level world, LivingEntity player, ItemStack stack, int count) {
        if(player instanceof Player && world.isClientSide()) {
            /*
            issprinting = Minecraft.getInstance().options.keySprint.isDown();
            AttributeInstance modifiableattributeinstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (modifiableattributeinstance.getModifier(BLOCKING_SPEED_BOOST_ID) != null) {
                modifiableattributeinstance.removeModifier(BLOCKING_SPEED_BOOST);
            }
            if (modifiableattributeinstance.getModifier(SPRINT_SPEED_BOOST_ID) != null) {
                modifiableattributeinstance.removeModifier(SPRINT_SPEED_BOOST);
            }
            if (issprinting) {
                modifiableattributeinstance.addPermanentModifier(SPRINT_SPEED_BOOST);
            }

            else {modifiableattributeinstance.addPermanentModifier(BLOCKING_SPEED_BOOST);
            }
            */
            needupdate=true;
        }
    }

    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft)  {
        if(entityLiving instanceof Player && entityLiving.getAttribute(Attributes.MOVEMENT_SPEED) !=null) {
            AttributeInstance modifiableattributeinstance = entityLiving.getAttribute(Attributes.MOVEMENT_SPEED);
            if (modifiableattributeinstance.getModifier(BLOCKING_SPEED_BOOST_ID) != null) {
                modifiableattributeinstance.removeModifier(BLOCKING_SPEED_BOOST);
            }
            if (modifiableattributeinstance.getModifier(SPRINT_SPEED_BOOST_ID) != null) {
                modifiableattributeinstance.removeModifier(SPRINT_SPEED_BOOST);
            }

        }

    }
/*
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof Player && worldIn.isClientSide()){
            LivingEntity player = (LivingEntity) entityIn;

            if(player.getUseItem() != stack && player.getItemInHand(offhand) != stack && player.getAttribute(Attributes.MOVEMENT_SPEED) !=null) {

                AttributeInstance modifiableattributeinstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (modifiableattributeinstance.getModifier(BLOCKING_SPEED_BOOST_ID) != null) {

                    modifiableattributeinstance.removeModifier(BLOCKING_SPEED_BOOST);
                }
                if (modifiableattributeinstance.getModifier(SPRINT_SPEED_BOOST_ID) != null) {

                    modifiableattributeinstance.removeModifier(SPRINT_SPEED_BOOST);
                }
            }

        }
    }*/
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof Player && worldIn.isClientSide() ){
            LivingEntity player = (LivingEntity) entityIn;
            if(needupdate){
                issprinting = Minecraft.getInstance().options.keySprint.isDown();
                AttributeInstance modifiableattributeinstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (modifiableattributeinstance.getModifier(BLOCKING_SPEED_BOOST_ID) != null) {
                    modifiableattributeinstance.removeModifier(BLOCKING_SPEED_BOOST);
                }
                if (modifiableattributeinstance.getModifier(SPRINT_SPEED_BOOST_ID) != null) {
                    modifiableattributeinstance.removeModifier(SPRINT_SPEED_BOOST);
                }
                if (issprinting) {
                    modifiableattributeinstance.addPermanentModifier(SPRINT_SPEED_BOOST);
                }

                else {modifiableattributeinstance.addPermanentModifier(BLOCKING_SPEED_BOOST);
                }
                needupdate = false;
            }
            else if(player.getUseItem() != stack && !player.isUsingItem() && player.getAttribute(Attributes.MOVEMENT_SPEED) !=null) {
                AttributeInstance modifiableattributeinstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (modifiableattributeinstance.getModifier(BLOCKING_SPEED_BOOST_ID) != null) {

                    modifiableattributeinstance.removeModifier(BLOCKING_SPEED_BOOST);
                }
                if (modifiableattributeinstance.getModifier(SPRINT_SPEED_BOOST_ID) != null) {

                    modifiableattributeinstance.removeModifier(SPRINT_SPEED_BOOST);
                }
            }
        }
        }
}
