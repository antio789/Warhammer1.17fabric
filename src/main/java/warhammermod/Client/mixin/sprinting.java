package warhammermod.Client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import warhammermod.Items.melee.SmallShieldTemplate;

@Mixin(ClientPlayerEntity.class)
public abstract class sprinting {
    //ClientPlayerEntity player = ((ClientPlayerEntity)(Object)this);
    @Shadow abstract boolean isUsingItem();
    @Shadow abstract Hand getActiveHand();
    @Shadow abstract ItemStack getStackInHand(Hand hand);
    @Shadow abstract boolean isSprinting();
    @Shadow abstract boolean isWalking();
    @Shadow abstract boolean canSprint();
    @Shadow abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);
    @Shadow abstract boolean hasVehicle();
    @Shadow abstract boolean canVehicleSprint(Entity entity);
    @Shadow abstract Entity getVehicle();
    @Shadow abstract boolean isFallFlying();

    @Inject(method="canStartSprinting",at = @At("TAIL"))
    public boolean cansprintwithshield(CallbackInfoReturnable<Boolean> info){
        if(!info.getReturnValue() && isUsingItem()){
            if(getStackInHand(getActiveHand()).getItem() instanceof SmallShieldTemplate)
            {
                return !this.isSprinting() && this.isWalking() && this.canSprint() &&
                        !this.hasStatusEffect(StatusEffects.BLINDNESS) &&
                        (!this.hasVehicle() || this.canVehicleSprint(this.getVehicle())) && !this.isFallFlying();

            }else return info.getReturnValue();
        }
        return info.getReturnValue();
    }
}
