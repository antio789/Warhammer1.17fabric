package warhammermod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import warhammermod.Items.IReloadItem;
import warhammermod.Items.ItemsInit;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class gunreload {
    @Shadow @Final private Minecraft minecraft;
    private static final ResourceLocation WIKI_image = new ResourceLocation(reference.modid,"textures/special/modwiki.png");

    @Shadow protected abstract void renderTextureOverlay(ResourceLocation resourceLocation, float f);

    @Shadow protected abstract void renderSpyglassOverlay(float f);

    @Shadow private float scopeScale;

    @Inject(at = @At("RETURN"), method = "renderCrosshair")
    private void renderreload(PoseStack poseStack, CallbackInfo ci){
        if(this.minecraft.player != null && this.minecraft.player.getUseItem().getItem() instanceof IReloadItem) {
            IReloadItem item = (IReloadItem) this.minecraft.player.getUseItem().getItem();
            if (!item.isReadytoFire(this.minecraft.player.getUseItem()) && !this.minecraft.player.isCreative()) {
                int screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
                int screenHeight = this.minecraft.getWindow().getGuiScaledHeight();

                int reloadtime = item.getTimetoreload();
                float diff = reloadtime - this.minecraft.player.getTicksUsingItem();
                float f = Mth.clamp(diff / reloadtime, 0,1);


                int j = screenHeight / 2 - 7 + 16;
                int k = screenWidth / 2 - 8;

                int l = (int) (f * 17.0F);
                this.minecraft.gui.blit(poseStack, k, j, 36, 94, 16, 4);
                this.minecraft.gui.blit(poseStack, k, j, 52, 94, l, 4);
            }
            else if(this.minecraft.player.getUseItem().getItem() == ItemsInit.Warplock_jezzail && this.minecraft.options.getCameraType().isFirstPerson()){
                renderSpyglassOverlay(1);
            }

        }
        if(Clientside.Wiki_Map.isDown()){
            this.renderTextureOverlay(WIKI_image,1);
        }
    }
}
