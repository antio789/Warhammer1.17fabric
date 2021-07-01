package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import warhammermod.Entities.Living.DwarfEntity;

public class WHPanicTask extends Behavior<DwarfEntity> {
   public WHPanicTask() {
      super(ImmutableMap.of());
   }

   protected boolean canStillUse(ServerLevel p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return isHurt(p_212834_2_) || hasHostile(p_212834_2_);
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      if (isHurt(p_212831_2_) || hasHostile(p_212831_2_)) {

         Brain<?> brain = p_212831_2_.getBrain();
         if (!brain.isActive(Activity.PANIC)) {
            brain.eraseMemory(MemoryModuleType.PATH);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            brain.eraseMemory(MemoryModuleType.BREED_TARGET);
            brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
         }

         brain.setActiveActivityIfPossible(Activity.PANIC);
      }

   }

   protected void tick(ServerLevel p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      if (p_212833_3_ % 100L == 0L) {
         p_212833_2_.spawnGolemIfNeeded(p_212833_1_, p_212833_3_, 3);
      }

   }

   public static boolean hasHostile(LivingEntity p_220513_0_) {
      return p_220513_0_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
   }

   public static boolean isHurt(LivingEntity p_220512_0_) {
      return p_220512_0_.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
   }


}