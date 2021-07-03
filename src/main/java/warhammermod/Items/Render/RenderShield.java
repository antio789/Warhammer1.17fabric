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
import warhammermod.Items.Render.Model.*;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

import java.util.ArrayList;
@Environment(EnvType.CLIENT)
public class RenderShield implements BuiltinItemRendererRegistry.DynamicItemRenderer, ResourceManagerReloadListener {
    private final ResourceLocation HEShieldTexture = new ResourceLocation(reference.modid,"textures/items/shields/high_elf_shield.png");
    private final ResourceLocation DEShieldTexture = new ResourceLocation(reference.modid,"textures/items/shields/dark_elf_shield.png");
    private final ResourceLocation DWShieldTexture = new ResourceLocation(reference.modid,"textures/items/shields/dwarf_shield.png");
    private final ResourceLocation EMShieldTexture = new ResourceLocation(reference.modid,"textures/items/shields/empire_shield.png");
    private final ResourceLocation SKShieldTexture = new ResourceLocation(reference.modid,"textures/items/shields/skaven_shield.png");

    private abstractModshieldModel modelShieldDE;
    private abstractModshieldModel modelShieldEM;
    private abstractModshieldModel modelShieldHE;
    private abstractModshieldModel modelShieldSK;
    private abstractModshieldModel modelShieldDW;
    private ArrayList<abstractModshieldModel> shields;

    public RenderShield(){
        entityModelSet =  Minecraft.getInstance().getEntityModels();
        modelShieldDE = new DarkElfshieldmodel(DarkElfshieldmodel.createLayer().bakeRoot());
        modelShieldHE = new HighelfshieldModel(HighelfshieldModel.createLayer().bakeRoot());
        modelShieldEM = new EmpireShieldmodel(EmpireShieldmodel.createLayer().bakeRoot());
        modelShieldSK = new DwarfshieldModel(DwarfshieldModel.createLayer().bakeRoot());
        modelShieldDW = new SkavenShieldModel(SkavenShieldModel.createLayer().bakeRoot());
    }

    private EntityModelSet entityModelSet;
    public void onResourceManagerReload(ResourceManager resourceManager) {
        entityModelSet =  Minecraft.getInstance().getEntityModels();
        modelShieldDE = new DarkElfshieldmodel(entityModelSet.bakeLayer(Clientside.DEShield));
        modelShieldHE = new HighelfshieldModel(entityModelSet.bakeLayer(Clientside.HEShield));
        modelShieldEM = new EmpireShieldmodel(entityModelSet.bakeLayer(Clientside.EMShield));
        modelShieldSK = new DwarfshieldModel(entityModelSet.bakeLayer(Clientside.DWShield));
        modelShieldDW = new SkavenShieldModel(entityModelSet.bakeLayer(Clientside.SKShield));

    }



    @Override
    public void render(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {

        if(modelShieldSK.isItem(stack.getItem())) {
            rendershield(modelShieldSK,stack,mode,matrices,vertexConsumers,light,overlay);
        }
        if(modelShieldHE.isItem(stack.getItem())) {
            rendershield(modelShieldHE,stack,mode,matrices,vertexConsumers,light,overlay);
        }
        if(modelShieldEM.isItem(stack.getItem())) {
            rendershield(modelShieldEM,stack,mode,matrices,vertexConsumers,light,overlay);
        }
        if(modelShieldDW.isItem(stack.getItem())) {
            rendershield(modelShieldDW,stack,mode,matrices,vertexConsumers,light,overlay);
        }
        if(modelShieldDE.isItem(stack.getItem())) {
            rendershield(modelShieldDE,stack,mode,matrices,vertexConsumers,light,overlay);
        }

    }

    private void rendershield(abstractModshieldModel model,ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay){
        matrices.pushPose();
        matrices.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(vertexConsumers, model.renderType(model.gettexture()), false, stack.hasFoil());
        model.renderToBuffer(matrices, ivertexbuilder1, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.popPose();
    }




}
