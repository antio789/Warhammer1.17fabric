package warhammermod.Client.Render.Entity.Renders.Layers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class SkavenItemInHandLayer<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms> extends FeatureRenderer<T, M> {
   private final HeldItemRenderer heldItemRenderer;

   public SkavenItemInHandLayer(FeatureRendererContext<T, M> renderLayerParent,HeldItemRenderer heldItemRenderer) {
      super(renderLayerParent);
      this.heldItemRenderer=heldItemRenderer;
   }

   public void render(MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
      boolean bl = livingEntity.getMainArm() == Arm.RIGHT;
      ItemStack itemStack = bl ? livingEntity.getOffHandStack() : livingEntity.getMainHandStack();
      ItemStack itemStack2 = bl ? livingEntity.getMainHandStack() : livingEntity.getOffHandStack();
      if (!itemStack.isEmpty() || !itemStack2.isEmpty()) {
         poseStack.push();
         if (this.getContextModel().child) {
            float m = 0.5F;
            poseStack.translate(0.0D, 0.75D, 0.0D);
            poseStack.scale(m, m, m);
         }

         this.renderArmWithItem(livingEntity, itemStack2, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, poseStack, multiBufferSource, i);
         this.renderArmWithItemleft(livingEntity, itemStack, ModelTransformationMode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, poseStack, multiBufferSource, i);
         poseStack.pop();
      }
   }

   protected void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ModelTransformationMode transformType, Arm humanoidArm, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i) {
      if (!itemStack.isEmpty()) {
         poseStack.push();
         ((ModelWithArms)this.getContextModel()).setArmAngle(humanoidArm, poseStack);
         poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
         poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
         boolean bl = false;
         poseStack.translate((double)((float) 1 / 16.0F), 0.125D, -0.625D);
         float f = 0.8F;
         poseStack.scale(f,f,f);
         this.heldItemRenderer.renderItem(livingEntity, itemStack, transformType, bl, poseStack, multiBufferSource, i);
         poseStack.pop();
      }
   }
   protected void renderArmWithItemleft(LivingEntity livingEntity, ItemStack itemStack, ModelTransformationMode transformType, Arm humanoidArm, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i) {
      if (!itemStack.isEmpty()) {
         poseStack.push();
         ((ModelWithArms)this.getContextModel()).setArmAngle(humanoidArm, poseStack);
         poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
         poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
         boolean bl = humanoidArm == Arm.LEFT;
         poseStack.translate((double)((float)(bl ? -1 : 1) / 16.0F), 0.125D, -0.625D);
         float f = 0.8F;
         poseStack.scale(f, f, f);
         this.heldItemRenderer.renderItem(livingEntity, itemStack, transformType, bl, poseStack, multiBufferSource, i);
         poseStack.pop();
      }
   }
}
