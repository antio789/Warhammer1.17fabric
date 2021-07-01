package warhammermod.Entities.Projectile.Render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import warhammermod.Entities.Projectile.SpearEntity;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
public class RenderSpear<T extends SpearEntity> extends ArrowRenderer<SpearEntity> {
   public RenderSpear(EntityRendererProvider.Context context) {
      super(context);
   }
   @Override
   public ResourceLocation getTextureLocation(SpearEntity p_110775_1_) {
      return SPEAR8TEXTURE;
   }
   public static final ResourceLocation SPEAR8TEXTURE = new ResourceLocation(reference.modid,"textures/entity/spear.png");

}