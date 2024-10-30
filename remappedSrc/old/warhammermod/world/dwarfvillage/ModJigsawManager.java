package warhammermod.world.dwarfvillage;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.minecraft.block.JigsawBlock;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.*;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ModJigsawManager extends StructurePoolBasedGenerator {
    /**
     *
     * very lazy fix were I replace minecraft with warhammermod: I don't know why it is the case in the first place but I already took way too much time trying to figure it out
     */
    static final Logger LOGGER = LogManager.getLogger();

    public static void generate(DynamicRegistryManager registryAccess, JigsawConfiguration jigsawConfiguration, StructurePoolBasedGenerator.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureTemplateManager structureManager, BlockPos blockPos, StructurePiecesHolder structurePieceAccessor, Random random, boolean bl, boolean bl2, HeightLimitView levelHeightAccessor) {
        net.minecraft.world.gen.structure.Structure.bootstrap();
        List<PoolStructurePiece> list = Lists.newArrayList();
        Registry<StructurePool> registry = registryAccess.get(Registry.TEMPLATE_POOL_REGISTRY);
        BlockRotation rotation = BlockRotation.random(random);
        StructurePool structureTemplatePool = (StructurePool)jigsawConfiguration.startPool().get();
        StructurePoolElement structurePoolElement = structureTemplatePool.getRandomElement(random);
        if (structurePoolElement != EmptyPoolElement.INSTANCE) {
            PoolStructurePiece poolElementStructurePiece = pieceFactory.create(structureManager, structurePoolElement, blockPos, structurePoolElement.getGroundLevelDelta(), rotation, structurePoolElement.getBoundingBox(structureManager, blockPos, rotation));
            BlockBox boundingBox = poolElementStructurePiece.getBoundingBox();
            int i = (boundingBox.getMaxX() + boundingBox.getMinX()) / 2;
            int j = (boundingBox.getMaxZ() + boundingBox.getMinZ()) / 2;
            int l;
            if (bl2) {
                l = blockPos.getY() + chunkGenerator.getHeightOnGround(i, j, Heightmap.Type.WORLD_SURFACE_WG, levelHeightAccessor);
            } else {
                l = blockPos.getY();
            }

            int m = boundingBox.getMinY() + poolElementStructurePiece.getGroundLevelDelta();
            poolElementStructurePiece.translate(0, l - m, 0);
            list.add(poolElementStructurePiece);
            if (jigsawConfiguration.maxDepth() > 0) {
                Box aABB = new Box((double)(i - 80), (double)(l - 80), (double)(j - 80), (double)(i + 80 + 1), (double)(l + 80 + 1), (double)(j + 80 + 1));
                Placer placer = new Placer(registry, jigsawConfiguration.maxDepth(), pieceFactory, chunkGenerator, structureManager, list, random);
                placer.placing.addLast(new PieceState(poolElementStructurePiece, new MutableObject(VoxelShapes.combineAndSimplify(VoxelShapes.cuboid(aABB), VoxelShapes.cuboid(Box.from(boundingBox)), BooleanBiFunction.ONLY_FIRST)), l + 80, 0));

                while(!placer.placing.isEmpty()) {
                    PieceState pieceState = (PieceState)placer.placing.removeFirst();
                    placer.tryPlacingChildren(pieceState.piece, pieceState.free, pieceState.boundsTop, pieceState.depth, bl, levelHeightAccessor);
                }

                Objects.requireNonNull(structurePieceAccessor);
                list.forEach(structurePieceAccessor::addPiece);
            }
        }
    }

    public static void generate(DynamicRegistryManager registryAccess, PoolStructurePiece poolElementStructurePiece, int i, StructurePoolBasedGenerator.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureTemplateManager structureManager, List<? super PoolStructurePiece> list, Random random, HeightLimitView levelHeightAccessor) {
        Registry<StructurePool> registry = registryAccess.get(Registry.TEMPLATE_POOL_REGISTRY);
        Placer placer = new Placer(registry, i, pieceFactory, chunkGenerator, structureManager, list, random);
        placer.placing.addLast(new PieceState(poolElementStructurePiece, new MutableObject(VoxelShapes.UNBOUNDED), 0, 0));

        while(!placer.placing.isEmpty()) {
            PieceState pieceState = (PieceState)placer.placing.removeFirst();
            placer.tryPlacingChildren(pieceState.piece, pieceState.free, pieceState.boundsTop, pieceState.depth, false, levelHeightAccessor);
        }

    }

    public interface PieceFactory {
        PoolStructurePiece create(StructureTemplateManager structureManager, StructurePoolElement structurePoolElement, BlockPos blockPos, int i, BlockRotation rotation, BlockBox boundingBox);
    }

    static final class Placer {
        private final Registry<StructurePool> pools;
        private final int maxDepth;
        private final StructurePoolBasedGenerator.PieceFactory factory;
        private final ChunkGenerator chunkGenerator;
        private final StructureTemplateManager structureManager;
        private final List<? super PoolStructurePiece> pieces;
        private final Random random;
        final Deque<PieceState> placing = Queues.newArrayDeque();

        Placer(Registry<StructurePool> registry, int i, StructurePoolBasedGenerator.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureTemplateManager structureManager, List<? super PoolStructurePiece> list, Random random) {
            this.pools = registry;
            this.maxDepth = i;
            this.factory = pieceFactory;
            this.chunkGenerator = chunkGenerator;
            this.structureManager = structureManager;
            this.pieces = list;
            this.random = random;
        }

        void tryPlacingChildren(PoolStructurePiece poolElementStructurePiece, MutableObject<VoxelShape> mutableObject, int i, int j, boolean bl, HeightLimitView levelHeightAccessor) {
            StructurePoolElement structurePoolElement = poolElementStructurePiece.getPoolElement();
            BlockPos blockPos = poolElementStructurePiece.getPos();
            BlockRotation rotation = poolElementStructurePiece.getRotation();
            StructurePool.Projection projection = structurePoolElement.getProjection();
            boolean bl2 = projection == StructurePool.Projection.RIGID;
            MutableObject<VoxelShape> mutableObject2 = new MutableObject();
            BlockBox boundingBox = poolElementStructurePiece.getBoundingBox();
            int k = boundingBox.getMinY();
            Iterator var15 = structurePoolElement.getStructureBlockInfos(this.structureManager, blockPos, rotation, this.random).iterator();

            while(true) {
                while(true) {
                    while(true) {
                        label93:
                        while(var15.hasNext()) {
                            StructureTemplate.StructureBlockInfo structureBlockInfo = (StructureTemplate.StructureBlockInfo)var15.next();
                            Direction direction = JigsawBlock.getFacing(structureBlockInfo.state);
                            BlockPos blockPos2 = structureBlockInfo.pos;
                            BlockPos blockPos3 = blockPos2.offset(direction);
                            int l = blockPos2.getY() - k;
                            int m = -1;
                            Identifier resourceLocation = new Identifier(structureBlockInfo.nbt.getString("pool"));
                            resourceLocation = new Identifier( replacemcifnecessary(resourceLocation.toString()));
                            Optional<StructurePool> optional = this.pools.getOrEmpty(resourceLocation);
                            if (optional.isPresent() && (((StructurePool)optional.get()).getElementCount() != 0 || Objects.equals(resourceLocation, StructurePools.EMPTY.getValue()))) {
                                Identifier resourceLocation2 = ((StructurePool)optional.get()).getFallback();
                                resourceLocation2 = new Identifier( replacemcifnecessary(resourceLocation2.toString()));
                                Optional<StructurePool> optional2 = this.pools.getOrEmpty(resourceLocation2);
                                if (optional2.isPresent() && (((StructurePool)optional2.get()).getElementCount() != 0 || Objects.equals(resourceLocation2, StructurePools.EMPTY.getValue()))) {
                                    boolean bl3 = boundingBox.contains(blockPos3);
                                    MutableObject mutableObject4;
                                    int o;
                                    if (bl3) {
                                        mutableObject4 = mutableObject2;
                                        o = k;
                                        if (mutableObject2.getValue() == null) {
                                            mutableObject2.setValue(VoxelShapes.cuboid(Box.from(boundingBox)));
                                        }
                                    } else {
                                        mutableObject4 = mutableObject;
                                        o = i;
                                    }

                                    List<StructurePoolElement> list = Lists.newArrayList();
                                    if (j != this.maxDepth) {
                                        list.addAll(((StructurePool)optional.get()).getElementIndicesInRandomOrder(this.random));
                                    }

                                    list.addAll(((StructurePool)optional2.get()).getElementIndicesInRandomOrder(this.random));
                                    Iterator var30 = list.iterator();

                                    while(var30.hasNext()) {
                                        StructurePoolElement structurePoolElement2 = (StructurePoolElement)var30.next();
                                        if (structurePoolElement2 == EmptyPoolElement.INSTANCE) {
                                            break;
                                        }

                                        Iterator var32 = BlockRotation.randomRotationOrder(this.random).iterator();

                                        label133:
                                        while(var32.hasNext()) {
                                            BlockRotation rotation2 = (BlockRotation)var32.next();
                                            List<StructureTemplate.StructureBlockInfo> list2 = structurePoolElement2.getStructureBlockInfos(this.structureManager, BlockPos.ORIGIN, rotation2, this.random);
                                            BlockBox boundingBox2 = structurePoolElement2.getBoundingBox(this.structureManager, BlockPos.ORIGIN, rotation2);
                                            int q;
                                            if (bl && boundingBox2.getBlockCountY() <= 16) {
                                                q = list2.stream().mapToInt((structureBlockInfox) -> {
                                                    if (!boundingBox2.contains(structureBlockInfox.pos.offset(JigsawBlock.getFacing(structureBlockInfox.state)))) {
                                                        return 0;
                                                    } else {
                                                        Identifier resourceLocation21 = new Identifier(structureBlockInfox.nbt.getString("pool"));
                                                        Optional<StructurePool> optional3 = this.pools.getOrEmpty(resourceLocation21);
                                                        Optional<StructurePool> optional4 = optional3.flatMap((structureTemplatePool) -> {
                                                            return this.pools.getOrEmpty(structureTemplatePool.getFallback());
                                                        });
                                                        int i2 = (Integer)optional3.map((structureTemplatePool) -> {
                                                            return structureTemplatePool.getHighestY(this.structureManager);
                                                        }).orElse(0);
                                                        int j2 = (Integer)optional4.map((structureTemplatePool) -> {
                                                            return structureTemplatePool.getHighestY(this.structureManager);
                                                        }).orElse(0);
                                                        return Math.max(i2, j2);
                                                    }
                                                }).max().orElse(0);
                                            } else {
                                                q = 0;
                                            }

                                            Iterator var37 = list2.iterator();

                                            StructurePool.Projection projection2;
                                            boolean bl4;
                                            int s;
                                            int t;
                                            int v;
                                            BlockBox boundingBox4;
                                            BlockPos blockPos6;
                                            int y;
                                            do {
                                                StructureTemplate.StructureBlockInfo structureBlockInfo2;
                                                do {
                                                    if (!var37.hasNext()) {
                                                        continue label133;
                                                    }

                                                    structureBlockInfo2 = (StructureTemplate.StructureBlockInfo)var37.next();
                                                } while(!JigsawBlock.attachmentMatches(structureBlockInfo, structureBlockInfo2));

                                                BlockPos blockPos4 = structureBlockInfo2.pos;
                                                BlockPos blockPos5 = blockPos3.subtract(blockPos4);
                                                BlockBox boundingBox3 = structurePoolElement2.getBoundingBox(this.structureManager, blockPos5, rotation2);
                                                int r = boundingBox3.getMinY();
                                                projection2 = structurePoolElement2.getProjection();
                                                bl4 = projection2 == StructurePool.Projection.RIGID;
                                                s = blockPos4.getY();
                                                t = l - s + JigsawBlock.getFacing(structureBlockInfo.state).getOffsetY();
                                                if (bl2 && bl4) {
                                                    v = k + t;
                                                } else {
                                                    if (m == -1) {
                                                        m = this.chunkGenerator.getHeightOnGround(blockPos2.getX(), blockPos2.getZ(), Heightmap.Type.WORLD_SURFACE_WG, levelHeightAccessor);
                                                    }

                                                    v = m - s;
                                                }

                                                int w = v - r;
                                                boundingBox4 = boundingBox3.offset(0, w, 0);
                                                blockPos6 = blockPos5.add(0, w, 0);
                                                if (q > 0) {
                                                    y = Math.max(q + 1, boundingBox4.getMaxY() - boundingBox4.getMinY());
                                                    boundingBox4.encompass(new BlockPos(boundingBox4.getMinX(), boundingBox4.getMinY() + y, boundingBox4.getMinZ()));
                                                }
                                            } while(VoxelShapes.matchesAnywhere((VoxelShape)mutableObject4.getValue(), VoxelShapes.cuboid(Box.from(boundingBox4).contract(0.25D)), BooleanBiFunction.ONLY_SECOND));

                                            mutableObject4.setValue(VoxelShapes.combine((VoxelShape)mutableObject4.getValue(), VoxelShapes.cuboid(Box.from(boundingBox4)), BooleanBiFunction.ONLY_FIRST));
                                            y = poolElementStructurePiece.getGroundLevelDelta();
                                            int aa;
                                            if (bl4) {
                                                aa = y - t;
                                            } else {
                                                aa = structurePoolElement2.getGroundLevelDelta();
                                            }

                                            PoolStructurePiece poolElementStructurePiece2 = this.factory.create(this.structureManager, structurePoolElement2, blockPos6, aa, rotation2, boundingBox4);
                                            int ad;
                                            if (bl2) {
                                                ad = k + l;
                                            } else if (bl4) {
                                                ad = v + s;
                                            } else {
                                                if (m == -1) {
                                                    m = this.chunkGenerator.getHeightOnGround(blockPos2.getX(), blockPos2.getZ(), Heightmap.Type.WORLD_SURFACE_WG, levelHeightAccessor);
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
        final PoolStructurePiece piece;
        final MutableObject<VoxelShape> free;
        final int boundsTop;
        final int depth;

        PieceState(PoolStructurePiece poolElementStructurePiece, MutableObject<VoxelShape> mutableObject, int i, int j) {
            this.piece = poolElementStructurePiece;
            this.free = mutableObject;
            this.boundsTop = i;
            this.depth = j;
        }
    }
}
