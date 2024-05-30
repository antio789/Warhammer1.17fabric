package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHsetWAlkTargetFromBlockMemory extends MultiTickTask<DwarfEntity> {
   private final MemoryModuleType<GlobalPos> memoryType;
   private final float speedModifier;
   private final int closeEnoughDist;
   private final int tooFarDistance;
   private final int tooLongUnreachableDuration;

   public WHsetWAlkTargetFromBlockMemory(MemoryModuleType<GlobalPos> p_i51501_1_, float p_i51501_2_, int p_i51501_3_, int p_i51501_4_, int p_i51501_5_) {
      super(ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleState.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT, p_i51501_1_, MemoryModuleState.VALUE_PRESENT));
      this.memoryType = p_i51501_1_;
      this.speedModifier = p_i51501_2_;
      this.closeEnoughDist = p_i51501_3_;
      this.tooFarDistance = p_i51501_4_;
      this.tooLongUnreachableDuration = p_i51501_5_;
   }

   private void dropPOI(DwarfEntity p_225457_1_, long p_225457_2_) {
      Brain<?> brain = p_225457_1_.getBrain();
      p_225457_1_.releasePoi(this.memoryType);
      brain.forget(this.memoryType);
      brain.remember(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, p_225457_2_);
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity dwarfEntity, long p_212831_3_) {
      Brain<?> brain = dwarfEntity.getBrain();
      brain.getOptionalRegisteredMemory(this.memoryType).ifPresent((globalPos) -> {
         if (!this.wrongDimension(p_212831_1_, globalPos) && !this.tiredOfTryingToFindTarget(p_212831_1_, dwarfEntity)) {
            if (this.tooFar(dwarfEntity, globalPos)) {
               Vec3d vec3 = null;
               int i = 0;

               for(int j = 1000; i < 1000 && (vec3 == null || this.tooFar(dwarfEntity, GlobalPos.create(p_212831_1_.getRegistryKey(), new BlockPos(vec3)))); ++i) {
                  vec3 = NoPenaltyTargeting.findTo(dwarfEntity, 15, 7, Vec3d.ofBottomCenter(globalPos.getPos()), 1.5707963705062866D);
               }

               if (i == 1000) {
                  this.dropPOI(dwarfEntity, p_212831_3_);
                  return;
               }

               brain.remember(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, this.speedModifier, this.closeEnoughDist));
            } else if (!this.closeEnough(p_212831_1_, dwarfEntity, globalPos)) {
               brain.remember(MemoryModuleType.WALK_TARGET, new WalkTarget(globalPos.getPos(), this.speedModifier, this.closeEnoughDist));
            }
         } else {
            this.dropPOI(dwarfEntity, p_212831_3_);
         }

      });
   }

   private boolean tiredOfTryingToFindTarget(ServerWorld p_223017_1_, DwarfEntity p_223017_2_) {
      Optional<Long> optional = p_223017_2_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
      if (optional.isPresent()) {
         return p_223017_1_.getTime() - optional.get() > (long)this.tooLongUnreachableDuration;
      } else {
         return false;
      }
   }

   private boolean tooFar(DwarfEntity p_242304_1_, GlobalPos p_242304_2_) {
      return p_242304_2_.getPos().getManhattanDistance(p_242304_1_.getBlockPos()) > this.tooFarDistance;
   }

   private boolean wrongDimension(ServerWorld p_242303_1_, GlobalPos p_242303_2_) {
      return p_242303_2_.getDimension() != p_242303_1_.getRegistryKey();
   }

   private boolean closeEnough(ServerWorld p_220547_1_, DwarfEntity p_220547_2_, GlobalPos p_220547_3_) {
      return p_220547_3_.getDimension() == p_220547_1_.getRegistryKey() && p_220547_3_.getPos().getManhattanDistance(p_220547_2_.getBlockPos()) <= this.closeEnoughDist;
   }
}