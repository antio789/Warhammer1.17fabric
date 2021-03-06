package warhammermod.Entities.Projectile.Render;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import warhammermod.Entities.Projectile.Render.Model.BulletModel;
import warhammermod.Entities.Projectile.Render.Model.WarpBulletModel;
import warhammermod.Entities.Projectile.WarpBulletEntity;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
public class WarpbulletRender extends EntityRenderer<WarpBulletEntity> {
    private static final ResourceLocation BULLET_TEXTURE = new ResourceLocation(reference.modid,"textures/entity/bullet.png");
    private final WarpBulletModel model;

    public WarpbulletRender(EntityRendererProvider.Context context) {
        super(context);
        model=new WarpBulletModel(context.bakeLayer(Clientside.WarpBullet));
    }

    public void render(WarpBulletEntity p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource multiBufferSource, int p_225623_6_) {
        p_225623_4_.pushPose();
        p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(p_225623_3_, p_225623_1_.yRotO, p_225623_1_.getYRot()) - 90.0F));
        p_225623_4_.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(p_225623_3_, p_225623_1_.xRotO, p_225623_1_.getXRot()) + 90.0F));
        VertexConsumer ivertexbuilder =  multiBufferSource.getBuffer(this.model.renderType(BULLET_TEXTURE));
        this.model.renderToBuffer(p_225623_4_, ivertexbuilder, p_225623_6_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_225623_4_.popPose();
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, multiBufferSource, p_225623_6_);

    }

    public ResourceLocation getTextureLocation(WarpBulletEntity entity) {
        return BULLET_TEXTURE;
    }
}
