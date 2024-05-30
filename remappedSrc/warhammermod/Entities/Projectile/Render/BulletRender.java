package warhammermod.Entities.Projectile.Render;


import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import warhammermod.Entities.Projectile.Bullet;
import warhammermod.Entities.Projectile.Render.Model.BulletModel;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
public class BulletRender extends EntityRenderer<Bullet> {
    private static final Identifier BULLET_TEXTURE = new Identifier(reference.modid,"textures/entity/bullet.png");
    private final BulletModel model;

    public BulletRender(EntityRendererFactory.Context context) {
        super(context);
        this.model = new BulletModel(context.getPart(Clientside.Bullet));
    }

    public void render(Bullet p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, VertexConsumerProvider multiBufferSource, int p_225623_6_) {
        p_225623_4_.push();
        p_225623_4_.multiply(Vector3f.YP.rotationDegrees(MathHelper.lerp(p_225623_3_, p_225623_1_.prevYaw, p_225623_1_.getYaw()) - 90.0F));
        p_225623_4_.multiply(Vector3f.ZP.rotationDegrees(MathHelper.lerp(p_225623_3_, p_225623_1_.prevPitch, p_225623_1_.getPitch()) + 90.0F));
        VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(this.model.getLayer(BULLET_TEXTURE));
        this.model.render(p_225623_4_, ivertexbuilder, p_225623_6_, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        p_225623_4_.pop();
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, multiBufferSource, p_225623_6_);
    }

    @Override
    public Identifier getTextureLocation(Bullet p_110775_1_) {
        return BULLET_TEXTURE;
    }
}
