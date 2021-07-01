package warhammermod.Items.melee;


import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import warhammermod.Items.GunBase;
import warhammermod.utils.reference;

public class ShieldTemplate  extends ShieldItem {


    public ShieldTemplate( Properties builder){

        super(builder.durability(1008).tab(reference.warhammer));
        FabricModelPredicateProviderRegistry.register(this, new ResourceLocation("blocking"), (stack, world, entity,i) -> {
            return entity != null && entity.isUsingItem() && (entity.getUseItem() == stack || (entity.getOffhandItem() == stack && hasGun(entity, entity.getMainHandItem()))) ? 1.0F : 0.0F;
        });

    }

    private boolean hasGun(LivingEntity entityIn, ItemStack stack){
        if(entityIn instanceof Player) {
            Player player = (Player) entityIn;
            return stack.getItem() instanceof GunBase && ((GunBase) stack.getItem()).isReadytoFire(stack);
            //|| stack.getItem() instanceof GunSwordTemplate && ((GunSwordTemplate) stack.getItem()).isReadytoFire(stack);
        }return false;
    }


}
