package warhammermod.Items.Render;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import warhammermod.Items.Render.Model.RepeaterModel;
import warhammermod.utils.Clientside;


public class RenderRepeater implements BuiltinItemRendererRegistry.DynamicItemRenderer, ResourceManagerReloadListener{
    private static final String resource_location="warhammermod:textures/items/repeater.png";
    static final ResourceLocation TEXTURE = new ResourceLocation(resource_location);
    private RepeaterModel model;
    private static int rotation;
    private EntityModelSet entityModelSet;

    public RenderRepeater(){
        entityModelSet =  Minecraft.getInstance().getEntityModels();
        model = new RepeaterModel(RepeaterModel.createLayer().bakeRoot());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resource) {
        entityModelSet =  Minecraft.getInstance().getEntityModels();
        model = new RepeaterModel(entityModelSet.bakeLayer(Clientside.Repeaterlayer));
    }
    public static void setrotationangle(){
        if(rotation==300){
            rotation=0;
        }else rotation+=60;
    }

    @Override
    public void render(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();
        matrices.scale(1.0F, -1.0F, -1.0F);
        this.model.barrel_holder1.zRot=rotation;
        VertexConsumer ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(vertexConsumers, this.model.renderType(TEXTURE), false, stack.hasFoil());
        this.model.renderToBuffer(matrices, ivertexbuilder1, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.popPose();
    }


}
