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
import warhammermod.Entities.Projectile.WarpBulletEntity;
import warhammermod.Items.GunBase;
import warhammermod.Items.IReloadItem;

public class WarpgunTemplate extends GunBase implements IReloadItem {
    float damage;

    public WarpgunTemplate(Settings properties, Item ammotype, int time, int magsize, float damagein){
        super(properties, ammotype, time, magsize);
        damage=damagein;
    }

    public void fire(PlayerEntity player, World world, ItemStack stack) {
        if(world.isClient()) {
            for (int k = 0; k < 40; ++k) {
                int i = 65280;
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i & 255) / 255.0D;
                world.addParticle(ParticleTypes.ENTITY_EFFECT, player.getX() + player.getRotationVector().x * 2 + (double) (this.rand.nextFloat() * this.width * 2) - (double) this.width, player.getY() + 0.4 + (double) (this.rand.nextFloat() * this.height), player.getZ() + player.getRotationVector().z * 2 + (double) (this.rand.nextFloat() * this.width * 2) - (double) this.width, d0, d1, d2);
            }
        }
        if(!world.isClient()) {
            world.playSound(null,player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS,1,1.35F/(rand.nextFloat()*0.4F+1.2F)+0.5F);

            WarpBulletEntity bullet = new WarpBulletEntity(player, world, damage);
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
