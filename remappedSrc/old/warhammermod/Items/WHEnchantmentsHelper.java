package warhammermod.Items;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.registry.Registry;

import java.util.Iterator;
import java.util.List;

public class WHEnchantmentsHelper {


    public static List<EnchantmentLevelEntry> getAvailableEnchantmentResults(int i, WHCustomenchantements item, boolean bl) {
        List<EnchantmentLevelEntry> list = Lists.newArrayList();
        Iterator var6 = Registry.ENCHANTMENT.iterator();

        while(true) {
            while(true) {
                Enchantment enchantment;
                do {
                    do {
                        do {
                            if (!var6.hasNext()) {
                                return list;
                            }

                            enchantment = (Enchantment)var6.next();
                        } while(enchantment.isTreasure() && !bl);
                    } while(!enchantment.isAvailableForRandomSelection());
                } while(!item.isCorrectEnchantementatTable(enchantment));

                for(int j = enchantment.getMaxLevel(); j > enchantment.getMinLevel() - 1; --j) {
                    if (i >= enchantment.getMinPower(j) && i <= enchantment.getMaxPower(j)) {
                        list.add(new EnchantmentLevelEntry(enchantment, j));
                        break;
                    }
                }
            }
        }
    }


}
