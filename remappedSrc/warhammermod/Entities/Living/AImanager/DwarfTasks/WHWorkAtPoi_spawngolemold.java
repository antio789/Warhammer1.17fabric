package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.GlobalPos;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHWorkAtPoi_spawngolemold extends MultiTickTask<DwarfEntity> {
   private long lastCheck;

   public WHWorkAtPoi_spawngolemold() {
      super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED));
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity p_212832_2_) {
      if (p_212832_1_.getTime() - this.lastCheck < 300L) {
         return false;
      } else if (p_212832_1_.random.nextInt(2) != 0) {
         return false;
      } else {
         this.lastCheck = p_212832_1_.getTime();
         GlobalPos globalpos = p_212832_2_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).get();
         return globalpos.getDimension() == p_212832_1_.getRegistryKey() && globalpos.getPos().isWithinDistance(p_212832_2_.getPos(), 1.73D);
      }
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      Brain<DwarfEntity> brain = p_212831_2_.getBrain();
      brain.remember(MemoryModuleType.LAST_WORKED_AT_POI, p_212831_3_);
      brain.getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).ifPresent((p_225460_1_) -> {
         brain.remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(p_225460_1_.getPos()));
      });
      p_212831_2_.playWorkSound();
      this.useWorkstation(p_212831_1_, p_212831_2_);
      if (p_212831_2_.shouldRestock()) {
         p_212831_2_.restock();
      }

   }

   protected void useWorkstation(ServerWorld p_230251_1_, DwarfEntity p_230251_2_) {
   }

   protected boolean canStillUse(ServerWorld p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      Optional<GlobalPos> optional = p_212834_2_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE);
      if (!optional.isPresent()) {
         return false;
      } else {
         GlobalPos globalpos = optional.get();
         return globalpos.getDimension() == p_212834_1_.getRegistryKey() && globalpos.getPos().isWithinDistance(p_212834_2_.getPos(), 1.73D);
      }
   }
}