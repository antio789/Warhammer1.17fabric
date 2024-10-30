package warhammermod.Items.Render.Model;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * nuln repeater handgun.json - Undefined
 * Created using Tabula 7.0.1
 */
@Environment(EnvType.CLIENT)
public class RepeaterModel extends Model {
    public ModelPart barrel_holder1;
    private final ModelPart root;

    public RepeaterModel(ModelPart modelPart) {
        super(RenderLayer::getEntitySolid);
        this.root = modelPart;
        this.barrel_holder1 =root.getChild("barrelholder1");
    }

    public static TexturedModelData createLayer() {
        ModelData meshDefinition = new ModelData();
        ModelPartData partDefinition = meshDefinition.getRoot();
        ModelPartData barrelholder1 = partDefinition.addChild("barrelholder1", ModelPartBuilder.create().uv(35, 25).cuboid(-2, -2F, 0.0F, 4, 4, 1), ModelTransform.pivot(0.5F, -2.0F, 8.0F));

        partDefinition.addChild("stock1", ModelPartBuilder.create().uv(0, 25).cuboid(0.0F, 0.0F, 0.0F, 1, 2, 2), ModelTransform.pivot(0.0F, 0.0F, -2.0F));
        partDefinition.addChild("handle1", ModelPartBuilder.create().uv(0, 22).cuboid(0.0F, 0.0F, 0.0F, 1, 1, 8), ModelTransform.pivot(0.0F, 0.0F, 0F));
        partDefinition.addChild("handle2", ModelPartBuilder.create().uv(0, 25).cuboid(0.0F, 0.0F, 0.0F, 1, 1, 1), ModelTransform.pivot(0.0F, -1.0F, 1.0F));
        partDefinition.addChild("stockbase", ModelPartBuilder.create().uv(0, 25).cuboid(-1.0F, -2.0F, 0.0F, 3, 3, 1), ModelTransform.pivot(0.0F, -1.0F, 2.0F));
        partDefinition.addChild("flint", ModelPartBuilder.create().uv(57, 20).cuboid(0.0F, 0.0F, 0.0F, 1, 1, 1), ModelTransform.pivot(0.0F, -4.0F, 2.0F));
        partDefinition.addChild("stock", ModelPartBuilder.create().uv(45, 0).cuboid(-1.5F, 0.0F, 0.0F, 4, 4, 2), ModelTransform.pivot(0.0F, -4.0F, 3.0F));
        barrelholder1.addChild("barrelholder2", ModelPartBuilder.create().uv(35, 25).cuboid(-1.5F, 0.0F, 0.0F, 4, 4, 1), ModelTransform.pivot(-0.5F, -2F, 4.0F));
        barrelholder1.addChild("barrel1", ModelPartBuilder.create().uv(0,0).cuboid(0.0F, 0.0F, 0.0F, 1, 1, 9), ModelTransform.pivot(-0.5F, -1.9F, -3F));
        barrelholder1.addChild("barrel2", ModelPartBuilder.create().uv(0,0).cuboid(-0.5F, -0.5F, 0.0F, 1, 1, 9), ModelTransform.of(-1.2F, -0.7F, -3F,0.0F, 0.0F, 0.5235987755982988F));
        barrelholder1.addChild("barrel3", ModelPartBuilder.create().uv(0,0).cuboid(-0.5F, -0.5F, 0.0F, 1, 1, 9), ModelTransform.of(-1.2F, 0.7F, -3F,0.0F, 0.0F, -0.5235987755982988F));
        barrelholder1.addChild("barrel4", ModelPartBuilder.create().uv(0,0).cuboid(0.0F, 0.0F, 0.0F, 1, 1, 9), ModelTransform.pivot(-0.5F, 0.9F, -3F));
        barrelholder1.addChild("barrel5", ModelPartBuilder.create().uv(0,0).cuboid(-0.5F, -0.5F, 0.0F, 1, 1, 9), ModelTransform.of(1.2F, 0.7F, -3F,0.0F, 0.0F, 0.5235987755982988F));
        barrelholder1.addChild("barrel6", ModelPartBuilder.create().uv(0,0).cuboid(-0.5F, -0.5F, 0.0F, 1, 1, 9), ModelTransform.of(1.2F, -0.7F, -3F,0.0F, 0.0F, -0.5235987755982988F));

        return TexturedModelData.of(meshDefinition, 64, 32);
    }

    public void render(MatrixStack p_225598_1_, VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.root.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}
