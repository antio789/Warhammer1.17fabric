package warhammermod.Entities.Projectile;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import warhammermod.utils.Registry.Entityinit;

public class GrenadeEntity extends ProjectileBase{



    public GrenadeEntity(EntityType<? extends GrenadeEntity> p_i50148_1_, World p_i50148_2_) {
        super(p_i50148_1_, p_i50148_2_);
    }

    public GrenadeEntity(World worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(worldin, shooter,damageIn, type);
    }
    public GrenadeEntity(World world, EntityType<? extends ProjectileBase> entity, double x, double y, double z) {
        super( world,entity, x, y, z);
    }

    public GrenadeEntity(LivingEntity shooter, World world) {
        this(world, shooter, 0, Entityinit.Grenade);
    }



    protected void onCollision(HitResult p_70227_1_) {
        if (!this.world.isClient()) {
            this.world.createExplosion(null, p_70227_1_.getPos().x, p_70227_1_.getPos().y, p_70227_1_.getPos().z, 2 + knocklevel, Explosion.DestructionType.BREAK);
            this.remove(RemovalReason.DISCARDED);

        }
    }
        protected void onBlockHit(BlockHitResult p_230299_1_) {}
        protected void onEntityHit(EntityHitResult p_213868_1_){}



}
