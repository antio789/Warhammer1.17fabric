package warhammermod.Entities.Projectile.Render.Model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
@Environment(EnvType.CLIENT)
public class GrenadeModel<T extends Entity> extends Model {
    public ModelPart grenadepart1;
    public ModelPart grenadetop;
    public ModelPart root;

    public GrenadeModel(ModelPart modelPart) {
        super(RenderType::entitySolid);
        this.root = modelPart;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("grenadetop", CubeListBuilder.create().texOffs(3, 0).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3), PartPose.offset(0.0F, 0.0F, 0.0F));
        partDefinition.addOrReplaceChild("grenadepart", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 1.5F, -0.5F, 1, 1, 1), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }
    public void renderToBuffer(PoseStack p_225598_1_, VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.root.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }


}
