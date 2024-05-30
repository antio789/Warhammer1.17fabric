package warhammermod.utils.Registry;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.poi.PointOfInterestType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.AImanager.Data.EntityAttributes;
import warhammermod.Entities.Living.AImanager.sensor.DwarfHostilesSensor;
import warhammermod.Entities.Living.AImanager.sensor.LordLastSeenSensor;
import warhammermod.Entities.Living.AImanager.sensor.WHSecondaryPositionSensor;
import warhammermod.Entities.Living.AImanager.sensor.dwarfBabiesSensor;
import warhammermod.Entities.Living.SkavenEntity;
import warhammermod.Items.ItemsInit;
import warhammermod.mixin.accesssensor;
import warhammermod.utils.reference;
import warhammermod.world.dwarfvillage.DwarfVillagePools;
import warhammermod.world.dwarfvillage.DwarfVillageStructure;

public class WHRegistry {
    public static void registerattributes(){
        FabricDefaultAttributeRegistry.register(Entityinit.Pegasus, HorseEntity.createBaseHorseAttributes());
        FabricDefaultAttributeRegistry.register(Entityinit.SKAVEN, SkavenEntity.createHostileAttributes());
        FabricDefaultAttributeRegistry.register(Entityinit.DWARF, EntityAttributes.registerDwarfTypesattributes());
    }


    public static final Identifier ratambientID = new Identifier(reference.modid,"entity.skaven.ambient");
    public static SoundEvent ratambient = new SoundEvent(ratambientID);
    public static final Identifier ratdeathID = new Identifier(reference.modid,"entity.skaven.death");
    public static SoundEvent ratdeath = new SoundEvent(ratambientID);
    public static final Identifier rathurtID = new Identifier(reference.modid,"entity.skaven.hurt");
    public static SoundEvent rathurt = new SoundEvent(ratambientID);
    public static final Identifier flameID = new Identifier(reference.modid,"flamethrower.sound");
    public static SoundEvent flame = new SoundEvent(ratambientID);

    public static void registersounds(){
        Registry.register(Registry.SOUND_EVENT,ratambientID,ratambient);
        Registry.register(Registry.SOUND_EVENT,ratdeathID,ratdeath);
        Registry.register(Registry.SOUND_EVENT,rathurtID,rathurt);
        Registry.register(Registry.SOUND_EVENT,flameID,flame);
    }

    public static SensorType<WHSecondaryPositionSensor> SECONDARY_POIS;
    public static final Identifier SECONDARY_POIS_id = new Identifier(reference.modid,"poissecond");
    public static SensorType<LordLastSeenSensor> Lord_LastSeen;
    public static final Identifier Lord_LastSeenid = new Identifier(reference.modid,"lordlasrseen");
    public static SensorType<dwarfBabiesSensor> VISIBLE_VILLAGER_BABIES;
    public static final Identifier VISIBLE_VILLAGER_BABIESid = new Identifier(reference.modid,"visiblebabydwarf");
    public static SensorType<DwarfHostilesSensor> Hostiles;
    public static final Identifier Hostilesid = new Identifier(reference.modid,"hostiles");

    public static void registersensors(){
        SECONDARY_POIS= Registry.register(Registry.SENSOR_TYPE,SECONDARY_POIS_id, accesssensor.init(WHSecondaryPositionSensor::new));
        Lord_LastSeen= Registry.register(Registry.SENSOR_TYPE,Lord_LastSeenid,accesssensor.init(LordLastSeenSensor::new));
        VISIBLE_VILLAGER_BABIES= Registry.register(Registry.SENSOR_TYPE,VISIBLE_VILLAGER_BABIESid,accesssensor.init(dwarfBabiesSensor::new));
        Hostiles= Registry.register(Registry.SENSOR_TYPE,Hostilesid,accesssensor.init(DwarfHostilesSensor::new));
    }

    public static void registerProcessors() {
        Registry.register(BuiltinRegistries.PROCESSOR_LIST, new Identifier(reference.modid, "pathschange"), DwarfVillagePools.pathchange);
    }

    private static final Structure<JigsawConfiguration> MY_STRUCTURE = new DwarfVillageStructure(JigsawConfiguration.CODEC);
    private static final ConfiguredStructureFeature<?, ?> MY_CONFIGURED = MY_STRUCTURE.configured(new JigsawConfiguration(() -> {
        return DwarfVillagePools.START;
    }, 6));
    public static final Identifier VillageID = new Identifier(reference.modid,"dwarfvillage");

    public static void registerStructures() {
        RegistryKey<ConfiguredStructureFeature<?,?>> Dwarfvillage = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, VillageID);
        FabricStructureBuilder.create(new Identifier(reference.modid, "dwarf_village"), MY_STRUCTURE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES).superflatFeature(MY_STRUCTURE.configured(new JigsawConfiguration(() -> {
            return DwarfVillagePools.START;
        }, 6)))
                .defaultConfig(12, 6, 12345)
                .adjustsSurface()
                .register();

        BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,Dwarfvillage.getValue(), MY_CONFIGURED);
        BiomeModifications.addStructure( biomeSelector ->
                biomeSelector.getBiome().getBiomeCategory() == Biome.BiomeCategory.EXTREME_HILLS,Dwarfvillage);
    }



    public static void registerprofessions(){
        DwarfProfession.Warrior = new DwarfProfession("warrior", 6, PointOfInterestType.UNEMPLOYED, null, Items.IRON_AXE, ItemsInit.Dwarf_shield);
        DwarfProfession.Miner = new DwarfProfession("miner", 0, PointOfInterestType.ARMORER, null, Items.IRON_PICKAXE);
        DwarfProfession.Builder = new DwarfProfession("builder", 1, PointOfInterestType.MASON, SoundEvents.ENTITY_VILLAGER_WORK_MASON,ItemsInit.iron_warhammer);
        DwarfProfession.Engineer = new DwarfProfession("engineer", 2, PointOfInterestType.TOOLSMITH, SoundEvents.ENTITY_VILLAGER_WORK_ARMORER, Items.IRON_AXE,ItemsInit.Dwarf_shield);
        DwarfProfession.FARMER = new DwarfProfession("farmer", 3, PointOfInterestType.FARMER, ImmutableSet.of(Items.WHEAT, Items.WHEAT_SEEDS), ImmutableSet.of(Blocks.FARMLAND), SoundEvents.ENTITY_VILLAGER_WORK_FARMER, Items.IRON_AXE, ItemStack.EMPTY.getItem());
        DwarfProfession.Slayer = new DwarfProfession("slayer", 4, PointOfInterestType.BUTCHER, SoundEvents.ENTITY_VILLAGER_WORK_BUTCHER, Items.IRON_AXE, Items.IRON_AXE);
        DwarfProfession.Lord = new DwarfProfession("lord", 5, PointOfInterestType.NITWIT,  (SoundEvent)null,ItemsInit.diamond_warhammer,ItemsInit.Dwarf_shield);
    }

}
