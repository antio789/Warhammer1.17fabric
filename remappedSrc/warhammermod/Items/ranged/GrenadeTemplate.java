package warhammermod.Items.ranged;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import warhammermod.Entities.Projectile.GrenadeEntity;
import warhammermod.Items.GunBase;
import warhammermod.Items.WHCustomenchantements;


public class GrenadeTemplate extends GunBase implements WHCustomenchantements {


    public GrenadeTemplate(Settings properties, Item ammotype, int time, int magsize){
        super(properties, ammotype, time, magsize);
    }


    public void fire(PlayerEntity player, World world, ItemStack stack){
         if(world.isClient()) {
            for (int k = 0; k < 40; ++k) {

                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getX() + player.getRotationVector().x * 2 + (double) (this.rand.nextFloat() * this.width * 2) - (double) this.width, player.getY() + 0.4 + (double) (this.rand.nextFloat() * this.height), player.getZ() + player.getRotationVector().z * 2 + (double) (this.rand.nextFloat() * this.width * 2) - (double) this.width, d2, d0, d1);
            }
        }
        if(!world.isClient()) {
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0F, 1.35F / (rand.nextFloat() * 0.4F + 1.2F) + 0.5F);

            GrenadeEntity bullet = new GrenadeEntity(player, world);


            int i = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            bullet.setVelocity(player,player.getPitch(), player.getYaw(), 0, 0.8F + 0.13F * i, 0.6F);

            int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack) + 1;
            if (k > 0) {
                bullet.setknockbacklevel(k);
            }

            world.spawnEntity(bullet);

            stack.damage(1, player, (p_220009_1_) -> {
                p_220009_1_.sendToolBreakStatus(player.getActiveHand());
            });
        }
    }


    private final String[] types={Enchantments.POWER.getTranslationKey(), Enchantments.PUNCH.getTranslationKey(), Enchantments.UNBREAKING.getTranslationKey(), Enchantments.MENDING.getTranslationKey()};


    @Override
    public Boolean isCorrectEnchantementatTable(Enchantment enchantment) {
        return this.isCorrectEnchantement(enchantment.target,enchantment.getTranslationKey());
    }

    @Override
    public Boolean isCorrectEnchantement(EnchantmentTarget enchantment, String ID) {
        for(String type:types){
            if(type.equals(ID))return true;
        }return false;
    }
}
