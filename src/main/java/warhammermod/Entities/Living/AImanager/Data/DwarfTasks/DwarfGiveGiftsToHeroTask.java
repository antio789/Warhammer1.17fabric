/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package warhammermod.Entities.Living.AImanager.Data.DwarfTasks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerProfession;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfessionRecord;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.utils.Registry.WHRegistry;
import warhammermod.utils.functions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DwarfGiveGiftsToHeroTask
extends MultiTickTask<DwarfEntity> {
    private static final int MAX_DISTANCE = 5;
    private static final int DEFAULT_DURATION = 600;
    private static final int MAX_NEXT_GIFT_DELAY = 6600;
    private static final int RUN_TIME = 20;

    private static final Map<DwarfProfessionRecord, RegistryKey<LootTable>> GIFTS = Util.make(Maps.newHashMap(), gifts -> {
        gifts.put(DwarfProfessionRecord.Lord, WHRegistry.HERO_OF_THE_VILLAGE_LORD_GIFT_GAMEPLAY);
        gifts.put(DwarfProfessionRecord.Miner, LootTables.HERO_OF_THE_VILLAGE_BUTCHER_GIFT_GAMEPLAY);
        gifts.put(DwarfProfessionRecord.FARMER, LootTables.HERO_OF_THE_VILLAGE_FARMER_GIFT_GAMEPLAY);
        gifts.put(DwarfProfessionRecord.Slayer, LootTables.HERO_OF_THE_VILLAGE_BUTCHER_GIFT_GAMEPLAY);
        gifts.put(DwarfProfessionRecord.Engineer, LootTables.HERO_OF_THE_VILLAGE_TOOLSMITH_GIFT_GAMEPLAY);
        gifts.put(DwarfProfessionRecord.Builder, LootTables.HERO_OF_THE_VILLAGE_MASON_GIFT_GAMEPLAY);
    });

    private static final float WALK_SPEED = 0.5f;
    private int ticksLeft = 600;
    private boolean done;
    private long startTime;

    public DwarfGiveGiftsToHeroTask(int delay) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleState.VALUE_PRESENT), delay);
    }

    @Override
    protected boolean shouldRun(ServerWorld serverWorld, DwarfEntity villagerEntity) {
        if (!this.isNearestPlayerHero(villagerEntity)) {
            return false;
        }
        if (this.ticksLeft > 0) {
            --this.ticksLeft;
            return false;
        }
        return true;
    }

    @Override
    protected void run(ServerWorld serverWorld, DwarfEntity villagerEntity, long l) {
        this.done = false;
        this.startTime = l;
        PlayerEntity playerEntity = this.getNearestPlayerIfHero(villagerEntity).get();
        villagerEntity.getBrain().remember(MemoryModuleType.INTERACTION_TARGET, playerEntity);
        LookTargetUtil.lookAt(villagerEntity, playerEntity);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld serverWorld, DwarfEntity villagerEntity, long l) {
        return this.isNearestPlayerHero(villagerEntity) && !this.done;
    }

    @Override
    protected void keepRunning(ServerWorld serverWorld, DwarfEntity villagerEntity, long l) {
        PlayerEntity playerEntity = this.getNearestPlayerIfHero(villagerEntity).get();
        LookTargetUtil.lookAt(villagerEntity, playerEntity);
        if (this.isCloseEnough(villagerEntity, playerEntity)) {
            if (l - this.startTime > 20L) {
                this.giveGifts(villagerEntity, playerEntity);
                this.done = true;
            }
        } else {
            LookTargetUtil.walkTowards((LivingEntity)villagerEntity, playerEntity, 0.5f, 5);
        }
    }

    @Override
    protected void finishRunning(ServerWorld serverWorld, DwarfEntity villagerEntity, long l) {
        this.ticksLeft = DwarfGiveGiftsToHeroTask.getNextGiftDelay(serverWorld);
        villagerEntity.getBrain().forget(MemoryModuleType.INTERACTION_TARGET);
        villagerEntity.getBrain().forget(MemoryModuleType.WALK_TARGET);
        villagerEntity.getBrain().forget(MemoryModuleType.LOOK_TARGET);
    }

    private void giveGifts(DwarfEntity villager, LivingEntity recipient) {
        List<ItemStack> list = this.getGifts(villager);
        for (ItemStack itemStack : list) {
            LookTargetUtil.give(villager, itemStack, recipient.getPos());
        }
    }

    private List<ItemStack> getGifts(DwarfEntity villager) {
        if (villager.isBaby()) {
            return ImmutableList.of(new ItemStack(Items.POPPY));
        }
        DwarfProfessionRecord villagerProfession = villager.getProfession();
        if (GIFTS.containsKey(villagerProfession)) {
            LootTable lootTable = villager.getWorld().getServer().getReloadableRegistries().getLootTable(GIFTS.get(villagerProfession));
            LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder((ServerWorld)villager.getWorld()).add(LootContextParameters.ORIGIN, villager.getPos()).add(LootContextParameters.THIS_ENTITY, villager).build(LootContextTypes.GIFT);
            return lootTable.generateLoot(lootContextParameterSet);
        }
        return ImmutableList.of(new ItemStack(Items.WHEAT_SEEDS));
    }

    private boolean isNearestPlayerHero(DwarfEntity villager) {
        return this.getNearestPlayerIfHero(villager).isPresent();
    }

    private Optional<PlayerEntity> getNearestPlayerIfHero(DwarfEntity villager) {
        return villager.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).filter(this::isHero);
    }

    private boolean isHero(PlayerEntity player) {
        return player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);
    }

    private boolean isCloseEnough(DwarfEntity villager, PlayerEntity player) {
        BlockPos blockPos = player.getBlockPos();
        BlockPos blockPos2 = villager.getBlockPos();
        return blockPos2.isWithinDistance(blockPos, 5.0);
    }

    private static int getNextGiftDelay(ServerWorld world) {
        return 600 + world.random.nextInt(6001);
    }

}

