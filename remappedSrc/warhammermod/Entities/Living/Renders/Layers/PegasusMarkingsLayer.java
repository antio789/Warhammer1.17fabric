package warhammermod.Entities.Living.Renders.Layers;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseMarking;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import warhammermod.Entities.Living.PegasusEntity;
import warhammermod.Entities.Living.Renders.Models.Pegasusmodel;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class PegasusMarkingsLayer extends FeatureRenderer<PegasusEntity, Pegasusmodel<PegasusEntity>> {
   private static final Map<HorseMarking, Identifier> LOCATION_BY_MARKINGS = Util.make(Maps.newEnumMap(HorseMarking.class), (p_239406_0_) -> {
      p_239406_0_.put(HorseMarking.NONE, null);
      p_239406_0_.put(HorseMarking.WHITE, new Identifier("textures/entity/horse/horse_markings_white.png"));
      p_239406_0_.put(HorseMarking.WHITE_FIELD, new Identifier("textures/entity/horse/horse_markings_whitefield.png"));
      p_239406_0_.put(HorseMarking.WHITE_DOTS, new Identifier("textures/entity/horse/horse_markings_whitedots.png"));
      p_239406_0_.put(HorseMarking.BLACK_DOTS, new Identifier("textures/entity/horse/horse_markings_blackdots.png"));
   });

   public PegasusMarkingsLayer(FeatureRendererContext<PegasusEntity, Pegasusmodel<PegasusEntity>> p_i232476_1_) {
      super(p_i232476_1_);
   }

   public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, PegasusEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      Identifier resourcelocation = LOCATION_BY_MARKINGS.get(entitylivingbaseIn.getMarking());
      if (resourcelocation != null && !entitylivingbaseIn.isInvisible()) {
         VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntityTranslucent(resourcelocation));
         this.getContextModel().render(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlay(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
      }
   }
}