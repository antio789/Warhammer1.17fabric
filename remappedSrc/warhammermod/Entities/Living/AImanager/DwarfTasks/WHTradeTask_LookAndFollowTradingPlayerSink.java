package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import warhammermod.Entities.Living.DwarfEntity;


public class WHTradeTask_LookAndFollowTradingPlayerSink extends MultiTickTask<DwarfEntity> {
   private final float speedModifier;

   public WHTradeTask_LookAndFollowTradingPlayerSink(float p_i50359_1_) {
      super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED), Integer.MAX_VALUE);
      this.speedModifier = p_i50359_1_;
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity p_212832_2_) {
      PlayerEntity playerentity = p_212832_2_.getCustomer();
      return p_212832_2_.isAlive() && playerentity != null && !p_212832_2_.isTouchingWater() && !p_212832_2_.velocityModified && p_212832_2_.squaredDistanceTo(playerentity) <= 16.0D && playerentity.currentScreenHandler != null;
   }

   protected boolean canStillUse(ServerWorld p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return this.checkExtraStartConditions(p_212834_1_, p_212834_2_);
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      this.followPlayer(p_212831_2_);
   }

   protected void stop(ServerWorld p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      Brain<?> brain = p_212835_2_.getBrain();
      brain.forget(MemoryModuleType.WALK_TARGET);
      brain.forget(MemoryModuleType.LOOK_TARGET);
   }

   protected void tick(ServerWorld p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      this.followPlayer(p_212833_2_);
   }

   protected boolean isTimeLimitExceeded(long p_220383_1_) {
      return false;
   }

   private void followPlayer(DwarfEntity p_220475_1_) {
      Brain<?> brain = p_220475_1_.getBrain();
      brain.remember(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityLookTarget(p_220475_1_.getCustomer(), false), this.speedModifier, 2));
      brain.remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(p_220475_1_.getCustomer(), true));
   }
}