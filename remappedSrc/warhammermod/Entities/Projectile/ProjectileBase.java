package warhammermod.Entities.Projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import warhammermod.utils.Clientside;
import warhammermod.utils.EntitySpawnPacket;

public class ProjectileBase extends PersistentProjectileEntity {
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

    public ProjectileBase(EntityType<? extends ProjectileBase> entityType, World world) {
        super(entityType, world);
    }
    public ProjectileBase(World worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(type, shooter, worldin);
        projectiledamage = damageIn;
    }
    public ProjectileBase(EntityType<? extends ProjectileBase> entityType,World world, LivingEntity owner) {
        super(entityType, owner, world);
    }

    public ProjectileBase(World world, EntityType<? extends ProjectileBase> entity, double x, double y, double z) {
        super(entity, x, y, z, world);
    }

    protected ItemStack asItemStack(){return null;}
    protected ItemStack getArrowStack() {
        return null;
    }
    public boolean getIsCritical() {
        return false;
    }

    protected void onBlockHit(BlockHitResult blockHitResult){
        this.playSound(SoundEvents.BLOCK_STONE_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.remove(RemovalReason.DISCARDED);
    }

    protected void onEntityHit(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();

        Entity entity1 = this.getOwner();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = DamageSource.arrow(this, this);
        } else {
            damagesource = DamageSource.arrow(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).onAttacking(entity);
            }
        }
        float i = getTotalDamage();
        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getFireTicks();
        if (this.isOnFire() && !flag) {
            entity.setOnFireFor(5);
        }

        if (entity.damage(damagesource, i)) {
            if (flag) {
                return;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                applyspecialeffect(livingentity);
                if (this.knocklevel > 0) {
                    Vec3d vec3d = this.getVelocity().multiply(1.0D, 0.0D, 1.0D).normalize().multiply((double)this.knocklevel * 0.6D);
                    if (vec3d.lengthSquared() > 0.0D) {
                        livingentity.addVelocity(vec3d.x, 0.1D, vec3d.z);
                    }
                }
                if (!this.world.isClient && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingentity, entity1);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity1, livingentity);
                }

                this.onHit(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)entity1).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
                }

            }

            this.playSound(SoundEvents.BLOCK_STONE_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.remove(RemovalReason.DISCARDED);
        } else {
            entity.setFireTicks(j);
            this.setVelocity(this.getVelocity().multiply(-0.1D));
            this.setYaw(this.getYaw()+180);
            this.prevYaw += 180.0F;
            if (!this.world.isClient && this.getVelocity().lengthSquared() < 1.0E-7D) {
                this.remove(RemovalReason.DISCARDED);
            }
        }

    }

    @Override
    public Packet createSpawnPacket() {
        return EntitySpawnPacket.create(this, Clientside.PacketID);
    }

}
