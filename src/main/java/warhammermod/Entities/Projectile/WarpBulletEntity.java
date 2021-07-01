package warhammermod.Entities.Projectile;



import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import warhammermod.utils.Registry.Entityinit;

public class WarpBulletEntity extends ProjectileBase {

    public WarpBulletEntity(Level world){
        super(Entityinit.WarpBullet,world);
    }
    public WarpBulletEntity(EntityType<? extends WarpBulletEntity> p_i50148_1_, Level p_i50148_2_) {
        super(p_i50148_1_, p_i50148_2_);
    }
    public WarpBulletEntity(Level worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(worldin, shooter,damageIn, type);
    }
    public WarpBulletEntity( LivingEntity shooter,Level world, float damageIn) {
        super(world, shooter, damageIn, Entityinit.WarpBullet);

    }






    public void tick()
    {
        super.tick();

        if (this.level.isClientSide() && !this.inGround)
        {
            spawnColoredParticles();
        }
    }


    @Override
    public void applyspecialeffect(LivingEntity entity) {
        if(random.nextFloat()<0.3){
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1));
        }
    }

    private void spawnColoredParticles()
    {
        int i = 65280;
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i & 255) / 255.0D;

            for (int j = 0; j < 5; ++j)
            {
                this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth(), this.getY() +this.random.nextDouble()*(double)this.getBbHeight(), this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth(), d0, d1, d2);
            }
    }

}
