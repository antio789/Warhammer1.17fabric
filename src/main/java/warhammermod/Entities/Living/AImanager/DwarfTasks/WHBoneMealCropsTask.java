package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHBoneMealCropsTask extends Behavior<DwarfEntity> {
   private long nextWorkCycleTime;
   private long lastBonemealingSession;
   private int timeWorkedSoFar;
   private Optional<BlockPos> cropPos = Optional.empty();

   public WHBoneMealCropsTask() {
      super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      if (p_212832_2_.tickCount % 10 == 0 && (this.lastBonemealingSession == 0L || this.lastBonemealingSession + 160L <= (long)p_212832_2_.tickCount)) {
         if (p_212832_2_.getInventory().countItem(Items.BONE_MEAL) <= 0) {
            return false;
         } else {
            this.cropPos = this.pickNextTarget(p_212832_1_, p_212832_2_);
            return this.cropPos.isPresent();
         }
      } else {
         return false;
      }
   }

   protected boolean canStillUse(ServerLevel p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return this.timeWorkedSoFar < 80 && this.cropPos.isPresent();
   }

   private Optional<BlockPos> pickNextTarget(ServerLevel p_233997_1_, DwarfEntity p_233997_2_) {
      BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
      Optional<BlockPos> optional = Optional.empty();
      int i = 0;

      for(int j = -1; j <= 1; ++j) {
         for(int k = -1; k <= 1; ++k) {
            for(int l = -1; l <= 1; ++l) {
               blockpos$mutable.setWithOffset(p_233997_2_.blockPosition(), j, k, l);
               if (this.validPos(blockpos$mutable, p_233997_1_)) {
                  ++i;
                  if (p_233997_1_.random.nextInt(i) == 0) {
                     optional = Optional.of(blockpos$mutable.immutable());
                  }
               }
            }
         }
      }

      return optional;
   }

   private boolean validPos(BlockPos p_233996_1_, ServerLevel p_233996_2_) {
      BlockState blockstate = p_233996_2_.getBlockState(p_233996_1_);
      Block block = blockstate.getBlock();
      return block instanceof CropBlock && !((CropBlock)block).isMaxAge(blockstate);
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      this.setCurrentCropAsTarget(p_212831_2_);
      p_212831_2_.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BONE_MEAL));
      this.nextWorkCycleTime = p_212831_3_;
      this.timeWorkedSoFar = 0;
   }

   private void setCurrentCropAsTarget(DwarfEntity p_233994_1_) {
      this.cropPos.ifPresent((p_233995_1_) -> {
         BlockPosTracker blockposwrapper = new BlockPosTracker(p_233995_1_);
         p_233994_1_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, blockposwrapper);
         p_233994_1_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(blockposwrapper, 0.5F, 1));
      });
   }

   protected void stop(ServerLevel p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      p_212835_2_.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
      this.lastBonemealingSession = (long)p_212835_2_.tickCount;
   }

   protected void tick(ServerLevel p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      BlockPos blockpos = this.cropPos.get();
      if (p_212833_3_ >= this.nextWorkCycleTime && blockpos.closerThan(p_212833_2_.position(), 1.0D)) {
         ItemStack itemstack = ItemStack.EMPTY;
         SimpleContainer inventory = p_212833_2_.getInventory();
         int i = inventory.getContainerSize();

         for(int j = 0; j < i; ++j) {
            ItemStack itemstack1 = inventory.getItem(j);
            if (itemstack1.getItem() == Items.BONE_MEAL) {
               itemstack = itemstack1;
               break;
            }
         }

         if (!itemstack.isEmpty() && BoneMealItem.growCrop(itemstack, p_212833_1_, blockpos)) {
            p_212833_1_.levelEvent(2005, blockpos, 0);
            this.cropPos = this.pickNextTarget(p_212833_1_, p_212833_2_);
            this.setCurrentCropAsTarget(p_212833_2_);
            this.nextWorkCycleTime = p_212833_3_ + 40L;
         }

         ++this.timeWorkedSoFar;
      }
   }
}