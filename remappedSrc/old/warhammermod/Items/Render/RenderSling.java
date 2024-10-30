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
import warhammermod.Items.Render.Model.SlingModel;
import warhammermod.Items.ranged.SlingTemplate;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;
@Environment(EnvType.CLIENT)
public class RenderSling implements BuiltinItemRendererRegistry.DynamicItemRenderer, SynchronousResourceReloader {

    private static final Identifier resource_location=new Identifier(reference.modid,"textures/items/sling.png");
    private SlingModel model;

    private EntityModelLoader entityModelSet;
    public void reload(ResourceManager resourceManager) {
        entityModelSet =  MinecraftClient.getInstance().getEntityModelLoader();
        model = new SlingModel(entityModelSet.getModelPart(Clientside.Sling));
    }

    public RenderSling(){
        entityModelSet =  MinecraftClient.getInstance().getEntityModelLoader();
        model = new SlingModel(SlingModel.createLayer().createModel());
    }

    public void renderByItem(ItemStack stack, ModelTransformation.TransformType p_239207_2_, MatrixStack p_239207_3_, VertexConsumerProvider p_239207_4_, int p_239207_5_, int p_239207_6_) {
        SlingTemplate item = (SlingTemplate) stack.getItem();
        p_239207_3_.push();
        p_239207_3_.scale(1.0F, -1.0F, -1.0F);
        Boolean used = MinecraftClient.getInstance().player.getActiveItem()==stack;
        VertexConsumer ivertexbuilder1 = ItemRenderer.getDirectItemGlintConsumer(p_239207_4_, this.model.getLayer(resource_location), false, stack.hasGlint());
        this.model.render(p_239207_3_, ivertexbuilder1, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F,item,used);
        p_239207_3_.pop();
    }


    @Override
    public void render(ItemStack stack, ModelTransformation.TransformType mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        SlingTemplate item = (SlingTemplate) stack.getItem();
        matrices.push();
        matrices.scale(1.0F, -1.0F, -1.0F);
        Boolean used = MinecraftClient.getInstance().player.getActiveItem()==stack;
        VertexConsumer ivertexbuilder1 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.model.getLayer(resource_location), false, stack.hasGlint());
        this.model.render(matrices, ivertexbuilder1, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F,item,used);
        matrices.pop();
    }




}
