package warhammermod.Items.Render;



import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import warhammermod.Items.Render.Model.RatlingGunModel;
import warhammermod.Items.Render.Model.SlingModel;
import warhammermod.Items.ranged.SlingTemplate;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;
@Environment(EnvType.CLIENT)
public class RenderSling implements BuiltinItemRendererRegistry.DynamicItemRenderer, ResourceManagerReloadListener {

    private static final ResourceLocation resource_location=new ResourceLocation(reference.modid,"textures/items/sling.png");
    private SlingModel model;

    private EntityModelSet entityModelSet;
    public void onResourceManagerReload(ResourceManager resourceManager) {
        entityModelSet =  Minecraft.getInstance().getEntityModels();
        model = new SlingModel(entityModelSet.bakeLayer(Clientside.Sling));
    }

    public RenderSling(){
        entityModelSet =  Minecraft.getInstance().getEntityModels();
        model = new SlingModel(SlingModel.createLayer().bakeRoot());
    }

    public void renderByItem(ItemStack stack, ItemTransforms.TransformType p_239207_2_, PoseStack p_239207_3_, MultiBufferSource p_239207_4_, int p_239207_5_, int p_239207_6_) {
        SlingTemplate item = (SlingTemplate) stack.getItem();
        p_239207_3_.pushPose();
        p_239207_3_.scale(1.0F, -1.0F, -1.0F);
        Boolean used = Minecraft.getInstance().player.getUseItem()==stack;
        VertexConsumer ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(p_239207_4_, this.model.renderType(resource_location), false, stack.hasFoil());
        this.model.render(p_239207_3_, ivertexbuilder1, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F,item,used);
        p_239207_3_.popPose();
    }


    @Override
    public void render(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        SlingTemplate item = (SlingTemplate) stack.getItem();
        matrices.pushPose();
        matrices.scale(1.0F, -1.0F, -1.0F);
        Boolean used = Minecraft.getInstance().player.getUseItem()==stack;
        VertexConsumer ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(vertexConsumers, this.model.renderType(resource_location), false, stack.hasFoil());
        this.model.render(matrices, ivertexbuilder1, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F,item,used);
        matrices.popPose();
    }




}
