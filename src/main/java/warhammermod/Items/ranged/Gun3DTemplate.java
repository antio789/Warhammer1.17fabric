package warhammermod.Items.ranged;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import warhammermod.Client.Render.Item.RenderRepeater;
import warhammermod.utils.ModEnchantmentHelper;


public class Gun3DTemplate extends GunTemplate implements ItemConvertible {
    public Gun3DTemplate(Settings properties, Item ammotype, int time, int magsize, float damagein){
        super(properties, ammotype, time, magsize,damagein);
    }
    public void fire(PlayerEntity player, World world, ItemStack stack) {
        super.fire(player, world, stack);

        if(world.isClient()) RenderRepeater.setrotationangle();

    }

    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        super.onStoppedUsing(stack, worldIn, entityLiving, timeLeft);
        if(timetoreload<=getMaxUseTime()-timeLeft && worldIn.isClient()){
            RenderRepeater.setdefaultangle();
        }
    }


}
