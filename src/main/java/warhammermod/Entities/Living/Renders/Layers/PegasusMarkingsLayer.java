package warhammermod.Entities.Living.Renders.Layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Markings;
import warhammermod.Entities.Living.PegasusEntity;
import warhammermod.Entities.Living.Renders.Models.Pegasusmodel;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class PegasusMarkingsLayer extends RenderLayer<PegasusEntity, Pegasusmodel<PegasusEntity>> {
   private static final Map<Markings, ResourceLocation> LOCATION_BY_MARKINGS = Util.make(Maps.newEnumMap(Markings.class), (p_239406_0_) -> {
      p_239406_0_.put(Markings.NONE, null);
      p_239406_0_.put(Markings.WHITE, new ResourceLocation("textures/entity/horse/horse_markings_white.png"));
      p_239406_0_.put(Markings.WHITE_FIELD, new ResourceLocation("textures/entity/horse/horse_markings_whitefield.png"));
      p_239406_0_.put(Markings.WHITE_DOTS, new ResourceLocation("textures/entity/horse/horse_markings_whitedots.png"));
      p_239406_0_.put(Markings.BLACK_DOTS, new ResourceLocation("textures/entity/horse/horse_markings_blackdots.png"));
   });

   public PegasusMarkingsLayer(RenderLayerParent<PegasusEntity, Pegasusmodel<PegasusEntity>> p_i232476_1_) {
      super(p_i232476_1_);
   }

   public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, PegasusEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      ResourceLocation resourcelocation = LOCATION_BY_MARKINGS.get(entitylivingbaseIn.getMarkings());
      if (resourcelocation != null && !entitylivingbaseIn.isInvisible()) {
         VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(resourcelocation));
         this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
      }
   }
}