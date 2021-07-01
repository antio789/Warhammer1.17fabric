package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHsetWAlkTargetFromBlockMemory extends Behavior<DwarfEntity> {
   private final MemoryModuleType<GlobalPos> memoryType;
   private final float speedModifier;
   private final int closeEnoughDist;
   private final int tooFarDistance;
   private final int tooLongUnreachableDuration;

   public WHsetWAlkTargetFromBlockMemory(MemoryModuleType<GlobalPos> p_i51501_1_, float p_i51501_2_, int p_i51501_3_, int p_i51501_4_, int p_i51501_5_) {
      super(ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, p_i51501_1_, MemoryStatus.VALUE_PRESENT));
      this.memoryType = p_i51501_1_;
      this.speedModifier = p_i51501_2_;
      this.closeEnoughDist = p_i51501_3_;
      this.tooFarDistance = p_i51501_4_;
      this.tooLongUnreachableDuration = p_i51501_5_;
   }

   private void dropPOI(DwarfEntity p_225457_1_, long p_225457_2_) {
      Brain<?> brain = p_225457_1_.getBrain();
      p_225457_1_.releasePoi(this.memoryType);
      brain.eraseMemory(this.memoryType);
      brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, p_225457_2_);
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity dwarfEntity, long p_212831_3_) {
      Brain<?> brain = dwarfEntity.getBrain();
      brain.getMemory(this.memoryType).ifPresent((globalPos) -> {
         if (!this.wrongDimension(p_212831_1_, globalPos) && !this.tiredOfTryingToFindTarget(p_212831_1_, dwarfEntity)) {
            if (this.tooFar(dwarfEntity, globalPos)) {
               Vec3 vec3 = null;
               int i = 0;

               for(int j = 1000; i < 1000 && (vec3 == null || this.tooFar(dwarfEntity, GlobalPos.of(p_212831_1_.dimension(), new BlockPos(vec3)))); ++i) {
                  vec3 = DefaultRandomPos.getPosTowards(dwarfEntity, 15, 7, Vec3.atBottomCenterOf(globalPos.pos()), 1.5707963705062866D);
               }

               if (i == 1000) {
                  this.dropPOI(dwarfEntity, p_212831_3_);
                  return;
               }

               brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, this.speedModifier, this.closeEnoughDist));
            } else if (!this.closeEnough(p_212831_1_, dwarfEntity, globalPos)) {
               brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(globalPos.pos(), this.speedModifier, this.closeEnoughDist));
            }
         } else {
            this.dropPOI(dwarfEntity, p_212831_3_);
         }

      });
   }

   private boolean tiredOfTryingToFindTarget(ServerLevel p_223017_1_, DwarfEntity p_223017_2_) {
      Optional<Long> optional = p_223017_2_.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
      if (optional.isPresent()) {
         return p_223017_1_.getGameTime() - optional.get() > (long)this.tooLongUnreachableDuration;
      } else {
         return false;
      }
   }

   private boolean tooFar(DwarfEntity p_242304_1_, GlobalPos p_242304_2_) {
      return p_242304_2_.pos().distManhattan(p_242304_1_.blockPosition()) > this.tooFarDistance;
   }

   private boolean wrongDimension(ServerLevel p_242303_1_, GlobalPos p_242303_2_) {
      return p_242303_2_.dimension() != p_242303_1_.dimension();
   }

   private boolean closeEnough(ServerLevel p_220547_1_, DwarfEntity p_220547_2_, GlobalPos p_220547_3_) {
      return p_220547_3_.dimension() == p_220547_1_.dimension() && p_220547_3_.pos().distManhattan(p_220547_2_.blockPosition()) <= this.closeEnoughDist;
   }
}