package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;


public class TargetTask extends Behavior<PathfinderMob> {
   private final MemoryModuleType<? extends LivingEntity> target;


   public TargetTask(MemoryModuleType<? extends LivingEntity> p_i50346_1_) {
      super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,p_i50346_1_, MemoryStatus.VALUE_PRESENT));
      this.target = p_i50346_1_;
   }



   public void start(ServerLevel p_212831_1_, PathfinderMob entityIn, long p_212831_3_) {
      LivingEntity entity = entityIn.getBrain().getMemory(this.target).get();
      if(entity instanceof Player && ((Player) entity).isCreative()){
         return;
      }
      setTarget(entityIn,entity);
   }

   public static void setTarget(PathfinderMob Dwarf, LivingEntity enemy) {
      Dwarf.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET,enemy);
   }


   private static boolean getTargetIfWithinRange(PathfinderMob p_242351_0_, LivingEntity p_242351_1_) {
         return p_242351_1_.closerThan(p_242351_0_, 12.0D);
   }

}
