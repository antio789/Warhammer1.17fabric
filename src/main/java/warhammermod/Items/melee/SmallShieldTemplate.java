package warhammermod.Items.melee;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import warhammermod.utils.reference;

import java.util.UUID;

public class SmallShieldTemplate extends ShieldItem {
    private Boolean issprinting=false;

    private static final UUID BLOCKING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3B-4F1C-8813-96EA6097278D");
    private static final EntityAttributeModifier BLOCKING_SPEED_BOOST = new EntityAttributeModifier(BLOCKING_SPEED_BOOST_ID, "blocking speed boost", 2D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final UUID SPRINT_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3C-4F1D-8814-96EA6197278D");
    private static final EntityAttributeModifier SPRINT_SPEED_BOOST = (new EntityAttributeModifier(SPRINT_SPEED_BOOST_ID, "sprint speed boost", 3.5D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));


    public SmallShieldTemplate(Settings builder){
        super(builder.maxDamage(1008).tab(reference.warhammer));
        FabricModelPredicateProviderRegistry.register(this, new Identifier("blocking"), (p_239421_0_, p_239421_1_, p_239421_2_,i) -> {
            return p_239421_2_ != null && p_239421_2_.isUsingItem() && p_239421_2_.getActiveItem() == p_239421_0_ ? 1.0F : 0.0F;
        });
    }

    private Boolean needupdate = false;
    public void usageTick(World world, LivingEntity player, ItemStack stack, int count) {
        if(player instanceof PlayerEntity && world.isClient()) {
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

    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)  {
        if(entityLiving instanceof PlayerEntity && entityLiving.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED) !=null) {
            EntityAttributeInstance modifiableattributeinstance = entityLiving.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
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
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof PlayerEntity && worldIn.isClient() ){
            LivingEntity player = (LivingEntity) entityIn;
            if(needupdate){
                issprinting = MinecraftClient.getInstance().options.sprintKey.isPressed();
                EntityAttributeInstance modifiableattributeinstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (modifiableattributeinstance.getModifier(BLOCKING_SPEED_BOOST_ID) != null) {
                    modifiableattributeinstance.removeModifier(BLOCKING_SPEED_BOOST);
                }
                if (modifiableattributeinstance.getModifier(SPRINT_SPEED_BOOST_ID) != null) {
                    modifiableattributeinstance.removeModifier(SPRINT_SPEED_BOOST);
                }
                if (issprinting) {
                    modifiableattributeinstance.addPersistentModifier(SPRINT_SPEED_BOOST);
                }

                else {modifiableattributeinstance.addPersistentModifier(BLOCKING_SPEED_BOOST);
                }
                needupdate = false;
            }
            else if(player.getActiveItem() != stack && !player.isUsingItem() && player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED) !=null) {
                EntityAttributeInstance modifiableattributeinstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
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
