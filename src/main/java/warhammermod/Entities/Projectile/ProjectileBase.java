package warhammermod.Entities.Projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import warhammermod.utils.Clientside;
import warhammermod.utils.EntitySpawnPacket;

public class ProjectileBase extends AbstractArrow {
    public float projectiledamage;
    protected float extradamage;
    protected int knocklevel;
    public float totaldamage;

    public void setpowerDamage(float powerIn){
        extradamage=1.5F*powerIn;
    }
    public void setknockbacklevel(int knockin){
        knocklevel=knockin;
    }

    public float getTotalDamage(){
        return projectiledamage+extradamage;
    }

    public void applyspecialeffect(LivingEntity entity){
    }

    public ProjectileBase(EntityType<? extends ProjectileBase> entityType, Level world) {
        super(entityType, world);
    }
    public ProjectileBase(Level worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(type, shooter, worldin);
        projectiledamage = damageIn;
    }
    public ProjectileBase(EntityType<? extends ProjectileBase> entityType,Level world, LivingEntity owner) {
        super(entityType, owner, world);
    }

    public ProjectileBase(Level world, EntityType<? extends ProjectileBase> entity, double x, double y, double z) {
        super(entity, x, y, z, world);
    }

    protected ItemStack getPickupItem(){return null;}
    protected ItemStack getArrowStack() {
        return null;
    }
    public boolean getIsCritical() {
        return false;
    }

    protected void onHitBlock(BlockHitResult blockHitResult){
        this.playSound(SoundEvents.STONE_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.remove(RemovalReason.DISCARDED);
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();

        Entity entity1 = this.getOwner();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = DamageSource.arrow(this, this);
        } else {
            damagesource = DamageSource.arrow(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastHurtMob(entity);
            }
        }
        float i = getTotalDamage();
        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getRemainingFireTicks();
        if (this.isOnFire() && !flag) {
            entity.setSecondsOnFire(5);
        }

        if (entity.hurt(damagesource, i)) {
            if (flag) {
                return;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                applyspecialeffect(livingentity);
                if (this.knocklevel > 0) {
                    Vec3 vec3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knocklevel * 0.6D);
                    if (vec3d.lengthSqr() > 0.0D) {
                        livingentity.push(vec3d.x, 0.1D, vec3d.z);
                    }
                }
                if (!this.level.isClientSide && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity);
                }

                this.doPostHurtEffects(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof Player && entity1 instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

            }

            this.playSound(SoundEvents.STONE_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.remove(RemovalReason.DISCARDED);
        } else {
            entity.setRemainingFireTicks(j);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot()+180);
            this.yRotO += 180.0F;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                this.remove(RemovalReason.DISCARDED);
            }
        }

    }

    @Override
    public Packet getAddEntityPacket() {
        return EntitySpawnPacket.create(this, Clientside.PacketID);
    }

}
