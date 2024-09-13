package warhammermod.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.Heightmap;
import warhammermod.utils.Registry.Entityinit;

public class EntitySpawning {
    public static void SpawnRestriction(){
        SpawnRestriction.register(Entityinit.PEGASUS, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GoatEntity::canSpawn);
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_MOUNTAIN), SpawnGroup.CREATURE,Entityinit.PEGASUS,4,1,3);
    }
    public static void addSpawn(){
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_MOUNTAIN), SpawnGroup.CREATURE,Entityinit.PEGASUS,4,1,3);
    }
}
