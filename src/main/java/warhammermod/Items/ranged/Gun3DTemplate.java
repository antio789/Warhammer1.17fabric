package warhammermod.Items.ranged;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import warhammermod.Items.Render.RenderRepeater;


public class Gun3DTemplate extends GunTemplate implements ItemLike {
    public Gun3DTemplate(Properties properties, Item ammotype, int time, int magsize, float damagein){
        super(properties, ammotype, time, magsize,damagein);
    }
    public void fire(Player player, Level world, ItemStack stack) {
        super.fire(player, world, stack);
        if(world.isClientSide()) RenderRepeater.setrotationangle();

    }

    public static void setCharge(ItemStack stack, int ammo, CompoundTag nbt) {
        CompoundTag compoundnbt = stack.getOrCreateTag();
        compoundnbt.putInt("ammo", ammo);
    }

    private static int setrotationangle(int i){
        if(i==300){
            return 0;
        }
        return (i+=60);
    }


}
