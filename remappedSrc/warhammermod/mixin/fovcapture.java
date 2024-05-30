package warhammermod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import warhammermod.Items.GunBase;
import warhammermod.Items.ItemsInit;
@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public class fovcapture {
    @Inject(at = @At("RETURN"), method = "getFieldOfViewModifier", cancellable = true)
    private void changefov(CallbackInfoReturnable<Float> cir){
        Item item = MinecraftClient.getInstance().player.getActiveItem().getItem();
        if(item == ItemsInit.Warplock_jezzail && ((GunBase)item).isReadytoFire(MinecraftClient.getInstance().player.getActiveItem())){
            cir.setReturnValue(0.5F);
        }
    }
}
