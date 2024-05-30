package warhammermod.Items.Render.Model;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class abstractModshieldModel extends Model{
    private final ModelPart root;

    public abstractModshieldModel(ModelPart modelPart)
    {
        super(RenderLayer::getEntitySolid);
        this.root = modelPart;
    }
    public void render(MatrixStack p_225598_1_, VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.root.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);

    }
    public abstract boolean isItem(Item item);
    public abstract Identifier gettexture();
}
