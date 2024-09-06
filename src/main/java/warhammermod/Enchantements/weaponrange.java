package warhammermod.Enchantements;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import warhammermod.utils.reference;

public class weaponrange {
    public static final RegistryKey<Enchantment> FROST = of("range");

    private static RegistryKey<Enchantment> of(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(reference.modid, name));
    }

    public static void initialize() {
    }
}
