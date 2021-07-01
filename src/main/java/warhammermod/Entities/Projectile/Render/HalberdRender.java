package warhammermod.Entities.Projectile.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import warhammermod.Entities.Projectile.HalberdEntity;
import warhammermod.utils.reference;

@Environment(EnvType.CLIENT)
public class HalberdRender extends EntityRenderer<HalberdEntity> {
    private static final ResourceLocation BULLET_TEXTURE = new ResourceLocation(reference.modid,"textures/entity/bullet.png");


    public HalberdRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(HalberdEntity p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_) {

    }

    public ResourceLocation getTextureLocation(HalberdEntity entity) {
        return BULLET_TEXTURE;
    }

}
