package warhammermod.Entities.Projectile;


import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import warhammermod.utils.Clientside;
import warhammermod.utils.EntitySpawnPacket;
import warhammermod.utils.Registry.Entityinit;

public class FlameEntity extends Fireball {
    private int limitedlifespan=30;


    public FlameEntity(EntityType<? extends FlameEntity> p_i50160_1_, Level p_i50160_2_) {
        super(p_i50160_1_, p_i50160_2_);
    }

    public FlameEntity(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(Entityinit.Flame, shooter, accelX, accelY, accelZ, worldIn);
    }

    public FlameEntity(Level worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(Entityinit.Flame, x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public FlameEntity(Level world){
        super(Entityinit.Flame,world);
    }



    public void tick()
    {

        --limitedlifespan;
        if(limitedlifespan<=0){remove(RemovalReason.DISCARDED);}
        super.tick();
    }

    protected void onHitEntity(EntityHitResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        if (!this.level.isClientSide) {
            Entity entity = p_213868_1_.getEntity();
            if (!entity.fireImmune()) {
                Entity entity1 = this.getOwner();
                int i = entity.getRemainingFireTicks();
                entity.setSecondsOnFire(5);
                boolean flag = entity.hurt(DamageSource.fireball(this, entity1), 5.0F);
                if (!flag) {
                    entity.setRemainingFireTicks(i);
                } else if (entity1 instanceof LivingEntity) {
                    this.doEnchantDamageEffects((LivingEntity)entity1, entity);
                }
            }

        }
    }
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (entity == null || !(entity instanceof Mob) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                BlockPos blockPos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
                if (this.level.isEmptyBlock(blockPos)) {
                    this.level.setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level, blockPos));
                }
            }

        }
    }

    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level.isClientSide) {
            this.remove(RemovalReason.DISCARDED);
        }

    }


    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    @Override
    public Packet getAddEntityPacket() {
        return EntitySpawnPacket.create(this, Clientside.PacketID);
    }

}
