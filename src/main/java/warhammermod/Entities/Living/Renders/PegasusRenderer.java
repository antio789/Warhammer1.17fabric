package warhammermod.Entities.Living.Renders;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Variant;
import warhammermod.Entities.Living.PegasusEntity;
import warhammermod.Entities.Living.Renders.Layers.PegasusArmorLayer;
import warhammermod.Entities.Living.Renders.Layers.PegasusMarkingsLayer;
import warhammermod.Entities.Living.Renders.Models.Pegasusmodel;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

import java.util.Map;

@Environment(EnvType.CLIENT)
public final class PegasusRenderer extends AbstractHorseRenderer<PegasusEntity, Pegasusmodel<PegasusEntity>> {
   private static final Map<Variant, ResourceLocation> COAT_COLORS_RESOURCE_LOCATION_MAP = Util.make(Maps.newEnumMap(Variant.class), (p_239384_0_) -> {
      p_239384_0_.put(Variant.WHITE, new ResourceLocation(reference.modid,"textures/entity/pegasus/pegasus_white.png"));
      p_239384_0_.put(Variant.CREAMY, new ResourceLocation(reference.modid,"textures/entity/pegasus/pegasus_creamy.png"));
      p_239384_0_.put(Variant.CHESTNUT, new ResourceLocation(reference.modid,"textures/entity/pegasus/pegasus_chestnut.png"));
      p_239384_0_.put(Variant.BROWN, new ResourceLocation(reference.modid,"textures/entity/pegasus/pegasus_brown.png"));
      p_239384_0_.put(Variant.BLACK, new ResourceLocation(reference.modid,"textures/entity/pegasus/pegasus_black.png"));
      p_239384_0_.put(Variant.GRAY, new ResourceLocation(reference.modid,"textures/entity/pegasus/pegasus_gray.png"));
      p_239384_0_.put(Variant.DARKBROWN, new ResourceLocation(reference.modid,"textures/entity/pegasus/pegasus_darkbrown.png"));
   });


   private static final ResourceLocation PEGASUS_TEXTURE = new ResourceLocation(reference.modid,"textures/entity/pegasus.png");

   public PegasusRenderer(EntityRendererProvider.Context context) {
      super(context, new Pegasusmodel<>(context.bakeLayer(Clientside.Pegasus)), 1.1F);
      this.addLayer( new PegasusArmorLayer(this, context.getModelSet()));
      this.addLayer(new PegasusMarkingsLayer(this));
   }

   public ResourceLocation getTextureLocation(PegasusEntity entity)
   {

      if(entity.ismixblood()) {
         return COAT_COLORS_RESOURCE_LOCATION_MAP.get(entity.getVariant());
      }
      return PEGASUS_TEXTURE;
   }
}