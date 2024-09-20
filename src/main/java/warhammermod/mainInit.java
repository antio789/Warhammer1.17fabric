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
     * DONE
     *
     * -enchantements
     * modelaction of items
     * rotation of models: to fix as currently it applies to all, see how to change that. ok-ish does not translate outside the clientplayer view.
     * loading time for weapons
     * model 2d in inventory for items
     * rats AI forget to use weapon after reload
     * rats falling through the ground
     * jezzail zoom in when shooting
     * smoke of warpbullet (WitchFactory use this)
     *rats jezzail, ratling gun, sling, shooting code and visuals
     * mobs spawning
     * mobs spawning to test;
     * pegasus breeding fix - can be revamped to use own color/marking system.
     *
     * TO DO
     *
     * lights and street revamp of dwarf village
     * tutorial book
     *
     * IMPROVEMENTS
     *
     * lore books
     * refining mob spawning
     * skaven slave can hold torch + make it illuminate;
     * skaven underground patrol
     * ratling gun reload (update rifleattack to use weapon ammo);
     * rats sounds
     * crafting system weapons
     * castle town for imperial weapons
     * companions
     * smoke when rats fire(put as improvement as it requires sending a packet)
     * dwarfs with rifles(require raycasting to avoid friendly fire)
     * revamping pegasus color coding
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
