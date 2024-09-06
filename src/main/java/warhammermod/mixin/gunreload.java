package warhammermod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class gunreload {
    @Shadow @Final private MinecraftClient minecraft;
    private static final Identifier WIKI_image = Identifier.of(reference.modid,"textures/special/modwiki.png");

    @Shadow protected abstract void renderTextureOverlay(Identifier resourceLocation, float f);

    @Shadow protected abstract void renderSpyglassOverlay(float f);

    @Shadow private float scopeScale;
/*
    @Inject(at = @At("RETURN"), method = "renderCrosshair")
    private void renderreload(MatrixStack poseStack, CallbackInfo ci){
        if(this.minecraft.player != null && this.minecraft.player.getActiveItem().getItem() instanceof IReloadItem) {
            IReloadItem item = (IReloadItem) this.minecraft.player.getActiveItem().getItem();
            if (!item.isReadytoFire(this.minecraft.player.getActiveItem()) && !this.minecraft.player.isCreative()) {
                int screenWidth = this.minecraft.getWindow().getScaledWidth();
                int screenHeight = this.minecraft.getWindow().getScaledHeight();

                int reloadtime = item.getTimetoreload();
                float diff = reloadtime - this.minecraft.player.getItemUseTime();
                float f = MathHelper.clamp(diff / reloadtime, 0,1);


                int j = screenHeight / 2 - 7 + 16;
                int k = screenWidth / 2 - 8;

                int l = (int) (f * 17.0F);
                this.minecraft.inGameHud.drawTexture(poseStack, k, j, 36, 94, 16, 4);
                this.minecraft.inGameHud.drawTexture(poseStack, k, j, 52, 94, l, 4);
            }
            else if(this.minecraft.player.getActiveItem().getItem() == ItemsInit.Warplock_jezzail && this.minecraft.options.getPerspective().isFirstPerson()){
                renderSpyglassOverlay(1);
            }

        }
        if(Clientside.Wiki_Map.isPressed()){
            this.renderTextureOverlay(WIKI_image,1);
        }
    }*/
}
