package warhammermod.Entities.Living.Renders;



import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import warhammermod.Entities.Living.Renders.Layers.SkavenItemInHandLayer;
import warhammermod.Entities.Living.Renders.Models.SkavenModel;
import warhammermod.Entities.Living.SkavenEntity;
import warhammermod.utils.Clientside;
import warhammermod.utils.reference;

import java.util.ArrayList;
import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class SkavenRenderer extends MobRenderer<SkavenEntity, SkavenModel<SkavenEntity>> {
    private static final ResourceLocation slave = new ResourceLocation(reference.modid,"textures/entity/skaven/slave.png");
    private static final ResourceLocation clanrat = new ResourceLocation(reference.modid,"textures/entity/skaven/clanrat.png");
    private static final ResourceLocation stormvermin = new ResourceLocation(reference.modid,"textures/entity/skaven/stormvermin.png");
    private static final ResourceLocation globadier = new ResourceLocation(reference.modid,"textures/entity/skaven/globadier.png");
    private static final ResourceLocation gutter_runner = new ResourceLocation(reference.modid,"textures/entity/skaven/gutter_runner.png");
    private static final ResourceLocation ratling_gunner = new ResourceLocation(reference.modid,"textures/entity/skaven/ratling_gunner.png");
    private ArrayList<ResourceLocation> SKAVEN_TEXTURES = new ArrayList<ResourceLocation>(Arrays.asList(slave,clanrat,stormvermin,gutter_runner,globadier,ratling_gunner));

    public SkavenRenderer(EntityRendererProvider.Context context) {
        super(context, new SkavenModel<>(context.bakeLayer(Clientside.Skaven)), 0.25F); //p3 0.5F default
        this.addLayer(new SkavenItemInHandLayer<>(this));
    }

    protected void scale(SkavenEntity skaven, PoseStack p_225620_2_, float p_225620_3_) {
        float i = skaven.getSkavenSize();
        p_225620_2_.scale(i, i, i);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    public ResourceLocation getTextureLocation(SkavenEntity entity)
    {
        String type = entity.getSkaventype();

        if(type!=null){
        return SKAVEN_TEXTURES.get(entity.getSkavenTypePosition());}
        return slave;

    }


}