package warhammermod.Items.Render.Model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

/**
 * nuln repeater handgun.json - Undefined
 * Created using Tabula 7.0.1
 */
public class RepeaterModel extends Model {
    public ModelPart barrel_holder1;
    private final ModelPart root;

    public RepeaterModel(ModelPart modelPart) {
        super(RenderType::entitySolid);
        this.root = modelPart;
        this.barrel_holder1 =root.getChild("barrelholder1");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition barrelholder1 = partDefinition.addOrReplaceChild("barrelholder1", CubeListBuilder.create().texOffs(35, 25).addBox(-2, -2F, 0.0F, 4, 4, 1), PartPose.offset(0.5F, -2.0F, 8.0F));

        partDefinition.addOrReplaceChild("stock1", CubeListBuilder.create().texOffs(0, 25).addBox(0.0F, 0.0F, 0.0F, 1, 2, 2), PartPose.offset(0.0F, 0.0F, -2.0F));
        partDefinition.addOrReplaceChild("handle1", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, 0.0F, 0.0F, 1, 1, 8), PartPose.offset(0.0F, 0.0F, 0F));
        partDefinition.addOrReplaceChild("handle2", CubeListBuilder.create().texOffs(0, 25).addBox(0.0F, 0.0F, 0.0F, 1, 1, 1), PartPose.offset(0.0F, -1.0F, 1.0F));
        partDefinition.addOrReplaceChild("stockbase", CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -2.0F, 0.0F, 3, 3, 1), PartPose.offset(0.0F, -1.0F, 2.0F));
        partDefinition.addOrReplaceChild("flint", CubeListBuilder.create().texOffs(57, 20).addBox(0.0F, 0.0F, 0.0F, 1, 1, 1), PartPose.offset(0.0F, -4.0F, 2.0F));
        partDefinition.addOrReplaceChild("stock", CubeListBuilder.create().texOffs(45, 0).addBox(-1.5F, 0.0F, 0.0F, 4, 4, 2), PartPose.offset(0.0F, -4.0F, 3.0F));
        barrelholder1.addOrReplaceChild("barrelholder2", CubeListBuilder.create().texOffs(35, 25).addBox(-1.5F, 0.0F, 0.0F, 4, 4, 1), PartPose.offset(-0.5F, -2F, 4.0F));
        barrelholder1.addOrReplaceChild("barrel1", CubeListBuilder.create().texOffs(0,0).addBox(0.0F, 0.0F, 0.0F, 1, 1, 9), PartPose.offset(-0.5F, -1.9F, -3F));
        barrelholder1.addOrReplaceChild("barrel2", CubeListBuilder.create().texOffs(0,0).addBox(-0.5F, -0.5F, 0.0F, 1, 1, 9), PartPose.offsetAndRotation(-1.2F, -0.7F, -3F,0.0F, 0.0F, 0.5235987755982988F));
        barrelholder1.addOrReplaceChild("barrel3", CubeListBuilder.create().texOffs(0,0).addBox(-0.5F, -0.5F, 0.0F, 1, 1, 9), PartPose.offsetAndRotation(-1.2F, 0.7F, -3F,0.0F, 0.0F, -0.5235987755982988F));
        barrelholder1.addOrReplaceChild("barrel4", CubeListBuilder.create().texOffs(0,0).addBox(0.0F, 0.0F, 0.0F, 1, 1, 9), PartPose.offset(-0.5F, 0.9F, -3F));
        barrelholder1.addOrReplaceChild("barrel5", CubeListBuilder.create().texOffs(0,0).addBox(-0.5F, -0.5F, 0.0F, 1, 1, 9), PartPose.offsetAndRotation(1.2F, 0.7F, -3F,0.0F, 0.0F, 0.5235987755982988F));
        barrelholder1.addOrReplaceChild("barrel6", CubeListBuilder.create().texOffs(0,0).addBox(-0.5F, -0.5F, 0.0F, 1, 1, 9), PartPose.offsetAndRotation(1.2F, -0.7F, -3F,0.0F, 0.0F, -0.5235987755982988F));

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    public void renderToBuffer(PoseStack p_225598_1_, VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.root.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}
