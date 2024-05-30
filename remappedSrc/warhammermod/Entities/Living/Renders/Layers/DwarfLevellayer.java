package warhammermod.Entities.Living.Renders.Layers;


import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.utils.reference;
@Environment(EnvType.CLIENT)
public class DwarfLevellayer<T extends DwarfEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Int2ObjectMap<Identifier> LEVEL_LOCATIONS = Util.make(new Int2ObjectOpenHashMap<>(), (p_215348_0_) -> {
        p_215348_0_.put(1, location("stone"));
        p_215348_0_.put(2, location("iron"));
        p_215348_0_.put(3, location("gold"));
        p_215348_0_.put(4, location("emerald"));
        p_215348_0_.put(5, location("diamond"));
    });

    private static Identifier location(String name){
        return new Identifier(reference.modid,"textures/entity/dwarf/belt/"+name+".png");
    }


    public DwarfLevellayer(FeatureRendererContext<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    public void render(MatrixStack p_225628_1_, VertexConsumerProvider p_225628_2_, int p_225628_3_, T p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!p_225628_4_.isInvisible()) {
            DwarfProfession prof = p_225628_4_.getProfession();
            M m = this.getContextModel();
            if (prof != DwarfProfession.Warrior && !p_225628_4_.isBaby()) {
                if (prof != DwarfProfession.Lord) {
                    Identifier resourcelocation2 =  LEVEL_LOCATIONS.get(MathHelper.clamp(p_225628_4_.getProfessionLevel(), 1, LEVEL_LOCATIONS.size()));
                    renderModel(m, resourcelocation2, p_225628_1_, p_225628_2_, p_225628_3_, p_225628_4_, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }


}
