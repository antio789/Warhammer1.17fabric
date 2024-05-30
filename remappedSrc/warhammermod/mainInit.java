package warhammermod;

import net.fabricmc.api.ModInitializer;
import warhammermod.Items.ItemsInit;
import warhammermod.utils.Registry.Entityinit;
import warhammermod.utils.Registry.WHRegistry;
import warhammermod.world.Spawn;


public class mainInit implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ItemsInit.registeritems();
        Entityinit.initializeEntities();
        WHRegistry.registerattributes();
        WHRegistry.registersounds();
        WHRegistry.registersensors();
        WHRegistry.registerprofessions();
        WHRegistry.registerProcessors();
        WHRegistry.registerStructures();
        Spawn.addEntitySpawn();
    }

}
