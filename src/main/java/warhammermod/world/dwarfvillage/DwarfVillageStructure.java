package warhammermod.world.dwarfvillage;



import com.mojang.serialization.Codec;
import com.sun.jna.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.Random;


public class DwarfVillageStructure extends JigsawFeature {
    public DwarfVillageStructure(Codec<JigsawConfiguration> p_i232001_1_) {
        super(p_i232001_1_, 0, true, true);
    }

    public StructureFeature.StructureStartFactory<JigsawConfiguration> getStartFactory() {
        return (structureFeature, chunkPos, i, l) -> new Start(this, chunkPos, i, l);
    }


    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public static class Start extends JigsawFeature.FeatureStart{
        public Start(JigsawFeature jigsawFeature, ChunkPos chunkPos, int i, long l) {
            super(jigsawFeature, chunkPos, i, l);
        }

        public void generatePieces(RegistryAccess p_230364_1_, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, JigsawConfiguration jigsawConfiguration, LevelHeightAccessor levelHeightAccessor) {
            BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ());
            DwarfVillagePools.init();
            addPieces(p_230364_1_, jigsawConfiguration, PoolElementStructurePiece::new, chunkGenerator, structureManager, blockPos, this, this.random, true, true, levelHeightAccessor);
        }
    }

    public static void addPieces(RegistryAccess registryAccess, JigsawConfiguration jigsawConfiguration, JigsawPlacement.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureManager structureManager, BlockPos blockPos, StructurePieceAccessor structurePieceAccessor, Random random, boolean bl, boolean bl2, LevelHeightAccessor levelHeightAccessor) {
        ModJigsawManager.addPieces(registryAccess, jigsawConfiguration, pieceFactory, chunkGenerator, structureManager, blockPos, structurePieceAccessor, random, bl, bl2, levelHeightAccessor);
    }



    

}