package warhammermod.Entities.Living.Renders;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import warhammermod.Entities.Living.PegasusEntity;
import warhammermod.Entities.Living.Renders.Layers.PegasusArmorLayer;
import warhammermod.Entities.Living.Renders.Layers.PegasusMarkingsLayer;
import warhammermod.Entities.Living.Renders.Models.Pegasusmodel;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

import java.util.Map;

@Environment(EnvType.CLIENT)
public final class PegasusRenderer extends AbstractHorseEntityRenderer<PegasusEntity, Pegasusmodel<PegasusEntity>> {
   private static final Map<HorseColor, Identifier> COAT_COLORS_RESOURCE_LOCATION_MAP = Util.make(Maps.newEnumMap(HorseColor.class), (p_239384_0_) -> {
      p_239384_0_.put(HorseColor.WHITE, new Identifier(reference.modid,"textures/entity/pegasus/pegasus_white.png"));
      p_239384_0_.put(HorseColor.CREAMY, new Identifier(reference.modid,"textures/entity/pegasus/pegasus_creamy.png"));
      p_239384_0_.put(HorseColor.CHESTNUT, new Identifier(reference.modid,"textures/entity/pegasus/pegasus_chestnut.png"));
      p_239384_0_.put(HorseColor.BROWN, new Identifier(reference.modid,"textures/entity/pegasus/pegasus_brown.png"));
      p_239384_0_.put(HorseColor.BLACK, new Identifier(reference.modid,"textures/entity/pegasus/pegasus_black.png"));
      p_239384_0_.put(HorseColor.GRAY, new Identifier(reference.modid,"textures/entity/pegasus/pegasus_gray.png"));
      p_239384_0_.put(HorseColor.DARK_BROWN, new Identifier(reference.modid,"textures/entity/pegasus/pegasus_darkbrown.png"));
   });


   private static final Identifier PEGASUS_TEXTURE = new Identifier(reference.modid,"textures/entity/pegasus.png");

   public PegasusRenderer(EntityRendererFactory.Context context) {
      super(context, new Pegasusmodel<>(context.getPart(Clientside.Pegasus)), 1.1F);
      this.addFeature( new PegasusArmorLayer(this, context.getModelLoader()));
      this.addFeature(new PegasusMarkingsLayer(this));
   }

   public Identifier getTextureLocation(PegasusEntity entity)
   {

      if(entity.ismixblood()) {
         return COAT_COLORS_RESOURCE_LOCATION_MAP.get(entity.getVariant());
      }
      return PEGASUS_TEXTURE;
   }
}