package warhammermod.Datageneration;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import warhammermod.utils.Registry.ItemsInit;
import warhammermod.utils.reference;

import java.util.concurrent.CompletableFuture;

public class moditemtagprovider extends FabricTagProvider.ItemTagProvider {

    /**
     * Constructs a new {@link FabricTagProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link FabricDataOutput} instance
     * @param registryKey
     * @param registriesFuture the backing registry for the tag type
     */
    public moditemtagprovider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.SWORDS).add(ItemsInit.iron_halberd).add(ItemsInit.netherite_halberd).add(ItemsInit.diamond_halberd).add(ItemsInit.gold_halberd).add(ItemsInit.stone_halberd).add(ItemsInit.wooden_halberd).add(ItemsInit.stone_spear).add(ItemsInit.iron_spear).add(ItemsInit.wooden_spear).add(ItemsInit.gold_spear).add(ItemsInit.diamond_spear).add(ItemsInit.netherite_spear).add(ItemsInit.iron_warhammer).add(ItemsInit.wooden_warhammer).add(ItemsInit.stone_warhammer).add(ItemsInit.diamond_warhammer).add(ItemsInit.gold_warhammer).add(ItemsInit.netherite_warhammer).add(ItemsInit.wooden_dagger).add(ItemsInit.stone_dagger).add(ItemsInit.iron_dagger).add(ItemsInit.gold_dagger).add(ItemsInit.diamond_dagger).add(ItemsInit.netherite_dagger).add(ItemsInit.diamond_gunsword).add(ItemsInit.iron_gunsword);
        }
}
