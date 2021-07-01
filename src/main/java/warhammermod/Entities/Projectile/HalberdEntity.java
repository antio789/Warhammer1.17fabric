package warhammermod.Entities.Projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import warhammermod.utils.Registry.Entityinit;


public class HalberdEntity extends ProjectileBase{
    int fuse=20;
    public HalberdEntity(EntityType<? extends HalberdEntity> entityType, Level p_i50148_2_) {
        super(entityType, p_i50148_2_);
    }
    public HalberdEntity(Level worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(worldin, shooter,damageIn, type);
    }
    public HalberdEntity(Level world, EntityType<? extends ProjectileBase> entity, double x, double y, double z) {
        super( world,entity, x, y, z);
    }
    public HalberdEntity(LivingEntity shooter, Level world, float damage) {
        this(world, shooter, damage, Entityinit.halberdthrust);
    }
    public HalberdEntity(Level world, LivingEntity owner) {
        super(Entityinit.halberdthrust, world, owner);
    }


    /*
        @OnlyIn(Dist.CLIENT)
        public boolean shouldRenderAtSqrDistance(double p_70112_1_) { return false; }
    */
    public void tick()
    {
        super.tick();
        fuse--;
        if(fuse==18){
            this.remove(RemovalReason.DISCARDED);
        }
    }
}
