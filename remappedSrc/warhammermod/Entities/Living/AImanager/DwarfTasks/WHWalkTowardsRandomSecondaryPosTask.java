package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.Nullable;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.List;
import java.util.Optional;

public class WHWalkTowardsRandomSecondaryPosTask extends MultiTickTask<DwarfEntity> {
   private final MemoryModuleType<List<GlobalPos>> strollToMemoryType;
   private final MemoryModuleType<GlobalPos> mustBeCloseToMemoryType;
   private final float speedModifier;
   private final int closeEnoughDist;
   private final int maxDistanceFromPoi;
   private long nextOkStartTime;
   @Nullable
   private GlobalPos targetPos;

   public WHWalkTowardsRandomSecondaryPosTask(MemoryModuleType<List<GlobalPos>> p_i50340_1_, float p_i50340_2_, int p_i50340_3_, int p_i50340_4_, MemoryModuleType<GlobalPos> p_i50340_5_) {
      super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.REGISTERED, p_i50340_1_, MemoryModuleState.VALUE_PRESENT, p_i50340_5_, MemoryModuleState.VALUE_PRESENT));
      this.strollToMemoryType = p_i50340_1_;
      this.speedModifier = p_i50340_2_;
      this.closeEnoughDist = p_i50340_3_;
      this.maxDistanceFromPoi = p_i50340_4_;
      this.mustBeCloseToMemoryType = p_i50340_5_;
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity p_212832_2_) {
      Optional<List<GlobalPos>> optional = p_212832_2_.getBrain().getOptionalRegisteredMemory(this.strollToMemoryType);
      Optional<GlobalPos> optional1 = p_212832_2_.getBrain().getOptionalRegisteredMemory(this.mustBeCloseToMemoryType);
      if (optional.isPresent() && optional1.isPresent()) {
         List<GlobalPos> list = optional.get();
         if (!list.isEmpty()) {
            this.targetPos = list.get(p_212832_1_.getRandom().nextInt(list.size()));
            return this.targetPos != null && p_212832_1_.getRegistryKey() == this.targetPos.getDimension() && optional1.get().getPos().isWithinDistance(p_212832_2_.getPos(), (double)this.maxDistanceFromPoi);
         }
      }

      return false;
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      if (p_212831_3_ > this.nextOkStartTime && this.targetPos != null) {
         p_212831_2_.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(this.targetPos.getPos(), this.speedModifier, this.closeEnoughDist));
         this.nextOkStartTime = p_212831_3_ + 100L;
      }

   }
}