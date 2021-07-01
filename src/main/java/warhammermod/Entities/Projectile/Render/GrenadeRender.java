package warhammermod.Entities.Projectile.Render;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import warhammermod.Entities.Projectile.Bullet;
import warhammermod.Entities.Projectile.GrenadeEntity;
import warhammermod.Entities.Projectile.Render.Model.BulletModel;
import warhammermod.Entities.Projectile.Render.Model.GrenadeModel;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
public class GrenadeRender extends EntityRenderer<GrenadeEntity> {
    private final GrenadeModel model;
    private static final ResourceLocation grenade_texture = new ResourceLocation(reference.modid,"textures/entity/grenade.png");

    public GrenadeRender(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GrenadeModel(context.bakeLayer(Clientside.Grenade));
    }
    public void render(GrenadeEntity p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource multiBufferSource, int p_225623_6_) {
        p_225623_4_.pushPose();
        p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(p_225623_3_, p_225623_1_.yRotO, p_225623_1_.getYRot()) - 90.0F));
        p_225623_4_.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(p_225623_3_, p_225623_1_.xRotO, p_225623_1_.getXRot()) + 90.0F));
        VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(this.model.renderType(grenade_texture));
        this.model.renderToBuffer(p_225623_4_, ivertexbuilder, p_225623_6_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_225623_4_.popPose();
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, multiBufferSource, p_225623_6_);
    }


    @Override
    public ResourceLocation getTextureLocation(GrenadeEntity p_110775_1_) {
        return grenade_texture;
    }
}
