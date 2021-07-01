package warhammermod.world.dwarfvillage;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.sun.jna.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.*;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ModJigsawManager extends JigsawPlacement {
    /**
     *
     * very lazy fix were I replace minecraft with warhammermod: I don't know why it is the case in the first place but I already took way too much time trying to figure it out
     */
    static final Logger LOGGER = LogManager.getLogger();

    public static void addPieces(RegistryAccess registryAccess, JigsawConfiguration jigsawConfiguration, JigsawPlacement.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureManager structureManager, BlockPos blockPos, StructurePieceAccessor structurePieceAccessor, Random random, boolean bl, boolean bl2, LevelHeightAccessor levelHeightAccessor) {
        StructureFeature.bootstrap();
        List<PoolElementStructurePiece> list = Lists.newArrayList();
        Registry<StructureTemplatePool> registry = registryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation = Rotation.getRandom(random);
        StructureTemplatePool structureTemplatePool = (StructureTemplatePool)jigsawConfiguration.startPool().get();
        StructurePoolElement structurePoolElement = structureTemplatePool.getRandomTemplate(random);
        if (structurePoolElement != EmptyPoolElement.INSTANCE) {
            PoolElementStructurePiece poolElementStructurePiece = pieceFactory.create(structureManager, structurePoolElement, blockPos, structurePoolElement.getGroundLevelDelta(), rotation, structurePoolElement.getBoundingBox(structureManager, blockPos, rotation));
            BoundingBox boundingBox = poolElementStructurePiece.getBoundingBox();
            int i = (boundingBox.maxX() + boundingBox.minX()) / 2;
            int j = (boundingBox.maxZ() + boundingBox.minZ()) / 2;
            int l;
            if (bl2) {
                l = blockPos.getY() + chunkGenerator.getFirstFreeHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
            } else {
                l = blockPos.getY();
            }

            int m = boundingBox.minY() + poolElementStructurePiece.getGroundLevelDelta();
            poolElementStructurePiece.move(0, l - m, 0);
            list.add(poolElementStructurePiece);
            if (jigsawConfiguration.maxDepth() > 0) {
                AABB aABB = new AABB((double)(i - 80), (double)(l - 80), (double)(j - 80), (double)(i + 80 + 1), (double)(l + 80 + 1), (double)(j + 80 + 1));
                Placer placer = new Placer(registry, jigsawConfiguration.maxDepth(), pieceFactory, chunkGenerator, structureManager, list, random);
                placer.placing.addLast(new PieceState(poolElementStructurePiece, new MutableObject(Shapes.join(Shapes.create(aABB), Shapes.create(AABB.of(boundingBox)), BooleanOp.ONLY_FIRST)), l + 80, 0));

                while(!placer.placing.isEmpty()) {
                    PieceState pieceState = (PieceState)placer.placing.removeFirst();
                    placer.tryPlacingChildren(pieceState.piece, pieceState.free, pieceState.boundsTop, pieceState.depth, bl, levelHeightAccessor);
                }

                Objects.requireNonNull(structurePieceAccessor);
                list.forEach(structurePieceAccessor::addPiece);
            }
        }
    }

    public static void addPieces(RegistryAccess registryAccess, PoolElementStructurePiece poolElementStructurePiece, int i, JigsawPlacement.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureManager structureManager, List<? super PoolElementStructurePiece> list, Random random, LevelHeightAccessor levelHeightAccessor) {
        Registry<StructureTemplatePool> registry = registryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Placer placer = new Placer(registry, i, pieceFactory, chunkGenerator, structureManager, list, random);
        placer.placing.addLast(new PieceState(poolElementStructurePiece, new MutableObject(Shapes.INFINITY), 0, 0));

        while(!placer.placing.isEmpty()) {
            PieceState pieceState = (PieceState)placer.placing.removeFirst();
            placer.tryPlacingChildren(pieceState.piece, pieceState.free, pieceState.boundsTop, pieceState.depth, false, levelHeightAccessor);
        }

    }

    public interface PieceFactory {
        PoolElementStructurePiece create(StructureManager structureManager, StructurePoolElement structurePoolElement, BlockPos blockPos, int i, Rotation rotation, BoundingBox boundingBox);
    }

    static final class Placer {
        private final Registry<StructureTemplatePool> pools;
        private final int maxDepth;
        private final JigsawPlacement.PieceFactory factory;
        private final ChunkGenerator chunkGenerator;
        private final StructureManager structureManager;
        private final List<? super PoolElementStructurePiece> pieces;
        private final Random random;
        final Deque<PieceState> placing = Queues.newArrayDeque();

        Placer(Registry<StructureTemplatePool> registry, int i, JigsawPlacement.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureManager structureManager, List<? super PoolElementStructurePiece> list, Random random) {
            this.pools = registry;
            this.maxDepth = i;
            this.factory = pieceFactory;
            this.chunkGenerator = chunkGenerator;
            this.structureManager = structureManager;
            this.pieces = list;
            this.random = random;
        }

        void tryPlacingChildren(PoolElementStructurePiece poolElementStructurePiece, MutableObject<VoxelShape> mutableObject, int i, int j, boolean bl, LevelHeightAccessor levelHeightAccessor) {
            StructurePoolElement structurePoolElement = poolElementStructurePiece.getElement();
            BlockPos blockPos = poolElementStructurePiece.getPosition();
            Rotation rotation = poolElementStructurePiece.getRotation();
            StructureTemplatePool.Projection projection = structurePoolElement.getProjection();
            boolean bl2 = projection == StructureTemplatePool.Projection.RIGID;
            MutableObject<VoxelShape> mutableObject2 = new MutableObject();
            BoundingBox boundingBox = poolElementStructurePiece.getBoundingBox();
            int k = boundingBox.minY();
            Iterator var15 = structurePoolElement.getShuffledJigsawBlocks(this.structureManager, blockPos, rotation, this.random).iterator();

            while(true) {
                while(true) {
                    while(true) {
                        label93:
                        while(var15.hasNext()) {
                            StructureTemplate.StructureBlockInfo structureBlockInfo = (StructureTemplate.StructureBlockInfo)var15.next();
                            Direction direction = JigsawBlock.getFrontFacing(structureBlockInfo.state);
                            BlockPos blockPos2 = structureBlockInfo.pos;
                            BlockPos blockPos3 = blockPos2.relative(direction);
                            int l = blockPos2.getY() - k;
                            int m = -1;
                            ResourceLocation resourceLocation = new ResourceLocation(structureBlockInfo.nbt.getString("pool"));
                            resourceLocation = new ResourceLocation( replacemcifnecessary(resourceLocation.toString()));
                            Optional<StructureTemplatePool> optional = this.pools.getOptional(resourceLocation);
                            if (optional.isPresent() && (((StructureTemplatePool)optional.get()).size() != 0 || Objects.equals(resourceLocation, Pools.EMPTY.location()))) {
                                ResourceLocation resourceLocation2 = ((StructureTemplatePool)optional.get()).getFallback();
                                resourceLocation2 = new ResourceLocation( replacemcifnecessary(resourceLocation2.toString()));
                                Optional<StructureTemplatePool> optional2 = this.pools.getOptional(resourceLocation2);
                                if (optional2.isPresent() && (((StructureTemplatePool)optional2.get()).size() != 0 || Objects.equals(resourceLocation2, Pools.EMPTY.location()))) {
                                    boolean bl3 = boundingBox.isInside(blockPos3);
                                    MutableObject mutableObject4;
                                    int o;
                                    if (bl3) {
                                        mutableObject4 = mutableObject2;
                                        o = k;
                                        if (mutableObject2.getValue() == null) {
                                            mutableObject2.setValue(Shapes.create(AABB.of(boundingBox)));
                                        }
                                    } else {
                                        mutableObject4 = mutableObject;
                                        o = i;
                                    }

                                    List<StructurePoolElement> list = Lists.newArrayList();
                                    if (j != this.maxDepth) {
                                        list.addAll(((StructureTemplatePool)optional.get()).getShuffledTemplates(this.random));
                                    }

                                    list.addAll(((StructureTemplatePool)optional2.get()).getShuffledTemplates(this.random));
                                    Iterator var30 = list.iterator();

                                    while(var30.hasNext()) {
                                        StructurePoolElement structurePoolElement2 = (StructurePoolElement)var30.next();
                                        if (structurePoolElement2 == EmptyPoolElement.INSTANCE) {
                                            break;
                                        }

                                        Iterator var32 = Rotation.getShuffled(this.random).iterator();

                                        label133:
                                        while(var32.hasNext()) {
                                            Rotation rotation2 = (Rotation)var32.next();
                                            List<StructureTemplate.StructureBlockInfo> list2 = structurePoolElement2.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, rotation2, this.random);
                                            BoundingBox boundingBox2 = structurePoolElement2.getBoundingBox(this.structureManager, BlockPos.ZERO, rotation2);
                                            int q;
                                            if (bl && boundingBox2.getYSpan() <= 16) {
                                                q = list2.stream().mapToInt((structureBlockInfox) -> {
                                                    if (!boundingBox2.isInside(structureBlockInfox.pos.relative(JigsawBlock.getFrontFacing(structureBlockInfox.state)))) {
                                                        return 0;
                                                    } else {
                                                        ResourceLocation resourceLocation21 = new ResourceLocation(structureBlockInfox.nbt.getString("pool"));
                                                        Optional<StructureTemplatePool> optional3 = this.pools.getOptional(resourceLocation21);
                                                        Optional<StructureTemplatePool> optional4 = optional3.flatMap((structureTemplatePool) -> {
                                                            return this.pools.getOptional(structureTemplatePool.getFallback());
                                                        });
                                                        int i2 = (Integer)optional3.map((structureTemplatePool) -> {
                                                            return structureTemplatePool.getMaxSize(this.structureManager);
                                                        }).orElse(0);
                                                        int j2 = (Integer)optional4.map((structureTemplatePool) -> {
                                                            return structureTemplatePool.getMaxSize(this.structureManager);
                                                        }).orElse(0);
                                                        return Math.max(i2, j2);
                                                    }
                                                }).max().orElse(0);
                                            } else {
                                                q = 0;
                                            }

                                            Iterator var37 = list2.iterator();

                                            StructureTemplatePool.Projection projection2;
                                            boolean bl4;
                                            int s;
                                            int t;
                                            int v;
                                            BoundingBox boundingBox4;
                                            BlockPos blockPos6;
                                            int y;
                                            do {
                                                StructureTemplate.StructureBlockInfo structureBlockInfo2;
                                                do {
                                                    if (!var37.hasNext()) {
                                                        continue label133;
                                                    }

                                                    structureBlockInfo2 = (StructureTemplate.StructureBlockInfo)var37.next();
                                                } while(!JigsawBlock.canAttach(structureBlockInfo, structureBlockInfo2));

                                                BlockPos blockPos4 = structureBlockInfo2.pos;
                                                BlockPos blockPos5 = blockPos3.subtract(blockPos4);
                                                BoundingBox boundingBox3 = structurePoolElement2.getBoundingBox(this.structureManager, blockPos5, rotation2);
                                                int r = boundingBox3.minY();
                                                projection2 = structurePoolElement2.getProjection();
                                                bl4 = projection2 == StructureTemplatePool.Projection.RIGID;
                                                s = blockPos4.getY();
                                                t = l - s + JigsawBlock.getFrontFacing(structureBlockInfo.state).getStepY();
                                                if (bl2 && bl4) {
                                                    v = k + t;
                                                } else {
                                                    if (m == -1) {
                                                        m = this.chunkGenerator.getFirstFreeHeight(blockPos2.getX(), blockPos2.getZ(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
                                                    }

                                                    v = m - s;
                                                }

                                                int w = v - r;
                                                boundingBox4 = boundingBox3.moved(0, w, 0);
                                                blockPos6 = blockPos5.offset(0, w, 0);
                                                if (q > 0) {
                                                    y = Math.max(q + 1, boundingBox4.maxY() - boundingBox4.minY());
                                                    boundingBox4.encapsulate(new BlockPos(boundingBox4.minX(), boundingBox4.minY() + y, boundingBox4.minZ()));
                                                }
                                            } while(Shapes.joinIsNotEmpty((VoxelShape)mutableObject4.getValue(), Shapes.create(AABB.of(boundingBox4).deflate(0.25D)), BooleanOp.ONLY_SECOND));

                                            mutableObject4.setValue(Shapes.joinUnoptimized((VoxelShape)mutableObject4.getValue(), Shapes.create(AABB.of(boundingBox4)), BooleanOp.ONLY_FIRST));
                                            y = poolElementStructurePiece.getGroundLevelDelta();
                                            int aa;
                                            if (bl4) {
                                                aa = y - t;
                                            } else {
                                                aa = structurePoolElement2.getGroundLevelDelta();
                                            }

                                            PoolElementStructurePiece poolElementStructurePiece2 = this.factory.create(this.structureManager, structurePoolElement2, blockPos6, aa, rotation2, boundingBox4);
                                            int ad;
                                            if (bl2) {
                                                ad = k + l;
                                            } else if (bl4) {
                                                ad = v + s;
                                            } else {
                                                if (m == -1) {
                                                    m = this.chunkGenerator.getFirstFreeHeight(blockPos2.getX(), blockPos2.getZ(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
                                                }

                                                ad = m + t / 2;
                                            }

                                            poolElementStructurePiece.addJunction(new JigsawJunction(blockPos3.getX(), ad - l + y, blockPos3.getZ(), t, projection2));
                                            poolElementStructurePiece2.addJunction(new JigsawJunction(blockPos2.getX(), ad - s + aa, blockPos2.getZ(), -t, projection));
                                            this.pieces.add(poolElementStructurePiece2);
                                            if (j + 1 <= this.maxDepth) {
                                                this.placing.addLast(new PieceState(poolElementStructurePiece2, mutableObject4, o, j + 1));
                                            }
                                            continue label93;
                                        }
                                    }
                                } else {
                                    LOGGER.warn((String)"Empty or non-existent fallback pool: {}", (Object)resourceLocation2);
                                }
                            } else {
                                LOGGER.warn((String)"Empty or non-existent pool: {}", (Object)resourceLocation);
                            }
                        }

                        return;
                    }
                }
            }
        }
        public String replacemcifnecessary(String string){
            if(string.contains("minecraft") && !string.contains("empty")){
                string = string.replace("minecraft","warhammermod");
            }
            return string;
        }
    }

    static final class PieceState {
        final PoolElementStructurePiece piece;
        final MutableObject<VoxelShape> free;
        final int boundsTop;
        final int depth;

        PieceState(PoolElementStructurePiece poolElementStructurePiece, MutableObject<VoxelShape> mutableObject, int i, int j) {
            this.piece = poolElementStructurePiece;
            this.free = mutableObject;
            this.boundsTop = i;
            this.depth = j;
        }
    }
}
