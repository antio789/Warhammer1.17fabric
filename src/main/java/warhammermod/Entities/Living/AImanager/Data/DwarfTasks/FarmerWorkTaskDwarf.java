/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package warhammermod.Entities.Living.AImanager.Data.DwarfTasks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.WorldEvents;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.utils.Registry.ItemsInit;

import java.util.List;
import java.util.Optional;

public class FarmerWorkTaskDwarf
extends VillagerWorkTaskDwarf {
    private static final List<Item> COMPOSTABLES = ImmutableList.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);

    @Override
    protected void performAdditionalWork(ServerWorld world, DwarfEntity entity) {
        Optional<GlobalPos> optional = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE);
        if (optional.isEmpty()) {
            return;
        }
        GlobalPos globalPos = optional.get();
        BlockState blockState = world.getBlockState(globalPos.pos());
        if (blockState.isOf(Blocks.COMPOSTER)) {
            this.craftAndDropBread(entity);
            this.compostSeeds(world, entity, globalPos, blockState);
        }
    }

    private void compostSeeds(ServerWorld world, DwarfEntity entity, GlobalPos pos, BlockState composterState) {
        BlockPos blockPos = pos.pos();
        if (composterState.get(ComposterBlock.LEVEL) == 8) {
            composterState = ComposterBlock.emptyFullComposter(entity, composterState, world, blockPos);
        }
        int i = 20;
        int j = 10;
        int[] is = new int[COMPOSTABLES.size()];
        SimpleInventory simpleInventory = entity.getInventory();
        int k = simpleInventory.size();
        BlockState blockState = composterState;
        for (int l = k - 1; l >= 0 && i > 0; --l) {
            int o;
            ItemStack itemStack = simpleInventory.getStack(l);
            int m = COMPOSTABLES.indexOf(itemStack.getItem());
            if (m == -1) continue;
            int n = itemStack.getCount();
            is[m] = o = is[m] + n;
            int p = Math.min(Math.min(o - 10, i), n);
            if (p <= 0) continue;
            i -= p;
            for (int q = 0; q < p; ++q) {
                if ((blockState = ComposterBlock.compost(entity, blockState, world, itemStack, blockPos)).get(ComposterBlock.LEVEL) != 7) continue;
                this.syncComposterEvent(world, composterState, blockPos, blockState);
                return;
            }
        }
        this.syncComposterEvent(world, composterState, blockPos, blockState);
    }

    private void syncComposterEvent(ServerWorld world, BlockState oldState, BlockPos pos, BlockState newState) {
        world.syncWorldEvent(WorldEvents.COMPOSTER_USED, pos, newState != oldState ? 1 : 0);
    }

    private void craftAndDropBread(DwarfEntity entity) {
        SimpleInventory simpleInventory = entity.getInventory();
        if (simpleInventory.count(ItemsInit.Beer) > 12) {
            return;
        }
        int i = simpleInventory.count(Items.WHEAT);
        int j = 3;
        int k = 3;
        int l = Math.min(6, i / 6);
        if (l == 0) {
            return;
        }
        int m = l * 6;
        simpleInventory.removeItem(Items.WHEAT, m);
        ItemStack itemStack = simpleInventory.addStack(new ItemStack(ItemsInit.Beer, l));
        if (!itemStack.isEmpty()) {
            entity.dropStack(itemStack, 0.5f);
        }
    }
}
