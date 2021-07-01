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
import warhammermod.Items.Render.RenderShield;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
public class DarkElfshieldmodel extends abstractModshieldModel{


    public DarkElfshieldmodel(ModelPart modelPart)
    {
        super(modelPart);
    }

    @Override
    public boolean isItem(Item item) {
        return item.equals(ItemsInit.Dark_Elf_Shield);
    }

    @Override
    public ResourceLocation gettexture() {
        return new ResourceLocation(reference.modid,"textures/items/shields/dark_elf_shield.png");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("plate",CubeListBuilder.create().texOffs(10,0).addBox(-2F, -11.0F, -2.0F, 7, 19, 1),PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate2",CubeListBuilder.create().texOffs(0, 0).addBox(-6F,-8F,-2.0F,4,16,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate3", CubeListBuilder.create().texOffs(0, 20).addBox(-5F,8F,-2.0F,9,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate4", CubeListBuilder.create().texOffs(0, 22).addBox(-4F,9F,-2.0F,7,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate5", CubeListBuilder.create().texOffs(0, 24).addBox(-3F,10F,-2.0F,5,2,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate6", CubeListBuilder.create().texOffs(0, 27).addBox(-2F,12F,-2.0F,3,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate7", CubeListBuilder.create().texOffs(0, 29).addBox(-3F,-11F,-2.0F,1,1,1).addBox(-6F,-9F,-2.0F,1,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("plate8", CubeListBuilder.create().texOffs(0, 18).addBox(-3F,-11F,-2.0F,1,1,1), PartPose.ZERO);
        partDefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(48, 0).addBox(-1F, -3F, -1F, 2, 6, 6), PartPose.ZERO);;

        return LayerDefinition.create(meshDefinition, 64, 64);
    }






}
