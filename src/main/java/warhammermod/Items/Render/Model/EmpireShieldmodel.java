package warhammermod.Items.Render.Model;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import warhammermod.Items.ItemsInit;
import warhammermod.utils.reference;
@Environment(EnvType.CLIENT)
public class EmpireShieldmodel extends abstractModshieldModel {

    public ModelPart plate2;
    public ModelPart plate3;
    public ModelPart plate4;
    public ModelPart plate5;
    public ModelPart plate6;
    public ModelPart plate7;
    public ModelPart plate8;
    public ModelPart handle;

    public EmpireShieldmodel(ModelPart modelPart){
        super(modelPart);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("plate2",CubeListBuilder.create().texOffs(0, 0).addBox(-6F,-11F,-2.0F,12,14,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate3", CubeListBuilder.create().texOffs(0, 20).addBox(-5F,3F,-2.0F,10,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate4", CubeListBuilder.create().texOffs(0, 22).addBox(-4F,4F,-2.0F,8,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate5", CubeListBuilder.create().texOffs(0, 24).addBox(-3F,5F,-2.0F,6,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate6", CubeListBuilder.create().texOffs(0, 26).addBox(-2F,6F,-2.0F,4,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate7", CubeListBuilder.create().texOffs(0, 28).addBox(-1F,7F,-2.0F,2,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate8", CubeListBuilder.create().texOffs(0, 30).addBox(-0.5F,8F,-2.0F,1,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -4.0F, -1.0F, 2, 6, 6), PartPose.ZERO);;

        return LayerDefinition.create(meshDefinition, 64, 64);
    }
    @Override
    public ResourceLocation gettexture() {
        return new ResourceLocation(reference.modid,"textures/items/shields/empire_shield.png");
    }

    @Override
    public boolean isItem(Item item) {
        return item.equals(ItemsInit.Imperial_shield);
    }



}
