package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;


public class TargetTask extends MultiTickTask<PathAwareEntity> {
   private final MemoryModuleType<? extends LivingEntity> target;


   public TargetTask(MemoryModuleType<? extends LivingEntity> p_i50346_1_) {
      super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT,p_i50346_1_, MemoryModuleState.VALUE_PRESENT));
      this.target = p_i50346_1_;
   }



   public void start(ServerWorld p_212831_1_, PathAwareEntity entityIn, long p_212831_3_) {
      LivingEntity entity = entityIn.getBrain().getOptionalRegisteredMemory(this.target).get();
      if(entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()){
         return;
      }
      setTarget(entityIn,entity);
   }

   public static void setTarget(PathAwareEntity Dwarf, LivingEntity enemy) {
      Dwarf.getBrain().remember(MemoryModuleType.ATTACK_TARGET,enemy);
   }


   private static boolean getTargetIfWithinRange(PathAwareEntity p_242351_0_, LivingEntity p_242351_1_) {
         return p_242351_1_.isInRange(p_242351_0_, 12.0D);
   }

}
