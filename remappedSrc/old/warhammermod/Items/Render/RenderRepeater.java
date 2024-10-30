package warhammermod.Items.Render;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import warhammermod.Items.Render.Model.RepeaterModel;
import warhammermod.utils.Clientside;

@Environment(EnvType.CLIENT)
public class RenderRepeater implements BuiltinItemRendererRegistry.DynamicItemRenderer, SynchronousResourceReloader{
    private static final String resource_location="warhammermod:textures/items/repeater.png";
    static final Identifier TEXTURE = new Identifier(resource_location);
    private RepeaterModel model;
    private static int rotation;
    private EntityModelLoader entityModelSet;

    public RenderRepeater(){
        entityModelSet =  MinecraftClient.getInstance().getEntityModelLoader();
        model = new RepeaterModel(RepeaterModel.createLayer().createModel());
    }

    @Override
    public void reload(ResourceManager resource) {
        entityModelSet =  MinecraftClient.getInstance().getEntityModelLoader();
        model = new RepeaterModel(entityModelSet.getModelPart(Clientside.Repeaterlayer));
    }
    public static void setrotationangle(){
        if(rotation==300){
            rotation=0;
        }else rotation+=60;
    }

    @Override
    public void render(ItemStack stack, ModelTransformation.TransformType mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.scale(1.0F, -1.0F, -1.0F);
        this.model.barrel_holder1.roll=rotation;
        VertexConsumer ivertexbuilder1 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.model.getLayer(TEXTURE), false, stack.hasGlint());
        this.model.render(matrices, ivertexbuilder1, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }


}
