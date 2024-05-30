package warhammermod.world.dwarfvillage;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
import net.minecraft.util.Identifier;
import warhammermod.utils.reference;

public class DwarfVillagePools {
    public static final StructurePool START = StructurePools.register(new StructurePool(new Identifier("warhammermod:dwarf_village/town_centers"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("town_centers/dwarf_meeting_point_1").toString(), StructureProcessorLists.EMPTY), 1),Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("town_centers/tavern").toString(), StructureProcessorLists.EMPTY), 3)), StructurePool.Projection.RIGID));

    public static void init() {
    }

    public static final StructureProcessorList pathchange =  new StructureProcessorList(ImmutableList.of(
            new RuleStructureProcessor(ImmutableList.of(
            new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.45F), AlwaysTrueRuleTest.INSTANCE, Blocks.ANDESITE.getDefaultState()),
            new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.GRAVEL,0.3F), AlwaysTrueRuleTest.INSTANCE, Blocks.ANDESITE.getDefaultState())))));


    static {

         StructurePools.register(new StructurePool(new Identifier("warhammermod:dwarf_village/streets"), new Identifier("warhammermod:dwarf_village/terminators"), ImmutableList.of(Pair.of(StructurePoolElement.ofLegacySingle("warhammermod:dwarf_village/streets/corner_01"), 2),  Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/streets/corner_02", pathchange), 2),  Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/streets/corner_03", pathchange), 2),  Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/streets/straight_01", pathchange), 2), Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/streets/straight_02", pathchange), 2), Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/streets/crossroad_01", pathchange), 2), Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/streets/crossroad_02", pathchange), 1),Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/streets/turn_01",pathchange), 3)), StructurePool.Projection.TERRAIN_MATCHING));

         StructurePools.register(new StructurePool(new Identifier("warhammermod:dwarf_village/houses"), new Identifier("dwarf_village/terminators"),ImmutableList.of(
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("houses/dwarf_small_house_1").toString(), StructureProcessorLists.EMPTY), 3),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("houses/dwarf_small_house_2").toString(), StructureProcessorLists.EMPTY), 3),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("houses/dwarf_medium_house_1").toString(), StructureProcessorLists.EMPTY), 2),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("houses/dwarf_builder_house_1").toString(), StructureProcessorLists.EMPTY), 2),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("houses/dwarf_engineer_house_1").toString(), StructureProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("houses/dwarf_farmer_house_1").toString(), StructureProcessorLists.EMPTY), 2),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("houses/dwarf_miner_house_1").toString(), StructureProcessorLists.EMPTY), 3),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle(location("houses/dwarf_slayer_house_1").toString(), StructureProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.ofEmpty(), 4)), StructurePool.Projection.RIGID));

        StructurePools.register(new StructurePool(new Identifier("warhammermod:dwarf_village/dwarf"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofLegacySingle("warhammermod:dwarf_village/dwarf/dwarf_villager"), 1)), StructurePool.Projection.RIGID));


        StructurePools.register(new StructurePool(new Identifier("warhammermod:dwarf_village/terminators"), new Identifier("empty"), ImmutableList.of(
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/terminators/terminator_01", StructureProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/terminators/terminator_02", StructureProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/terminators/terminator_03", StructureProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.ofProcessedLegacySingle("warhammermod:dwarf_village/terminators/terminator_04", StructureProcessorLists.EMPTY), 1)), StructurePool.Projection.TERRAIN_MATCHING));

         }


    public static Identifier location(String name)
    {
        return new Identifier(reference.modid, "dwarf_village/"+name);
    }

}