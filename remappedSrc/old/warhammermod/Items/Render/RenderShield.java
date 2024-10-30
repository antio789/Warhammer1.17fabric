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
import warhammermod.Items.Render.Model.*;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

import java.util.ArrayList;
@Environment(EnvType.CLIENT)
public class RenderShield implements BuiltinItemRendererRegistry.DynamicItemRenderer, SynchronousResourceReloader {
    private final Identifier HEShieldTexture = new Identifier(reference.modid,"textures/items/shields/high_elf_shield.png");
    private final Identifier DEShieldTexture = new Identifier(reference.modid,"textures/items/shields/dark_elf_shield.png");
    private final Identifier DWShieldTexture = new Identifier(reference.modid,"textures/items/shields/dwarf_shield.png");
    private final Identifier EMShieldTexture = new Identifier(reference.modid,"textures/items/shields/empire_shield.png");
    private final Identifier SKShieldTexture = new Identifier(reference.modid,"textures/items/shields/skaven_shield.png");

    private abstractModshieldModel modelShieldDE;
    private abstractModshieldModel modelShieldEM;
    private abstractModshieldModel modelShieldHE;
    private abstractModshieldModel modelShieldSK;
    private abstractModshieldModel modelShieldDW;
    private ArrayList<abstractModshieldModel> shields;

    public RenderShield(){
        entityModelSet =  MinecraftClient.getInstance().getEntityModelLoader();
        modelShieldDE = new DarkElfshieldmodel(DarkElfshieldmodel.createLayer().createModel());
        modelShieldHE = new HighelfshieldModel(HighelfshieldModel.createLayer().createModel());
        modelShieldEM = new EmpireShieldmodel(EmpireShieldmodel.createLayer().createModel());
        modelShieldSK = new DwarfshieldModel(DwarfshieldModel.createLayer().createModel());
        modelShieldDW = new SkavenShieldModel(SkavenShieldModel.createLayer().createModel());
    }

    private EntityModelLoader entityModelSet;
    public void reload(ResourceManager resourceManager) {
        entityModelSet =  MinecraftClient.getInstance().getEntityModelLoader();
        modelShieldDE = new DarkElfshieldmodel(entityModelSet.getModelPart(Clientside.DEShield));
        modelShieldHE = new HighelfshieldModel(entityModelSet.getModelPart(Clientside.HEShield));
        modelShieldEM = new EmpireShieldmodel(entityModelSet.getModelPart(Clientside.EMShield));
        modelShieldSK = new DwarfshieldModel(entityModelSet.getModelPart(Clientside.DWShield));
        modelShieldDW = new SkavenShieldModel(entityModelSet.getModelPart(Clientside.SKShield));

    }



    @Override
    public void render(ItemStack stack, ModelTransformation.TransformType mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

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

    private void rendershield(abstractModshieldModel model,ItemStack stack, ModelTransformation.TransformType mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay){
        matrices.push();
        matrices.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer ivertexbuilder1 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, model.getLayer(model.gettexture()), false, stack.hasGlint());
        model.render(matrices, ivertexbuilder1, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }




}
