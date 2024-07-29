/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package warhammermod.Entities.Living.AImanager.Data.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfessionRecord;
import warhammermod.Entities.Living.DwarfEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class DwarfGatherItemsVillagerTask
extends MultiTickTask<DwarfEntity> {
    private Set<Item> items = ImmutableSet.of();

    public DwarfGatherItemsVillagerTask() {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleState.VALUE_PRESENT));
    }

    protected boolean shouldRun(ServerWorld serverWorld, DwarfEntity villagerEntity) {
        return LookTargetUtil.canSee(villagerEntity.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER);
    }

    protected boolean shouldKeepRunning(ServerWorld serverWorld, DwarfEntity villagerEntity, long l) {
        return this.shouldRun(serverWorld, villagerEntity);
    }

    protected void run(ServerWorld serverWorld, DwarfEntity villagerEntity, long l) {
        DwarfEntity villagerEntity2 = (DwarfEntity)villagerEntity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.INTERACTION_TARGET).get();
        LookTargetUtil.lookAtAndWalkTowardsEachOther(villagerEntity, villagerEntity2, 0.5f, 2);
        this.items = DwarfGatherItemsVillagerTask.getGatherableItems(villagerEntity, villagerEntity2);
    }

    protected void keepRunning(ServerWorld serverWorld, DwarfEntity villagerEntity, long l) {
        DwarfEntity villagerEntity2 = (DwarfEntity)villagerEntity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (villagerEntity.squaredDistanceTo(villagerEntity2) > 5.0) {
            return;
        }
        LookTargetUtil.lookAtAndWalkTowardsEachOther(villagerEntity, villagerEntity2, 0.5f, 2);
        villagerEntity.talkWithVillager(serverWorld, villagerEntity2, l);
        if (villagerEntity.wantsToStartBreeding() && (villagerEntity.getProfession() == DwarfProfessionRecord.FARMER || villagerEntity2.canBreed())) {
            DwarfGatherItemsVillagerTask.giveHalfOfStack(villagerEntity, DwarfEntity.ITEM_FOOD_VALUES.keySet(), villagerEntity2);
        }
        if (villagerEntity2.getProfession() == DwarfProfessionRecord.FARMER && villagerEntity.getInventory().count(Items.WHEAT) > Items.WHEAT.getMaxCount() / 2) {
            DwarfGatherItemsVillagerTask.giveHalfOfStack(villagerEntity, ImmutableSet.of(Items.WHEAT), villagerEntity2);
        }
        if (!this.items.isEmpty() && villagerEntity.getInventory().containsAny(this.items)) {
            DwarfGatherItemsVillagerTask.giveHalfOfStack(villagerEntity, this.items, villagerEntity2);
        }
    }

    protected void finishRunning(ServerWorld serverWorld, DwarfEntity villagerEntity, long l) {
        villagerEntity.getBrain().forget(MemoryModuleType.INTERACTION_TARGET);
    }

    private static Set<Item> getGatherableItems(DwarfEntity entity, DwarfEntity target) {
        ImmutableSet<Item> immutableSet = target.getProfession().gatherableItems();
        ImmutableSet<Item> immutableSet2 = entity.getProfession().gatherableItems();
        return immutableSet.stream().filter(item -> !immutableSet2.contains(item)).collect(Collectors.toSet());
    }

    private static void giveHalfOfStack(DwarfEntity villager, Set<Item> validItems, LivingEntity target) {
        SimpleInventory simpleInventory = villager.getInventory();
        ItemStack itemStack = ItemStack.EMPTY;
        for (int i = 0; i < simpleInventory.size(); ++i) {
            int j;
            Item item;
            ItemStack itemStack2 = simpleInventory.getStack(i);
            if (itemStack2.isEmpty() || !validItems.contains(item = itemStack2.getItem())) continue;
            if (itemStack2.getCount() > itemStack2.getMaxCount() / 2) {
                j = itemStack2.getCount() / 2;
            } else {
                if (itemStack2.getCount() <= 24) continue;
                j = itemStack2.getCount() - 24;
            }
            itemStack2.decrement(j);
            itemStack = new ItemStack(item, j);
            break;
        }
        if (!itemStack.isEmpty()) {
            LookTargetUtil.give(villager, itemStack, target.getPos());
        }
    }


}

