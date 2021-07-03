package warhammermod.Entities.Living.Renders;


import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.Entities.Living.Renders.Layers.DwarfLevellayer;
import warhammermod.Entities.Living.Renders.Models.DwarfModel;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
public class DwarfRenderer extends MobRenderer<DwarfEntity, DwarfModel<DwarfEntity>> {
    private static final ResourceLocation warrior = new ResourceLocation(reference.modid,"textures/entity/dwarf/warrior.png");
    private static final ResourceLocation miner = new ResourceLocation(reference.modid,"textures/entity/dwarf/miner.png");
    private static final ResourceLocation slayer = new ResourceLocation(reference.modid,"textures/entity/dwarf/slayer.png");
    private static final ResourceLocation builder = new ResourceLocation(reference.modid,"textures/entity/dwarf/builder.png");
    private static final ResourceLocation engineer = new ResourceLocation(reference.modid,"textures/entity/dwarf/engineer.png");
    private static final ResourceLocation farmer = new ResourceLocation(reference.modid,"textures/entity/dwarf/farmer.png");
    private static final ResourceLocation lord = new ResourceLocation(reference.modid,"textures/entity/dwarf/lord.png");
    private ResourceLocation[] Dwarf_Textures = {miner,builder,engineer,farmer,slayer,lord,warrior}; //

    public DwarfRenderer(EntityRendererProvider.Context context) {
        super(context, new DwarfModel(context.bakeLayer(Clientside.Dwarf)), 0.25F); //p3 0.5F default
        this.addLayer(new ItemInHandLayer<DwarfEntity, DwarfModel<DwarfEntity>>(this) {
            public void render(PoseStack p_225628_1_, MultiBufferSource p_225628_2_, int p_225628_3_, DwarfEntity p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
                if (p_225628_4_.isAggressive()) {
                    super.render(p_225628_1_, p_225628_2_, p_225628_3_, p_225628_4_, p_225628_5_, p_225628_6_, p_225628_7_, p_225628_8_, p_225628_9_, p_225628_10_);
                }

            }
        });
        this.addLayer(new DwarfLevellayer(this));
    }

    public ResourceLocation getTextureLocation(DwarfEntity entity)
    {
        return Dwarf_Textures[entity.getProfessionID()];
    }

/*
    public int getBlockLight(DwarfEntity entityIn, BlockPos partialTicks) {
        return entityIn.isOnFire() || entityIn.getProfession().equals(DwarfProfession.Miner) ? 15 : entityIn.level.getLightFor(LightType.BLOCK, partialTicks);
    }
*/
    public int getBlockLightLevel(DwarfEntity entity, BlockPos p_225624_2_) {
        return entity.isOnFire() || entity.getProfession().equals(DwarfProfession.Miner)? 15 : entity.level.getBrightness(LightLayer.BLOCK, p_225624_2_);
    }
    protected void scale(DwarfEntity p_225620_1_, PoseStack p_225620_2_, float p_225620_3_) {
        float f = 0.9375F;
        if (p_225620_1_.isBaby()) {
            f = (float)((double)f * 0.5D);
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }

        p_225620_2_.scale(f, f, f);
    }


}
