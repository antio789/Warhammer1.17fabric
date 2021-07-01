package warhammermod.Entities.Projectile;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import warhammermod.utils.Registry.Entityinit;

public class ShotEntity extends ProjectileBase {
    public BlockPos Playerpos;
    private int timer;
    public ShotEntity(Level world) {
        super(Entityinit.Shotentity, world);
    }

    public ShotEntity(EntityType<? extends ShotEntity> p_i50148_1_, Level p_i50148_2_) {
        super(p_i50148_1_, p_i50148_2_);
    }

    public ShotEntity(Level worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(worldin, shooter, damageIn, type);
    }

    public ShotEntity(Level world, EntityType<? extends ProjectileBase> entity, double x, double y, double z) {
        super(world, entity, x, y, z);
    }

    public ShotEntity(LivingEntity shooter, Level world, float damage, BlockPos blockPos) {
        this(world, shooter, damage, Entityinit.Shotentity);
        Playerpos = blockPos;
    }



    protected void onHitEntity(EntityHitResult entityhit) {
        if(modifydamage(timer)) {
            super.onHitEntity(entityhit);
        }else this.remove(RemovalReason.DISCARDED);

    }

    public void tick()
    {
        super.tick();
        timer++;
    }

    public boolean modifydamage(double distance){
        double modifier = damagemodifier(distance);
        projectiledamage*=modifier;
        extradamage*=modifier;
        return !(modifier==0);
    }

    public double damagemodifier(double X){
        if(X==0)X=1;
        return Math.max( -0.3*X+1.3,0);
    }
}
