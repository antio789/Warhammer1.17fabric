package warhammermod.Items.melee;


import com.mojang.authlib.minecraft.client.MinecraftClient;
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


    public SmallShieldTemplate(Settings builder){
        super(builder.maxDamage(1008));
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

}
