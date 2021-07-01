package warhammermod.Entities.Projectile.Render.Model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public class GrapeshotModel<T extends Entity> extends Model {
    public ModelPart bullet;
    public ModelPart bullet2;
    public ModelPart bullet3;
    public ModelPart bullet4;
    public ModelPart bullet5;
    public ModelPart root;

    public GrapeshotModel(ModelPart modelPart) {
        super(RenderType::entitySolid);
        this.root = modelPart;
        this.bullet =root.getChild("bullet");
        this.bullet2 =root.getChild("bullet_2");
        this.bullet3 =root.getChild("bullet3");
        this.bullet4 =root.getChild("bullet4");
        this.bullet5 =root.getChild("bullet5");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        CubeDeformation f01 = new CubeDeformation(0.1F);
        partDefinition.addOrReplaceChild("bullet", CubeListBuilder.create().texOffs(0,0).addBox(0F, 0F, 0F, 1, 1, 1,f01), PartPose.ZERO);
        partDefinition.addOrReplaceChild("bullet1", CubeListBuilder.create().texOffs(0,0).addBox(-1.0F, -1.0F, -1.0F, 1, 1, 1,f01), PartPose.ZERO);
        partDefinition.addOrReplaceChild("bullet_2", CubeListBuilder.create().texOffs(0,0).addBox(0.5F, 1F, -2F, 1, 1, 1,f01), PartPose.ZERO);
        partDefinition.addOrReplaceChild("bullet3", CubeListBuilder.create().texOffs(0,0).addBox(1.5F, -1F, 1F, 1, 1, 1,f01), PartPose.ZERO);
        partDefinition.addOrReplaceChild("bullet4", CubeListBuilder.create().texOffs(0,0).addBox(-1.5F, 1F, -1.5F, 1, 1, 1,f01), PartPose.ZERO);
        partDefinition.addOrReplaceChild("bullet5", CubeListBuilder.create().texOffs(0,0).addBox(2F, 0.5F, 0F, 1, 1, 1,f01), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 32);
    }



    public void setRotationAngles(T p_225597_1_, float partialticks, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        bullet.x=p_225597_1_.tickCount*3F +2;
        bullet2.y=-p_225597_1_.tickCount*3F -1;
        bullet3.z=-p_225597_1_.tickCount*3F -2;
        bullet4.x=p_225597_1_.tickCount*3F +1.5F;
        bullet5.z=-p_225597_1_.tickCount*3F -1.5F;
    }





    public void renderToBuffer(PoseStack p_225598_1_, VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.root.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }


}
