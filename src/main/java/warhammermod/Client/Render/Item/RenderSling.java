package warhammermod.Client.Render.Item;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import warhammermod.Client.Clientside;
import warhammermod.Client.Render.Item.Model.SlingModel;
import warhammermod.Items.ranged.SlingTemplate;
import warhammermod.utils.reference;
@Environment(EnvType.CLIENT)
public class RenderSling implements BuiltinItemRendererRegistry.DynamicItemRenderer, SynchronousResourceReloader {

    private static final Identifier resource_location=Identifier.of(reference.modid,"textures/item/sling.png");
    private SlingModel model;
    private EntityModelLoader entityModelSet;


    public RenderSling(){
        entityModelSet =  MinecraftClient.getInstance().getEntityModelLoader();
        model = new SlingModel(SlingModel.createLayer().createModel());
    }

    public void reload(ResourceManager resourceManager) {
        entityModelSet =  MinecraftClient.getInstance().getEntityModelLoader();
        model = new SlingModel(entityModelSet.getModelPart(Clientside.Sling));
    }


    public void renderByItem(ItemStack stack, ModelTransformationMode mode, MatrixStack matrixStack, VertexConsumerProvider p_239207_4_, int p_239207_5_, int p_239207_6_) {
        SlingTemplate item = (SlingTemplate) stack.getItem();
        matrixStack.push();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        Boolean used = MinecraftClient.getInstance().player.getActiveItem()==stack;
        VertexConsumer ivertexbuilder1 = ItemRenderer.getDirectItemGlintConsumer(p_239207_4_, this.model.getLayer(resource_location), false, stack.hasGlint());
        this.model.render(matrixStack, ivertexbuilder1, p_239207_5_, p_239207_6_,  1,item,used);
        matrixStack.pop();
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        SlingTemplate item = (SlingTemplate) stack.getItem();
        matrices.push();
        matrices.scale(1.0F, -1.0F, -1.0F);
        if(MinecraftClient.getInstance().player instanceof ClientPlayerEntity clplayer && clplayer.getActiveItem() != null);
        Boolean used = MinecraftClient.getInstance().player.getActiveItem()==stack;//to change
        VertexConsumer ivertexbuilder1 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.model.getLayer(resource_location), false, stack.hasGlint());
        this.model.render(matrices, ivertexbuilder1, light, overlay, -1,item,used);
        matrices.pop();
    }
}
