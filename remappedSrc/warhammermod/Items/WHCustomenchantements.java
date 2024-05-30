package warhammermod.Items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;

public interface WHCustomenchantements {
    Boolean isCorrectEnchantementatTable(Enchantment enchantment);
    Boolean isCorrectEnchantement( EnchantmentTarget enchantment, String ID);

}
