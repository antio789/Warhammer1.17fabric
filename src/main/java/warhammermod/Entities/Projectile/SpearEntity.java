package warhammermod.Entities.Projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import warhammermod.utils.Registry.Entityinit;

public class SpearEntity extends ProjectileBase {
    private ItemStack throwed_spear;
    public SpearEntity(EntityType<? extends SpearEntity> p_i50148_1_, Level p_i50148_2_) {
        super(p_i50148_1_, p_i50148_2_);
    }
    public SpearEntity(Level worldin, LivingEntity shooter, float damageIn, EntityType<? extends SpearEntity> type) {
        super(worldin, shooter,damageIn, type);
    }
    public SpearEntity(Level world, EntityType<? extends SpearEntity> entity, double x, double y, double z) {
        super( world,entity, x, y, z);
    }
    public SpearEntity(LivingEntity shooter, Level world, float damage,ItemStack stack) {
        this(world, shooter, damage, Entityinit.SpearProjectile);
        throwed_spear =stack;

    }


    protected void onHitEntity(EntityHitResult p_213868_1_){
        if(throwed_spear!=null) this.spawnAtLocation(throwed_spear);
        super.onHitEntity(p_213868_1_);

    }

    protected void onHitBlock(BlockHitResult p_230299_1_){
        if(throwed_spear!=null)this.spawnAtLocation(throwed_spear);
        super.onHitBlock(p_230299_1_);
    }

}
