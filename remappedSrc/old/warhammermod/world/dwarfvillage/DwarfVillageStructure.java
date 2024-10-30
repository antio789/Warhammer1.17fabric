package warhammermod.world.dwarfvillage;


import com.mojang.serialization.Codec;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

import java.util.Random;


public class DwarfVillageStructure extends JigsawStructure {
    public DwarfVillageStructure(Codec<JigsawConfiguration> p_i232001_1_) {
        super(p_i232001_1_, 0, true, true);
    }

    public net.minecraft.world.gen.structure.Structure.StructureStartFactory<JigsawConfiguration> getStartFactory() {
        return (structureFeature, chunkPos, i, l) -> new Start(this, chunkPos, i, l);
    }


    public GenerationStep.Feature step() {
        return GenerationStep.Feature.SURFACE_STRUCTURES;
    }

    public static class Start extends JigsawStructure.FeatureStart{
        public Start(JigsawStructure jigsawFeature, ChunkPos chunkPos, int i, long l) {
            super(jigsawFeature, chunkPos, i, l);
        }

        public void generatePieces(DynamicRegistryManager p_230364_1_, ChunkGenerator chunkGenerator, StructureTemplateManager structureManager, ChunkPos chunkPos, Biome biome, JigsawConfiguration jigsawConfiguration, HeightLimitView levelHeightAccessor) {
            BlockPos blockPos = new BlockPos(chunkPos.getStartX(), 0, chunkPos.getStartZ());
            DwarfVillagePools.init();
            addPieces(p_230364_1_, jigsawConfiguration, PoolStructurePiece::new, chunkGenerator, structureManager, blockPos, this, this.random, true, true, levelHeightAccessor);
        }
    }

    public static void addPieces(DynamicRegistryManager registryAccess, JigsawConfiguration jigsawConfiguration, StructurePoolBasedGenerator.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureTemplateManager structureManager, BlockPos blockPos, StructurePiecesHolder structurePieceAccessor, Random random, boolean bl, boolean bl2, HeightLimitView levelHeightAccessor) {
        ModJigsawManager.generate(registryAccess, jigsawConfiguration, pieceFactory, chunkGenerator, structureManager, blockPos, structurePieceAccessor, random, bl, bl2, levelHeightAccessor);
    }



    

}