package warhammermod.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import warhammermod.Entities.Living.SkavenEntity;
import warhammermod.utils.Registry.Entityinit;

public class Spawn {
    public static void addEntitySpawn(){
        BiomeModifications.addSpawn(biomeSelector ->
                        biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.EXTREME_HILLS,
                    SpawnGroup.MONSTER, Entityinit.SKAVEN, 100, 5, 7);

        BiomeModifications.addSpawn(biomeSelector ->
                        biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.PLAINS ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.FOREST ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.TAIGA ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.SAVANNA ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.SWAMP ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.JUNGLE ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.MESA,
                SpawnGroup.MONSTER, Entityinit.SKAVEN, 30, 3, 6);

        SpawnRestrictionAccessor.callRegister(Entityinit.SKAVEN, SpawnRestriction.Location.ON_GROUND,  Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SkavenEntity::checkSkavenSpawnRules);


        BiomeModifications.addSpawn(biomeSelector ->
                        biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.EXTREME_HILLS,
                SpawnGroup.CREATURE, Entityinit.Pegasus, 4, 1, 4);

        SpawnRestrictionAccessor.callRegister(Entityinit.Pegasus, SpawnRestriction.Location.ON_GROUND,  Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);


    }
}
