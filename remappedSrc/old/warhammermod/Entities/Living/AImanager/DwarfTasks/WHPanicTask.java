package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import warhammermod.Entities.Living.DwarfEntity;

public class WHPanicTask extends MultiTickTask<DwarfEntity> {
   public WHPanicTask() {
      super(ImmutableMap.of());
   }

   protected boolean canStillUse(ServerWorld p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return isHurt(p_212834_2_) || hasHostile(p_212834_2_);
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      if (isHurt(p_212831_2_) || hasHostile(p_212831_2_)) {

         Brain<?> brain = p_212831_2_.getBrain();
         if (!brain.hasActivity(Activity.PANIC)) {
            brain.forget(MemoryModuleType.PATH);
            brain.forget(MemoryModuleType.WALK_TARGET);
            brain.forget(MemoryModuleType.LOOK_TARGET);
            brain.forget(MemoryModuleType.BREED_TARGET);
            brain.forget(MemoryModuleType.INTERACTION_TARGET);
         }

         brain.doExclusively(Activity.PANIC);
      }

   }

   protected void tick(ServerWorld p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      if (p_212833_3_ % 100L == 0L) {
         p_212833_2_.spawnGolemIfNeeded(p_212833_1_, p_212833_3_, 3);
      }

   }

   public static boolean hasHostile(LivingEntity p_220513_0_) {
      return p_220513_0_.getBrain().hasMemoryModule(MemoryModuleType.NEAREST_HOSTILE);
   }

   public static boolean isHurt(LivingEntity p_220512_0_) {
      return p_220512_0_.getBrain().hasMemoryModule(MemoryModuleType.HURT_BY);
   }


}