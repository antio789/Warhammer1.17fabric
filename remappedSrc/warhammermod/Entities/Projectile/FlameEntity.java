package warhammermod.Entities.Projectile;


import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import warhammermod.utils.Clientside;
import warhammermod.utils.EntitySpawnPacket;
import warhammermod.utils.Registry.Entityinit;

public class FlameEntity extends AbstractFireballEntity {
    private int limitedlifespan=30;


    public FlameEntity(EntityType<? extends FlameEntity> p_i50160_1_, World p_i50160_2_) {
        super(p_i50160_1_, p_i50160_2_);
    }

    public FlameEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(Entityinit.Flame, shooter, accelX, accelY, accelZ, worldIn);
    }

    public FlameEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(Entityinit.Flame, x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public FlameEntity(World world){
        super(Entityinit.Flame,world);
    }



    public void tick()
    {

        --limitedlifespan;
        if(limitedlifespan<=0){remove(RemovalReason.DISCARDED);}
        super.tick();
    }

    protected void onEntityHit(EntityHitResult p_213868_1_) {
        super.onEntityHit(p_213868_1_);
        if (!this.world.isClient) {
            Entity entity = p_213868_1_.getEntity();
            if (!entity.isFireImmune()) {
                Entity entity1 = this.getOwner();
                int i = entity.getFireTicks();
                entity.setOnFireFor(5);
                boolean flag = entity.damage(DamageSource.fireball(this, entity1), 5.0F);
                if (!flag) {
                    entity.setFireTicks(i);
                } else if (entity1 instanceof LivingEntity) {
                    this.applyDamageEffects((LivingEntity)entity1, entity);
                }
            }

        }
    }
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.world.isClient) {
            Entity entity = this.getOwner();
            if (entity == null || !(entity instanceof MobEntity) || this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                BlockPos blockPos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
                if (this.world.isAir(blockPos)) {
                    this.world.setBlockState(blockPos, AbstractFireBlock.getState(this.world, blockPos));
                }
            }

        }
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            this.remove(RemovalReason.DISCARDED);
        }

    }


    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean isCollidable() {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean canHit() {
        return false;
    }

    public boolean damage(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    @Override
    public Packet createSpawnPacket() {
        return EntitySpawnPacket.create(this, Clientside.PacketID);
    }

}
