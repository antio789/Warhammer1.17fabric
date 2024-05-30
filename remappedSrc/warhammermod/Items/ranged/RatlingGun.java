package warhammermod.Items.ranged;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import warhammermod.Entities.Projectile.WarpBulletEntity;
import warhammermod.Items.AutogunBase;
import warhammermod.Items.ItemsInit;
import warhammermod.Items.Render.RenderRatlingGun;
import warhammermod.Items.WHCustomenchantements;


public class RatlingGun extends AutogunBase implements WHCustomenchantements {

    public RatlingGun(Settings properties, int MagSize, int time) {
        super(properties, ItemsInit.Warpstone,time,MagSize,64, 6);
    }


    public void fire(PlayerEntity player, World world, ItemStack stack){
        if(!world.isClient()) {
            WarpBulletEntity bullet = new WarpBulletEntity(player,world, 11);
            bullet.setPosition(player.getX(), player.getEyeY() - 0.26, player.getZ());
            bullet.setVelocity(player,player.getPitch(), player.getYaw(), 0, 3.5F, 6F);

            int i = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            if (i > 0) {
                bullet.setpowerDamage(i);
            }
            int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack) + 1;
            if (k > 0) {
                bullet.setknockbacklevel(k);
            }

            world.spawnEntity(bullet);
        }else {
            RenderRatlingGun.setrotationangle();
        }
        PlayaSound(world, player);
    }

    public void PlayaSound(World world, PlayerEntity player){
        world.playSound(null,player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS,0.6F,1.55F/(rand.nextFloat()*0.4F+1.2F)+0.5F);
    }


    @Override
    public Boolean isCorrectEnchantementatTable(Enchantment enchantment) {
        return this.isCorrectEnchantement(enchantment.target,enchantment.getTranslationKey());
    }

    @Override
    public Boolean isCorrectEnchantement(EnchantmentTarget enchantment, String ID) {
        return enchantment==EnchantmentTarget.BOW || enchantment.isAcceptableItem(this);
    }
}
