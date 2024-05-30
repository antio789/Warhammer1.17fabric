package warhammermod.Items.ranged;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import warhammermod.Entities.Projectile.FlameEntity;
import warhammermod.Items.AutogunBase;
import warhammermod.Items.WHCustomenchantements;
import warhammermod.utils.Registry.WHRegistry;


public class DrakeGunTemplate extends AutogunBase implements WHCustomenchantements {

    public DrakeGunTemplate(Settings properties, int MagSize, int time) {
        //super(properties,MagSize,1,time, Items.BLAZE_ROD.getName().toString(),5);
        super(properties, Items.BLAZE_ROD, time, MagSize,1,4);
    }

    public void fire(PlayerEntity player, World world, ItemStack stack){
        world.playSound(null, player.getX(), player.getY(), player.getZ(), WHRegistry.flame, SoundCategory.PLAYERS, 0.3F, 1.05F+(rand.nextFloat()+rand.nextFloat())*0.1F);

        if(!world.isClient()){
            FlameEntity smallfireballentity = new FlameEntity(world, player, player.getRotationVector().x*5 + rand.nextGaussian() * 0.1, player.getRotationVector().y*5, player.getRotationVector().z*5 + rand.nextGaussian() * 0.1);
            smallfireballentity.setPosition(player.getX(), (int)(player.getEyeY()- 0.35F), player.getZ());
            world.spawnEntity(smallfireballentity);
        }

    }
    private final String[] types={Enchantments.UNBREAKING.getTranslationKey(), Enchantments.MENDING.getTranslationKey()};

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
