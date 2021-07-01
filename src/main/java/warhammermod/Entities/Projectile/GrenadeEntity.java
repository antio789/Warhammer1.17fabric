package warhammermod.Entities.Projectile;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import warhammermod.utils.Registry.Entityinit;

public class GrenadeEntity extends ProjectileBase{



    public GrenadeEntity(EntityType<? extends GrenadeEntity> p_i50148_1_, Level p_i50148_2_) {
        super(p_i50148_1_, p_i50148_2_);
    }

    public GrenadeEntity(Level worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(worldin, shooter,damageIn, type);
    }
    public GrenadeEntity(Level world, EntityType<? extends ProjectileBase> entity, double x, double y, double z) {
        super( world,entity, x, y, z);
    }

    public GrenadeEntity(LivingEntity shooter, Level world) {
        this(world, shooter, 0, Entityinit.Grenade);
    }



    protected void onHit(HitResult p_70227_1_) {
        if (!this.level.isClientSide()) {
            this.level.explode(null, p_70227_1_.getLocation().x, p_70227_1_.getLocation().y, p_70227_1_.getLocation().z, 2 + knocklevel, Explosion.BlockInteraction.BREAK);
            this.remove(RemovalReason.DISCARDED);

        }
    }
        protected void onHitBlock(BlockHitResult p_230299_1_) {}
        protected void onHitEntity(EntityHitResult p_213868_1_){}



}
