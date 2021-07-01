package warhammermod.Items;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public interface WHCustomenchantements {
    Boolean isCorrectEnchantementatTable(Enchantment enchantment);
    Boolean isCorrectEnchantement( EnchantmentCategory enchantment, String ID);

}
