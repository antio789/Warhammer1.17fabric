package warhammermod.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import warhammermod.Entities.Living.SkavenEntity;
import warhammermod.utils.Registry.Entityinit;

public class Spawn {
    public static void addEntitySpawn(){
        BiomeModifications.addSpawn(biomeSelector ->
                        biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.EXTREME_HILLS,
                    MobCategory.MONSTER, Entityinit.SKAVEN, 100, 5, 7);

        BiomeModifications.addSpawn(biomeSelector ->
                        biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.PLAINS ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.FOREST ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.TAIGA ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.SAVANNA ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.SWAMP ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.JUNGLE ||
                                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.MESA,
                MobCategory.MONSTER, Entityinit.SKAVEN, 30, 3, 6);

        SpawnRestrictionAccessor.callRegister(Entityinit.SKAVEN, SpawnPlacements.Type.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SkavenEntity::checkSkavenSpawnRules);


        BiomeModifications.addSpawn(biomeSelector ->
                        biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.EXTREME_HILLS,
                MobCategory.CREATURE, Entityinit.Pegasus, 4, 1, 4);

        SpawnRestrictionAccessor.callRegister(Entityinit.Pegasus, SpawnPlacements.Type.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);


    }
}
