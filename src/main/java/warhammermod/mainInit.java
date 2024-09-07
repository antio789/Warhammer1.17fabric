package warhammermod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import warhammermod.Enchantements.ModEnchantements;
import warhammermod.utils.ItemFiringPayload;
import warhammermod.utils.Registry.Entityinit;
import warhammermod.utils.Registry.ItemsInit;
import warhammermod.utils.Registry.WHRegistry;
import warhammermod.utils.reference;


public class mainInit implements ModInitializer {
    /**
     * to do:
     * -enchantements ok?
     * modelaction of items ok?
     * rotation of models: to fix as currently it applies to all, see how to change that. ok-ish does not translate outside the clientplayer view.
     * loading time for weapons
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
        PayloadTypeRegistry.playS2C().register(ItemFiringPayload.ID, ItemFiringPayload.CODEC);
        //Spawn.addEntitySpawn();
    }

    public static final Identifier HIGHLIGHT_PACKET_ID = Identifier.of(reference.modid, "shooting_packet");



}
