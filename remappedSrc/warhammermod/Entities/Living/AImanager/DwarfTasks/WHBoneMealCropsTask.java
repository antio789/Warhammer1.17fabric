package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHBoneMealCropsTask extends MultiTickTask<DwarfEntity> {
   private long nextWorkCycleTime;
   private long lastBonemealingSession;
   private int timeWorkedSoFar;
   private Optional<BlockPos> cropPos = Optional.empty();

   public WHBoneMealCropsTask() {
      super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT));
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity p_212832_2_) {
      if (p_212832_2_.age % 10 == 0 && (this.lastBonemealingSession == 0L || this.lastBonemealingSession + 160L <= (long)p_212832_2_.age)) {
         if (p_212832_2_.getInventory().count(Items.BONE_MEAL) <= 0) {
            return false;
         } else {
            this.cropPos = this.pickNextTarget(p_212832_1_, p_212832_2_);
            return this.cropPos.isPresent();
         }
      } else {
         return false;
      }
   }

   protected boolean canStillUse(ServerWorld p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return this.timeWorkedSoFar < 80 && this.cropPos.isPresent();
   }

   private Optional<BlockPos> pickNextTarget(ServerWorld p_233997_1_, DwarfEntity p_233997_2_) {
      BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
      Optional<BlockPos> optional = Optional.empty();
      int i = 0;

      for(int j = -1; j <= 1; ++j) {
         for(int k = -1; k <= 1; ++k) {
            for(int l = -1; l <= 1; ++l) {
               blockpos$mutable.set(p_233997_2_.getBlockPos(), j, k, l);
               if (this.validPos(blockpos$mutable, p_233997_1_)) {
                  ++i;
                  if (p_233997_1_.random.nextInt(i) == 0) {
                     optional = Optional.of(blockpos$mutable.toImmutable());
                  }
               }
            }
         }
      }

      return optional;
   }

   private boolean validPos(BlockPos p_233996_1_, ServerWorld p_233996_2_) {
      BlockState blockstate = p_233996_2_.getBlockState(p_233996_1_);
      Block block = blockstate.getBlock();
      return block instanceof CropBlock && !((CropBlock)block).isMature(blockstate);
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      this.setCurrentCropAsTarget(p_212831_2_);
      p_212831_2_.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BONE_MEAL));
      this.nextWorkCycleTime = p_212831_3_;
      this.timeWorkedSoFar = 0;
   }

   private void setCurrentCropAsTarget(DwarfEntity p_233994_1_) {
      this.cropPos.ifPresent((p_233995_1_) -> {
         BlockPosLookTarget blockposwrapper = new BlockPosLookTarget(p_233995_1_);
         p_233994_1_.getBrain().remember(MemoryModuleType.LOOK_TARGET, blockposwrapper);
         p_233994_1_.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(blockposwrapper, 0.5F, 1));
      });
   }

   protected void stop(ServerWorld p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      p_212835_2_.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
      this.lastBonemealingSession = (long)p_212835_2_.age;
   }

   protected void tick(ServerWorld p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      BlockPos blockpos = this.cropPos.get();
      if (p_212833_3_ >= this.nextWorkCycleTime && blockpos.isWithinDistance(p_212833_2_.getPos(), 1.0D)) {
         ItemStack itemstack = ItemStack.EMPTY;
         SimpleInventory inventory = p_212833_2_.getInventory();
         int i = inventory.size();

         for(int j = 0; j < i; ++j) {
            ItemStack itemstack1 = inventory.getStack(j);
            if (itemstack1.getItem() == Items.BONE_MEAL) {
               itemstack = itemstack1;
               break;
            }
         }

         if (!itemstack.isEmpty() && BoneMealItem.useOnFertilizable(itemstack, p_212833_1_, blockpos)) {
            p_212833_1_.syncWorldEvent(2005, blockpos, 0);
            this.cropPos = this.pickNextTarget(p_212833_1_, p_212833_2_);
            this.setCurrentCropAsTarget(p_212833_2_);
            this.nextWorkCycleTime = p_212833_3_ + 40L;
         }

         ++this.timeWorkedSoFar;
      }
   }
}