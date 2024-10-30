package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.PanicTask;
import net.minecraft.server.world.ServerWorld;
import warhammermod.Entities.Living.DwarfEntity;

public class WHClearHurtTask extends MultiTickTask<DwarfEntity> {
   public WHClearHurtTask() {
      super(ImmutableMap.of());
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      boolean flag = PanicTask.wasHurt(p_212831_2_) || PanicTask.isHostileNearby(p_212831_2_) || isCloseToEntityThatHurtMe(p_212831_2_);
      if (!flag) {

         p_212831_2_.getBrain().forget(MemoryModuleType.HURT_BY);
         p_212831_2_.getBrain().forget(MemoryModuleType.HURT_BY_ENTITY);
         p_212831_2_.getBrain().forget(MemoryModuleType.ATTACK_TARGET);
         p_212831_2_.getBrain().refreshActivities(p_212831_1_.getTimeOfDay(), p_212831_1_.getTime());
      }

   }

   private static boolean isCloseToEntityThatHurtMe(DwarfEntity p_220394_0_) {
      return p_220394_0_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.HURT_BY_ENTITY).filter((p_223523_1_) -> {
         return p_223523_1_.squaredDistanceTo(p_220394_0_) <= 36.0D;
      }).isPresent();
   }
}