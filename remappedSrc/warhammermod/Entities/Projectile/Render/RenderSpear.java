package warhammermod.Entities.Projectile.Render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import warhammermod.Entities.Projectile.SpearEntity;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
public class RenderSpear<T extends SpearEntity> extends ProjectileEntityRenderer<SpearEntity> {
   public RenderSpear(EntityRendererFactory.Context context) {
      super(context);
   }
   @Override
   public Identifier getTextureLocation(SpearEntity p_110775_1_) {
      return SPEAR8TEXTURE;
   }
   public static final Identifier SPEAR8TEXTURE = new Identifier(reference.modid,"textures/entity/spear.png");

}