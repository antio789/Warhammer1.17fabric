package warhammermod.Entities.Living.AImanager.DwarfTasks;

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
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.Items.ItemsInit;

import java.util.List;
import java.util.Optional;

public class WHWorkATComposter extends WHWorkAtPoi_spawngolemold {
   private static final List<Item> COMPOSTABLE_ITEMS = ImmutableList.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);

   protected void useWorkstation(ServerWorld p_230251_1_, DwarfEntity p_230251_2_) {
      Optional<GlobalPos> optional = p_230251_2_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE);
      if (optional.isPresent()) {
         GlobalPos globalpos = optional.get();
         BlockState blockstate = p_230251_1_.getBlockState(globalpos.getPos());
         if (blockstate.isOf(Blocks.COMPOSTER)) {
            this.makeBread(p_230251_2_);
            this.compostItems(p_230251_1_, p_230251_2_, globalpos, blockstate);
         }

      }
   }

   private void compostItems(ServerWorld p_234016_1_, DwarfEntity p_234016_2_, GlobalPos p_234016_3_, BlockState p_234016_4_) {
      BlockPos blockpos = p_234016_3_.getPos();
      if (p_234016_4_.get(ComposterBlock.LEVEL) == 8) {
         p_234016_4_ = ComposterBlock.emptyFullComposter(p_234016_4_, p_234016_1_, blockpos);
      }

      int i = 20;
      int j = 10;
      int[] aint = new int[COMPOSTABLE_ITEMS.size()];
      SimpleInventory inventory = p_234016_2_.getInventory();
      int k = inventory.size();
      BlockState blockstate = p_234016_4_;

      for(int l = k - 1; l >= 0 && i > 0; --l) {
         ItemStack itemstack = inventory.getStack(l);
         int i1 = COMPOSTABLE_ITEMS.indexOf(itemstack.getItem());
         if (i1 != -1) {
            int j1 = itemstack.getCount();
            int k1 = aint[i1] + j1;
            aint[i1] = k1;
            int l1 = Math.min(Math.min(k1 - 10, i), j1);
            if (l1 > 0) {
               i -= l1;

               for(int i2 = 0; i2 < l1; ++i2) {
                  blockstate = ComposterBlock.compost(blockstate, p_234016_1_, itemstack, blockpos);
                  if (blockstate.get(ComposterBlock.LEVEL) == 7) {
                     this.spawnComposterFillEffects(p_234016_1_, p_234016_4_, blockpos, blockstate);
                     return;
                  }
               }
            }
         }
      }

      this.spawnComposterFillEffects(p_234016_1_, p_234016_4_, blockpos, blockstate);
   }

   private void spawnComposterFillEffects(ServerWorld p_242308_1_, BlockState p_242308_2_, BlockPos p_242308_3_, BlockState p_242308_4_) {
      p_242308_1_.syncWorldEvent(1500, p_242308_3_, p_242308_4_ != p_242308_2_ ? 1 : 0);
   }

   private void makeBread(DwarfEntity p_234015_1_) {
      SimpleInventory inventory = p_234015_1_.getInventory();
      if (inventory.count(ItemsInit.BEER) <= 3) {
         int i = inventory.count(Items.WHEAT);
         int j = 3;
         int k = 3;
         int l = Math.min(3, i / 3);
         if (l != 0) {
            int i1 = l * 3;
            inventory.removeItem(ItemsInit.BEER, i1);
            ItemStack itemstack = inventory.addStack(new ItemStack(ItemsInit.BEER, l));
            if (!itemstack.isEmpty()) {
               p_234015_1_.dropStack(itemstack, 0.5F);
            }

         }
      }
   }
}