package warhammermod.Items.ranged;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class Gun3DTemplate extends GunTemplate implements ItemConvertible {
    public Gun3DTemplate(Settings properties, Item ammotype, int time, int magsize, float damagein){
        super(properties, ammotype, time, magsize,damagein);
    }
    public void fire(PlayerEntity player, World world, ItemStack stack) {
        super.fire(player, world, stack);
        /*
        if(world.isClient()) RenderRepeater.setrotationangle();
*/
    }
    private static int setrotationangle(int i){
        if(i==300){
            return 0;
        }
        return (i+=60);
    }


}
