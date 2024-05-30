package warhammermod.Entities.Living.Renders.Layers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.DyeableHorseArmorItem;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import warhammermod.Entities.Living.PegasusEntity;
import warhammermod.Entities.Living.Renders.Models.Pegasusmodel;
import warhammermod.utils.Clientside;

@Environment(EnvType.CLIENT)
public class PegasusArmorLayer extends FeatureRenderer<PegasusEntity, Pegasusmodel<PegasusEntity>> {
   private final Pegasusmodel<PegasusEntity> model;

   public PegasusArmorLayer(FeatureRendererContext<PegasusEntity, Pegasusmodel<PegasusEntity>> renderLayerParent, EntityModelLoader entityModelSet) {
      super(renderLayerParent);
      this.model = new Pegasusmodel<>(entityModelSet.getModelPart(Clientside.pegasus_armor));
   }

   public void render(MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i, PegasusEntity horse, float f, float g, float h, float j, float k, float l) {
      ItemStack itemStack = horse.getArmorType();
      if (itemStack.getItem() instanceof HorseArmorItem) {
         HorseArmorItem horseArmorItem = (HorseArmorItem)itemStack.getItem();
         (this.getContextModel()).copyStateTo(this.model);
         this.model.prepareMobModel(horse, f, g, h);
         this.model.setAngles(horse, f, g, j, k, l);
         float q;
         float r;
         float s;
         if (horseArmorItem instanceof DyeableHorseArmorItem) {
            int m = ((DyeableHorseArmorItem)horseArmorItem).getColor(itemStack);
            q = (float)(m >> 16 & 255) / 255.0F;
            r = (float)(m >> 8 & 255) / 255.0F;
            s = (float)(m & 255) / 255.0F;
         } else {
            q = 1.0F;
            r = 1.0F;
            s = 1.0F;
         }

         VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderLayer.getEntityCutoutNoCull(horseArmorItem.getEntityTexture()));
         this.model.render(poseStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, q, r, s, 1.0F);
      }
   }
}
