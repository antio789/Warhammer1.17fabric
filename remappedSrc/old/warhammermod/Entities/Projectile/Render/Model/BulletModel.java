package warhammermod.Entities.Projectile.Render.Model;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class BulletModel<T extends Entity> extends Model {
    public ModelPart bullet;

    public BulletModel(ModelPart modelPart) {
        super(RenderLayer::getEntitySolid);
        this.bullet = modelPart;
    }

    public static TexturedModelData createLayer() {
        ModelData meshDefinition = new ModelData();
        ModelPartData partDefinition = meshDefinition.getRoot();
        partDefinition.addChild("bullet", ModelPartBuilder.create().uv(0,0).cuboid(-1.0F, -1.0F, -1.0F, 2, 2, 2), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(meshDefinition, 64, 32);
    }


    public void render(MatrixStack p_225598_1_, VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.bullet.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }



}