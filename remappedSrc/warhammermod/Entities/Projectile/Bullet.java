package warhammermod.Entities.Projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import warhammermod.utils.Registry.Entityinit;

public class Bullet extends  ProjectileBase{
    public Bullet(World world){
        super(Entityinit.Bullet,world);
    }

    public Bullet(EntityType<? extends Bullet> p_i50148_1_, World p_i50148_2_) {
        super(p_i50148_1_, p_i50148_2_);
    }

    public Bullet(World worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(worldin, shooter,damageIn, type);
    }
    public Bullet(World world, EntityType<? extends ProjectileBase> entity, double x, double y, double z) {
        super( world,entity, x, y, z);
    }

    public Bullet(LivingEntity shooter, World world, float damage) {
        this(world, shooter, damage, Entityinit.Bullet);
    }

    public void tick()
    {
        if (this.world.isClient() && !this.inGround)
        {
            this.world.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
        super.tick();
    }

}
