package warhammermod.world.dwarfvillage;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen.RuleEntry;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import warhammermod.utils.reference;

public class DwarfVillagePools {
    public static final StructureTemplatePool START = Pools.register(new StructureTemplatePool(new ResourceLocation("warhammermod:dwarf_village/town_centers"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.legacy(location("town_centers/dwarf_meeting_point_1").toString(), ProcessorLists.EMPTY), 1),Pair.of(StructurePoolElement.legacy(location("town_centers/tavern").toString(), ProcessorLists.EMPTY), 3)), StructureTemplatePool.Projection.RIGID));

    public static void init() {
    }

    public static final StructureProcessorList pathchange =  new StructureProcessorList(ImmutableList.of(
            new RuleProcessor(ImmutableList.of(
            new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.45F), AlwaysTrueTest.INSTANCE, Blocks.ANDESITE.defaultBlockState()),
            new ProcessorRule(new RandomBlockMatchTest(Blocks.GRAVEL,0.3F), AlwaysTrueTest.INSTANCE, Blocks.ANDESITE.defaultBlockState())))));


    static {

         Pools.register(new StructureTemplatePool(new ResourceLocation("warhammermod:dwarf_village/streets"), new ResourceLocation("warhammermod:dwarf_village/terminators"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/streets/corner_01"), 2),  Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/streets/corner_02", pathchange), 2),  Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/streets/corner_03", pathchange), 2),  Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/streets/straight_01", pathchange), 2), Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/streets/straight_02", pathchange), 2), Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/streets/crossroad_01", pathchange), 2), Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/streets/crossroad_02", pathchange), 1),Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/streets/turn_01",pathchange), 3)), StructureTemplatePool.Projection.TERRAIN_MATCHING));

         Pools.register(new StructureTemplatePool(new ResourceLocation("warhammermod:dwarf_village/houses"), new ResourceLocation("dwarf_village/terminators"),ImmutableList.of(
                 Pair.of(StructurePoolElement.legacy(location("houses/dwarf_small_house_1").toString(), ProcessorLists.EMPTY), 3),
                 Pair.of(StructurePoolElement.legacy(location("houses/dwarf_small_house_2").toString(), ProcessorLists.EMPTY), 3),
                 Pair.of(StructurePoolElement.legacy(location("houses/dwarf_medium_house_1").toString(), ProcessorLists.EMPTY), 2),
                 Pair.of(StructurePoolElement.legacy(location("houses/dwarf_builder_house_1").toString(), ProcessorLists.EMPTY), 2),
                 Pair.of(StructurePoolElement.legacy(location("houses/dwarf_engineer_house_1").toString(), ProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.legacy(location("houses/dwarf_farmer_house_1").toString(), ProcessorLists.EMPTY), 2),
                 Pair.of(StructurePoolElement.legacy(location("houses/dwarf_miner_house_1").toString(), ProcessorLists.EMPTY), 3),
                 Pair.of(StructurePoolElement.legacy(location("houses/dwarf_slayer_house_1").toString(), ProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.empty(), 4)), StructureTemplatePool.Projection.RIGID));

        Pools.register(new StructureTemplatePool(new ResourceLocation("warhammermod:dwarf_village/dwarf"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/dwarf/dwarf_villager"), 1)), StructureTemplatePool.Projection.RIGID));


        Pools.register(new StructureTemplatePool(new ResourceLocation("warhammermod:dwarf_village/terminators"), new ResourceLocation("empty"), ImmutableList.of(
                 Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/terminators/terminator_01", ProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/terminators/terminator_02", ProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/terminators/terminator_03", ProcessorLists.EMPTY), 1),
                 Pair.of(StructurePoolElement.legacy("warhammermod:dwarf_village/terminators/terminator_04", ProcessorLists.EMPTY), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));

         }


    public static ResourceLocation location(String name)
    {
        return new ResourceLocation(reference.modid, "dwarf_village/"+name);
    }

}