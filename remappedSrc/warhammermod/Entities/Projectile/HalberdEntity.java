package warhammermod.Entities.Projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import warhammermod.utils.Registry.Entityinit;


public class HalberdEntity extends ProjectileBase{
    int fuse=20;
    public HalberdEntity(EntityType<? extends HalberdEntity> entityType, World p_i50148_2_) {
        super(entityType, p_i50148_2_);
    }
    public HalberdEntity(World worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(worldin, shooter,damageIn, type);
    }
    public HalberdEntity(World world, EntityType<? extends ProjectileBase> entity, double x, double y, double z) {
        super( world,entity, x, y, z);
    }
    public HalberdEntity(LivingEntity shooter, World world, float damage) {
        this(world, shooter, damage, Entityinit.halberdthrust);
    }
    public HalberdEntity(World world, LivingEntity owner) {
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
