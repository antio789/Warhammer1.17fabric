package warhammermod.Entities.Projectile.Render.Model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
@Environment(EnvType.CLIENT)
public class GrenadeModel<T extends Entity> extends Model {
    public ModelPart grenadepart1;
    public ModelPart grenadetop;
    public ModelPart root;

    public GrenadeModel(ModelPart modelPart) {
        super(RenderLayer::getEntitySolid);
        this.root = modelPart;
    }

    public static TexturedModelData createLayer() {
        ModelData meshDefinition = new ModelData();
        ModelPartData partDefinition = meshDefinition.getRoot();
        partDefinition.addChild("grenadetop", ModelPartBuilder.create().uv(3, 0).cuboid(-1.5F, -1.5F, -1.5F, 3, 3, 3), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        partDefinition.addChild("grenadepart", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, 1.5F, -0.5F, 1, 1, 1), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(meshDefinition, 64, 32);
    }
    public void render(MatrixStack p_225598_1_, VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.root.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }


}
