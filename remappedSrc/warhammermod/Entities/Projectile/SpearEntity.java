package warhammermod.Entities.Projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import warhammermod.utils.Registry.Entityinit;

public class SpearEntity extends ProjectileBase {
    private ItemStack throwed_spear;
    public SpearEntity(EntityType<? extends SpearEntity> p_i50148_1_, World p_i50148_2_) {
        super(p_i50148_1_, p_i50148_2_);
    }
    public SpearEntity(World worldin, LivingEntity shooter, float damageIn, EntityType<? extends SpearEntity> type) {
        super(worldin, shooter,damageIn, type);
    }
    public SpearEntity(World world, EntityType<? extends SpearEntity> entity, double x, double y, double z) {
        super( world,entity, x, y, z);
    }
    public SpearEntity(LivingEntity shooter, World world, float damage,ItemStack stack) {
        this(world, shooter, damage, Entityinit.SpearProjectile);
        throwed_spear =stack;

    }


    protected void onEntityHit(EntityHitResult p_213868_1_){
        if(throwed_spear!=null) this.dropStack(throwed_spear);
        super.onEntityHit(p_213868_1_);

    }

    protected void onBlockHit(BlockHitResult p_230299_1_){
        if(throwed_spear!=null)this.dropStack(throwed_spear);
        super.onBlockHit(p_230299_1_);
    }

}
