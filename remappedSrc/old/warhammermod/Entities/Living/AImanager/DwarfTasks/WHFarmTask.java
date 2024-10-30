package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.List;

public class WHFarmTask extends MultiTickTask<DwarfEntity> {
   @Nullable
   private BlockPos aboveFarmlandPos;
   private long nextOkStartTime;
   private int timeWorkedSoFar;
   private final List<BlockPos> validFarmlandAroundVillager = Lists.newArrayList();

   public WHFarmTask() {
      super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleState.VALUE_PRESENT));
   }

   protected boolean checkExtraStartConditions(ServerWorld serverLevel, DwarfEntity p_212832_2_) {
      if (!serverLevel.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
         return false;
      } else if (p_212832_2_.getProfession() != DwarfProfession.FARMER) {
         return false;
      } else {
         BlockPos.Mutable blockpos$mutable = p_212832_2_.getBlockPos().mutableCopy();
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
   private BlockPos getValidFarmland(ServerWorld p_223517_1_) {
      return this.validFarmlandAroundVillager.isEmpty() ? null : this.validFarmlandAroundVillager.get(p_223517_1_.getRandom().nextInt(this.validFarmlandAroundVillager.size()));
   }

   private boolean validPos(BlockPos p_223516_1_, ServerWorld p_223516_2_) {
      BlockState blockstate = p_223516_2_.getBlockState(p_223516_1_);
      Block block = blockstate.getBlock();
      Block block1 = p_223516_2_.getBlockState(p_223516_1_.down()).getBlock();
      return block instanceof CropBlock && ((CropBlock)block).isMature(blockstate) || blockstate.isAir() && block1 instanceof FarmlandBlock;
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      if (p_212831_3_ > this.nextOkStartTime && this.aboveFarmlandPos != null) {
         p_212831_2_.getBrain().remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(this.aboveFarmlandPos));
         p_212831_2_.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosLookTarget(this.aboveFarmlandPos), 0.5F, 1));
      }

   }

   protected void stop(ServerWorld p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      p_212835_2_.getBrain().forget(MemoryModuleType.LOOK_TARGET);
      p_212835_2_.getBrain().forget(MemoryModuleType.WALK_TARGET);
      this.timeWorkedSoFar = 0;
      this.nextOkStartTime = p_212835_3_ + 40L;
   }

   protected void tick(ServerWorld p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      if (this.aboveFarmlandPos == null || this.aboveFarmlandPos.isWithinDistance(p_212833_2_.getPos(), 1.0D)) {
         if (this.aboveFarmlandPos != null && p_212833_3_ > this.nextOkStartTime) {
            BlockState blockstate = p_212833_1_.getBlockState(this.aboveFarmlandPos);
            Block block = blockstate.getBlock();
            Block block1 = p_212833_1_.getBlockState(this.aboveFarmlandPos.down()).getBlock();
            if (block instanceof CropBlock && ((CropBlock)block).isMature(blockstate)) {
               p_212833_1_.breakBlock(this.aboveFarmlandPos, true, p_212833_2_);
            }

            if (blockstate.isAir() && block1 instanceof FarmlandBlock && p_212833_2_.hasFarmSeeds()) {
               SimpleInventory inventory = p_212833_2_.getInventory();

               for(int i = 0; i < inventory.size(); ++i) {
                  ItemStack itemstack = inventory.getStack(i);
                  boolean flag = false;
                  if (!itemstack.isEmpty()) {
                     if (itemstack.getItem() == Items.WHEAT_SEEDS) {
                        p_212833_1_.setBlockState(this.aboveFarmlandPos, Blocks.WHEAT.getDefaultState(), 3);
                        flag = true;
                     } else if (itemstack.getItem() == Items.POTATO) {
                        p_212833_1_.setBlockState(this.aboveFarmlandPos, Blocks.POTATOES.getDefaultState(), 3);
                        flag = true;
                     } else if (itemstack.getItem() == Items.CARROT) {
                        p_212833_1_.setBlockState(this.aboveFarmlandPos, Blocks.CARROTS.getDefaultState(), 3);
                        flag = true;
                     } else if (itemstack.getItem() == Items.BEETROOT_SEEDS) {
                        p_212833_1_.setBlockState(this.aboveFarmlandPos, Blocks.BEETROOTS.getDefaultState(), 3);
                        flag = true;
                     }
                  }

                  if (flag) {
                     p_212833_1_.playSound((PlayerEntity)null, (double)this.aboveFarmlandPos.getX(), (double)this.aboveFarmlandPos.getY(), (double)this.aboveFarmlandPos.getZ(), SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                     itemstack.decrement(1);
                     if (itemstack.isEmpty()) {
                        inventory.setStack(i, ItemStack.EMPTY);
                     }
                     break;
                  }
               }
            }

            if (block instanceof CropBlock && !((CropBlock)block).isMature(blockstate)) {
               this.validFarmlandAroundVillager.remove(this.aboveFarmlandPos);
               this.aboveFarmlandPos = this.getValidFarmland(p_212833_1_);
               if (this.aboveFarmlandPos != null) {
                  this.nextOkStartTime = p_212833_3_ + 20L;
                  p_212833_2_.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosLookTarget(this.aboveFarmlandPos), 0.5F, 1));
                  p_212833_2_.getBrain().remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(this.aboveFarmlandPos));
               }
            }
         }

         ++this.timeWorkedSoFar;
      }
   }

   protected boolean canStillUse(ServerWorld p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return this.timeWorkedSoFar < 200;
   }
}
