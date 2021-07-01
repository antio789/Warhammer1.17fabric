package warhammermod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import warhammermod.Items.GunBase;
import warhammermod.Items.IReloadItem;
import warhammermod.Items.ItemsInit;
@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayer.class)
public class fovcapture {
    @Inject(at = @At("RETURN"), method = "getFieldOfViewModifier", cancellable = true)
    private void changefov(CallbackInfoReturnable<Float> cir){
        Item item = Minecraft.getInstance().player.getUseItem().getItem();
        if(item == ItemsInit.Warplock_jezzail && ((GunBase)item).isReadytoFire(Minecraft.getInstance().player.getUseItem())){
            cir.setReturnValue(0.5F);
        }
    }
}
