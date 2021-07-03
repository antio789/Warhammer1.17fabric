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
public class DwarfshieldModel extends abstractModshieldModel {
    public ModelPart plate1;
    public ModelPart plate2;
    public ModelPart plate3;
    public ModelPart plate4;
    public ModelPart plate5;
    public ModelPart plate6;
    public ModelPart plate7;
    public ModelPart plate8;
    public ModelPart plate9;
    public ModelPart handle;

    public DwarfshieldModel(ModelPart modelPart){
        super(modelPart);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("plate1", CubeListBuilder.create().texOffs(0,0).addBox(-3F,-11F,-2.0F,6,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate2",CubeListBuilder.create().texOffs(0, 2).addBox(-5F,-10F,-2.0F,10,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate3", CubeListBuilder.create().texOffs(0, 4).addBox(-6F,-9F,-2.0F,12,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate4", CubeListBuilder.create().texOffs(0, 6).addBox(-7F,-8F,-2.0F,14,2,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate5", CubeListBuilder.create().texOffs(0, 9).addBox(-8F,-6F,-2.0F,16,6,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate6", CubeListBuilder.create().texOffs(0, 16).addBox(-7F,0F,-2.0F,14,2,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate7", CubeListBuilder.create().texOffs(0, 19).addBox(-6F,2F,-2.0F,12,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate8", CubeListBuilder.create().texOffs(0, 21).addBox(-5F,3F,-2.0F,10,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate9", CubeListBuilder.create().texOffs(0, 23).addBox(-3F,4F,-2.0F,6,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(48, 0).addBox(-1F, -4F, -1F, 2, 6, 6), PartPose.ZERO);;

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public ResourceLocation gettexture() {
        return new ResourceLocation(reference.modid,"textures/items/shields/dwarf_shield.png");
    }

    @Override
    public boolean isItem(Item item) {
        return item.equals(ItemsInit.Dwarf_shield);
    }

}
