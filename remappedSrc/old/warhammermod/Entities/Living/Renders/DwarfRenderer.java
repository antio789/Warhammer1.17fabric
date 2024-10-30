package warhammermod.Entities.Living.Renders;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.Entities.Living.Renders.Layers.DwarfLevellayer;
import warhammermod.Entities.Living.Renders.Models.DwarfModel;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
public class DwarfRenderer extends MobEntityRenderer<DwarfEntity, DwarfModel<DwarfEntity>> {
    private static final Identifier warrior = new Identifier(reference.modid,"textures/entity/dwarf/warrior.png");
    private static final Identifier miner = new Identifier(reference.modid,"textures/entity/dwarf/miner.png");
    private static final Identifier slayer = new Identifier(reference.modid,"textures/entity/dwarf/slayer.png");
    private static final Identifier builder = new Identifier(reference.modid,"textures/entity/dwarf/builder.png");
    private static final Identifier engineer = new Identifier(reference.modid,"textures/entity/dwarf/engineer.png");
    private static final Identifier farmer = new Identifier(reference.modid,"textures/entity/dwarf/farmer.png");
    private static final Identifier lord = new Identifier(reference.modid,"textures/entity/dwarf/lord.png");
    private Identifier[] Dwarf_Textures = {miner,builder,engineer,farmer,slayer,lord,warrior}; //

    public DwarfRenderer(EntityRendererFactory.Context context) {
        super(context, new DwarfModel(context.getPart(Clientside.Dwarf)), 0.25F); //p3 0.5F default
        this.addFeature(new HeldItemFeatureRenderer<DwarfEntity, DwarfModel<DwarfEntity>>(this) {
            public void render(MatrixStack p_225628_1_, VertexConsumerProvider p_225628_2_, int p_225628_3_, DwarfEntity p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
                if (p_225628_4_.isAttacking()) {
                    super.render(p_225628_1_, p_225628_2_, p_225628_3_, p_225628_4_, p_225628_5_, p_225628_6_, p_225628_7_, p_225628_8_, p_225628_9_, p_225628_10_);
                }

            }
        });
        this.addFeature(new DwarfLevellayer(this));
    }

    public Identifier getTextureLocation(DwarfEntity entity)
    {
        return Dwarf_Textures[entity.getProfessionID()];
    }

/*
    public int getBlockLight(DwarfEntity entityIn, BlockPos partialTicks) {
        return entityIn.isOnFire() || entityIn.getProfession().equals(DwarfProfession.Miner) ? 15 : entityIn.level.getLightFor(LightType.BLOCK, partialTicks);
    }
*/
    public int getBlockLightLevel(DwarfEntity entity, BlockPos p_225624_2_) {
        return entity.isOnFire() || entity.getProfession().equals(DwarfProfession.Miner)? 15 : entity.world.getLightLevel(LightType.BLOCK, p_225624_2_);
    }
    protected void scale(DwarfEntity p_225620_1_, MatrixStack p_225620_2_, float p_225620_3_) {
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
