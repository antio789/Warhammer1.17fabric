package warhammermod;

import net.fabricmc.api.ModInitializer;
import warhammermod.Enchantements.ModEnchantements;
import warhammermod.utils.Registry.Entityinit;
import warhammermod.utils.Registry.ItemsInit;
import warhammermod.utils.Registry.WHRegistry;


public class mainInit implements ModInitializer {
    /**
     * to do:
     * -enchantements ok?
     * modelaction of items ok?
     * rotation of models: to fix as currently it applies to all, see how to change that.
     */
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ModEnchantements.initialize();
        Entityinit.initializeEntities();
        ItemsInit.initialize();

        WHRegistry.initialize();

        //Spawn.addEntitySpawn();
    }



}
