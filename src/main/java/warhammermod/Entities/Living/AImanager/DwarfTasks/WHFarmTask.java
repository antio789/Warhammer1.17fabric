package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;


import java.util.List;

public class WHFarmTask extends Behavior<DwarfEntity> {
   @Nullable
   private BlockPos aboveFarmlandPos;
   private long nextOkStartTime;
   private int timeWorkedSoFar;
   private final List<BlockPos> validFarmlandAroundVillager = Lists.newArrayList();

   public WHFarmTask() {
      super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryStatus.VALUE_PRESENT));
   }

   protected boolean checkExtraStartConditions(ServerLevel serverLevel, DwarfEntity p_212832_2_) {
      if (!serverLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
         return false;
      } else if (p_212832_2_.getProfession() != DwarfProfession.FARMER) {
         return false;
      } else {
         BlockPos.MutableBlockPos blockpos$mutable = p_212832_2_.blockPosition().mutable();
         this.validFarmlandAroundVillager.clear();

         for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
               for(int k = -1; k <= 1; ++k) {
                  blockpos$mutable.set(p_212832_2_.getX() + (double)i, p_212832_2_.getY() + (double)j, p_212832_2_.getZ() + (double)k);
                  if (this.validPos(blockpos$mutable, serverLevel)) {
                     this.validFarmlandAroundVillager.add(new BlockPos(blockpos$mutable));
                  }
               }
            }
         }

         this.aboveFarmlandPos = this.getValidFarmland(serverLevel);
         return this.aboveFarmlandPos != null;
      }
   }

   @Nullable
   private BlockPos getValidFarmland(ServerLevel p_223517_1_) {
      return this.validFarmlandAroundVillager.isEmpty() ? null : this.validFarmlandAroundVillager.get(p_223517_1_.getRandom().nextInt(this.validFarmlandAroundVillager.size()));
   }

   private boolean validPos(BlockPos p_223516_1_, ServerLevel p_223516_2_) {
      BlockState blockstate = p_223516_2_.getBlockState(p_223516_1_);
      Block block = blockstate.getBlock();
      Block block1 = p_223516_2_.getBlockState(p_223516_1_.below()).getBlock();
      return block instanceof CropBlock && ((CropBlock)block).isMaxAge(blockstate) || blockstate.isAir() && block1 instanceof FarmBlock;
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      if (p_212831_3_ > this.nextOkStartTime && this.aboveFarmlandPos != null) {
         p_212831_2_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
         p_212831_2_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
      }

   }

   protected void stop(ServerLevel p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      p_212835_2_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
      p_212835_2_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
      this.timeWorkedSoFar = 0;
      this.nextOkStartTime = p_212835_3_ + 40L;
   }

   protected void tick(ServerLevel p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      if (this.aboveFarmlandPos == null || this.aboveFarmlandPos.closerThan(p_212833_2_.position(), 1.0D)) {
         if (this.aboveFarmlandPos != null && p_212833_3_ > this.nextOkStartTime) {
            BlockState blockstate = p_212833_1_.getBlockState(this.aboveFarmlandPos);
            Block block = blockstate.getBlock();
            Block block1 = p_212833_1_.getBlockState(this.aboveFarmlandPos.below()).getBlock();
            if (block instanceof CropBlock && ((CropBlock)block).isMaxAge(blockstate)) {
               p_212833_1_.destroyBlock(this.aboveFarmlandPos, true, p_212833_2_);
            }

            if (blockstate.isAir() && block1 instanceof FarmBlock && p_212833_2_.hasFarmSeeds()) {
               SimpleContainer inventory = p_212833_2_.getInventory();

               for(int i = 0; i < inventory.getContainerSize(); ++i) {
                  ItemStack itemstack = inventory.getItem(i);
                  boolean flag = false;
                  if (!itemstack.isEmpty()) {
                     if (itemstack.getItem() == Items.WHEAT_SEEDS) {
                        p_212833_1_.setBlock(this.aboveFarmlandPos, Blocks.WHEAT.defaultBlockState(), 3);
                        flag = true;
                     } else if (itemstack.getItem() == Items.POTATO) {
                        p_212833_1_.setBlock(this.aboveFarmlandPos, Blocks.POTATOES.defaultBlockState(), 3);
                        flag = true;
                     } else if (itemstack.getItem() == Items.CARROT) {
                        p_212833_1_.setBlock(this.aboveFarmlandPos, Blocks.CARROTS.defaultBlockState(), 3);
                        flag = true;
                     } else if (itemstack.getItem() == Items.BEETROOT_SEEDS) {
                        p_212833_1_.setBlock(this.aboveFarmlandPos, Blocks.BEETROOTS.defaultBlockState(), 3);
                        flag = true;
                     }
                  }

                  if (flag) {
                     p_212833_1_.playSound((Player)null, (double)this.aboveFarmlandPos.getX(), (double)this.aboveFarmlandPos.getY(), (double)this.aboveFarmlandPos.getZ(), SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
                     itemstack.shrink(1);
                     if (itemstack.isEmpty()) {
                        inventory.setItem(i, ItemStack.EMPTY);
                     }
                     break;
                  }
               }
            }

            if (block instanceof CropBlock && !((CropBlock)block).isMaxAge(blockstate)) {
               this.validFarmlandAroundVillager.remove(this.aboveFarmlandPos);
               this.aboveFarmlandPos = this.getValidFarmland(p_212833_1_);
               if (this.aboveFarmlandPos != null) {
                  this.nextOkStartTime = p_212833_3_ + 20L;
                  p_212833_2_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
                  p_212833_2_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
               }
            }
         }

         ++this.timeWorkedSoFar;
      }
   }

   protected boolean canStillUse(ServerLevel p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return this.timeWorkedSoFar < 200;
   }
}
