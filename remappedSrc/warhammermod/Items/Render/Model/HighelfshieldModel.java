package warhammermod.Items.Render.Model;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import warhammermod.Items.ItemsInit;
import warhammermod.utils.reference;
@Environment(EnvType.CLIENT)
public class HighelfshieldModel extends abstractModshieldModel {
    private final ModelPart root;


    public HighelfshieldModel(ModelPart modelPart)
    {
        super(modelPart);
        this.root = modelPart;
    }

    public static TexturedModelData createLayer() {
        ModelData meshDefinition = new ModelData();
        ModelPartData partDefinition = meshDefinition.getRoot();
        partDefinition.addChild("plate2",ModelPartBuilder.create().uv(0, 0).cuboid(-6F,-11F,-2.0F,11,19,1), ModelTransform.NONE);
        partDefinition.addChild("plate3", ModelPartBuilder.create().uv(0, 20).cuboid(-5F,8F,-2.0F,9,1,1), ModelTransform.NONE);
        partDefinition.addChild("plate4", ModelPartBuilder.create().uv(0, 22).cuboid(-4F,9F,-2.0F,7,1,1), ModelTransform.NONE);
        partDefinition.addChild("plate5", ModelPartBuilder.create().uv(0, 24).cuboid(-3F,10F,-2.0F,5,2,1), ModelTransform.NONE);
        partDefinition.addChild("plate6", ModelPartBuilder.create().uv(0, 27).cuboid(-2F,12F,-2.0F,3,1,1), ModelTransform.NONE);
        partDefinition.addChild("plate7", ModelPartBuilder.create().uv(0, 29).cuboid(-1F,13F,-2.0F,1,1,1), ModelTransform.NONE);
        partDefinition.addChild("handle", ModelPartBuilder.create().uv(426, 0).cuboid(-1F, -3F, -1F, 2, 6, 6), ModelTransform.NONE);;

        return TexturedModelData.of(meshDefinition, 64, 64);
    }
    @Override
    public Identifier gettexture() {
        return new Identifier(reference.modid,"textures/items/shields/high_elf_shield.png");
    }
    public void render(MatrixStack p_225598_1_, VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
       this.root.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }

    @Override
    public boolean isItem(Item item) {
        return item.equals(ItemsInit.High_Elf_Shield);
    }
}
