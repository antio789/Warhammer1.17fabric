package warhammermod.Items.ranged;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import warhammermod.Entities.Projectile.Bullet;
import warhammermod.Items.GunBase;


public class GunTemplate extends GunBase {
    float damage;

    public GunTemplate(Settings properties, Item ammotype, int time, int magsize, float damagein){
        super(properties, ammotype, time, magsize);
        damage=damagein;
    }

    public void fire(PlayerEntity player, World world, ItemStack stack) {
        if(world.isClient()) {
            for (int k = 0; k < 40; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getX() + player.getRotationVector().x * 2 + (double) (this.rand.nextFloat() * this.width * 2) - (double) this.width, player.getY() + 0.4 + (double) (this.rand.nextFloat() * this.height), player.getZ() + player.getRotationVector().z * 2 + (double) (this.rand.nextFloat() * this.width * 2) - (double) this.width, d2, d0, d1);
            }
        }
        if(!world.isClient()) {
            world.playSound(null,player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS,1,1.35F/(rand.nextFloat()*0.4F+1.2F)+0.5F);

            Bullet bullet = new Bullet(player, world, damage);
            bullet.setPosition(player.getX(), player.getEyeY() - 0.26, player.getZ());
            bullet.setVelocity(player,player.getPitch(), player.getYaw(), 0, 3.5F, 0.3F);

            int i = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            if (i > 0) {
                bullet.setpowerDamage(i);
            }
            int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack) + 1;
            if (k > 0) {
                bullet.setknockbacklevel(k);
            }

            world.spawnEntity(bullet);
            if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                bullet.setOnFireFor(100);
            }

            stack.damage(1, player, (p_220009_1_) -> {
                p_220009_1_.sendToolBreakStatus(player.getActiveHand());
            });
        }
    }

}
