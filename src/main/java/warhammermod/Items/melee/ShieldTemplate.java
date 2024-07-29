package warhammermod.Items.melee;



import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;
import warhammermod.Items.GunBase;
import warhammermod.utils.reference;

public class ShieldTemplate  extends ShieldItem {


    public ShieldTemplate( Settings builder){
        super(builder.maxDamage(1008));
    }

    private boolean hasGun(LivingEntity entityIn, ItemStack stack){
        if(entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            return stack.getItem() instanceof GunBase && ((GunBase) stack.getItem()).isReadytoFire(stack);
            //|| stack.getItem() instanceof GunSwordTemplate && ((GunSwordTemplate) stack.getItem()).isReadytoFire(stack);
        }return false;
    }


}
