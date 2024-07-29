package warhammermod.Items.ranged;


import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import warhammermod.Entities.Projectile.WarpBulletEntity;
import warhammermod.Items.AutogunBase;
import warhammermod.utils.ModEnchantmentHelper;
import warhammermod.utils.Registry.ItemsInit;


public class RatlingGun extends AutogunBase { //BOW enchantements

    public RatlingGun(Settings properties, int MagSize, int time) {
        super(properties, ItemsInit.Warpstone,time,MagSize,64, 6);
    }


    public void fire(PlayerEntity player, World world, ItemStack stack){
        if(!world.isClient()) {
            WarpBulletEntity bullet = new WarpBulletEntity(player,world, 11,stack,findAmmo(player));
            bullet.setPosition(player.getX(), player.getEyeY() - 0.26, player.getZ());
            bullet.setVelocity(player,player.getPitch(), player.getYaw(), 0, 3.5F, 6F);

            int i = ModEnchantmentHelper.getLevel(world, stack, Enchantments.POWER);
            if (i > 0) {
                bullet.setpowerDamage(i);
            }
            int k = ModEnchantmentHelper.getLevel(world, stack, Enchantments.PUNCH) + 1;
            if (k > 0) {
                bullet.setknockbacklevel(k);
            }

            world.spawnEntity(bullet);
        }
        /*
        else {
            RenderRatlingGun.setrotationangle();
        }
        */
        PlayaSound(world, player);
    }

    public void PlayaSound(World world, PlayerEntity player){
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, player.getSoundCategory(), 0.6f, 1.55F/(rand.nextFloat()*0.4F+1.2F)+0.5F);
    }
}
