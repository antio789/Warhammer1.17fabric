package warhammermod.Items;


import net.minecraft.item.ItemStack;

public interface IReloadItem {
    int getTimetoreload();
    boolean isReadytoFire(ItemStack stack);
}