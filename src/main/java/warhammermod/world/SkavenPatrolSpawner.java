package warhammermod.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.spawner.SpecialSpawner;
import warhammermod.utils.Registry.Entityinit;

public class SkavenPatrolSpawner implements SpecialSpawner {


    private int cooldown;

    @Override
    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        if (!spawnMonsters) {
            return 0;
        }
        if (!world.getGameRules().getBoolean(GameRules.DO_PATROL_SPAWNING)) {
            return 0;
        }
        Random random = world.random;
        --this.cooldown;
        if (this.cooldown > 0) {
            return 0;
        }
        this.cooldown += 1000 + random.nextInt(400);//cd 1000 + rand 400
        long l = world.getTimeOfDay() / 24000L;
        if (l < 5L && false) {
            return 0;
        }
        if (random.nextInt(1) != 0) {//5
            return 0;
        }
        int playerlistsize = world.getPlayers().size();
        if (playerlistsize < 1) {
            return 0;
        }
        PlayerEntity playerEntity = world.getPlayers().get(random.nextInt(playerlistsize));
        if (playerEntity.isSpectator()) {
            return 0;
        }
        if (world.isNearOccupiedPointOfInterest(playerEntity.getBlockPos(), 2)) {
            return 0;
        }
        int j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
        BlockPos.Mutable mutable = playerEntity.getBlockPos().mutableCopy().move(j, 0, k);
        if (!world.isRegionLoaded(mutable.getX() - 10, mutable.getZ() - 10, mutable.getX() + 10, mutable.getZ() + 10)) {
            return 0;
        }
        RegistryEntry<Biome> registryEntry = world.getBiome(mutable);
        if (registryEntry.isIn(BiomeTags.WITHOUT_PATROL_SPAWNS)) {
            return 0;
        }
        int patrol_size = 0;
        int o = (int)Math.ceil(world.getLocalDifficulty(mutable).getLocalDifficulty()) + 3;
        for (int p = 0; p < o; ++p) {
            ++patrol_size;
            mutable.setY(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable).getY());
            if (p == 0) {
                if (!this.spawnSkaven(world, mutable, random, true)) {
                    break;
                }
            } else {
                this.spawnSkaven(world, mutable, random, false);
            }
            mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
            mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
        }
        return patrol_size;
    }

    /**
     * @param captain whether the pillager is the captain of a patrol
     */
    private boolean spawnSkaven(ServerWorld world, BlockPos pos, Random random, boolean captain) {

        BlockState blockState = world.getBlockState(pos);
        if (!SpawnHelper.isClearForSpawn(world, pos, blockState, blockState.getFluidState(), Entityinit.SKAVEN)) {
            return false;
        }
        if (!PatrolEntity.canSpawn(Entityinit.SKAVEN, world, SpawnReason.PATROL, pos, random)) {
            return false;
        }
        PatrolEntity patrolEntity = Entityinit.SKAVEN.create(world);
        if (patrolEntity != null) {
            if (captain) {
                patrolEntity.setPatrolLeader(true);
                patrolEntity.setRandomPatrolTarget();
            }
            patrolEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            patrolEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, null);
            world.spawnEntityAndPassengers(patrolEntity);
            System.out.println("spawned patrol at: " + pos.getX() + "  "+pos.getY()+"  "+pos.getZ());
            return true;
        }
        return false;
    }
}
