package warhammermod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import warhammermod.utils.Registry.ItemsInit;
import warhammermod.utils.Registry.Entityinit;
import warhammermod.utils.Registry.WHRegistry;
import warhammermod.utils.reference;

import java.io.InputStream;


public class mainInit implements ModInitializer {
    /**
     * to do:
     * -enchantements
     * modelaction of items
     * rotation of models
     */
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ItemsInit.initialize();
        Entityinit.initializeEntities();
        WHRegistry.initialize();

        //Spawn.addEntitySpawn();
    }



}
