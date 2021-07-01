package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import warhammermod.Entities.Living.DwarfEntity;


public class WHTradeTask_LookAndFollowTradingPlayerSink extends Behavior<DwarfEntity> {
   private final float speedModifier;

   public WHTradeTask_LookAndFollowTradingPlayerSink(float p_i50359_1_) {
      super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Integer.MAX_VALUE);
      this.speedModifier = p_i50359_1_;
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      Player playerentity = p_212832_2_.getTradingPlayer();
      return p_212832_2_.isAlive() && playerentity != null && !p_212832_2_.isInWater() && !p_212832_2_.hurtMarked && p_212832_2_.distanceToSqr(playerentity) <= 16.0D && playerentity.containerMenu != null;
   }

   protected boolean canStillUse(ServerLevel p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return this.checkExtraStartConditions(p_212834_1_, p_212834_2_);
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      this.followPlayer(p_212831_2_);
   }

   protected void stop(ServerLevel p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      Brain<?> brain = p_212835_2_.getBrain();
      brain.eraseMemory(MemoryModuleType.WALK_TARGET);
      brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
   }

   protected void tick(ServerLevel p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      this.followPlayer(p_212833_2_);
   }

   protected boolean timedOut(long p_220383_1_) {
      return false;
   }

   private void followPlayer(DwarfEntity p_220475_1_) {
      Brain<?> brain = p_220475_1_.getBrain();
      brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(p_220475_1_.getTradingPlayer(), false), this.speedModifier, 2));
      brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(p_220475_1_.getTradingPlayer(), true));
   }
}