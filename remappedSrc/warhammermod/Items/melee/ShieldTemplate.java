package warhammermod.Items.melee;


import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;
import warhammermod.Items.GunBase;
import warhammermod.utils.reference;

public class ShieldTemplate  extends ShieldItem {


    public ShieldTemplate( Settings builder){

        super(builder.maxDamage(1008).tab(reference.warhammer));
        FabricModelPredicateProviderRegistry.register(this, new Identifier("blocking"), (stack, world, entity,i) -> {
            return entity != null && entity.isUsingItem() && (entity.getActiveItem() == stack || (entity.getOffHandStack() == stack && hasGun(entity, entity.getMainHandStack()))) ? 1.0F : 0.0F;
        });

    }

    private boolean hasGun(LivingEntity entityIn, ItemStack stack){
        if(entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            return stack.getItem() instanceof GunBase && ((GunBase) stack.getItem()).isReadytoFire(stack);
            //|| stack.getItem() instanceof GunSwordTemplate && ((GunSwordTemplate) stack.getItem()).isReadytoFire(stack);
        }return false;
    }


}
