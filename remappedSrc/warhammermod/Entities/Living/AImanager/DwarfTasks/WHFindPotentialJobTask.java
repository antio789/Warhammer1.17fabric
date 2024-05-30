package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestStorage;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHFindPotentialJobTask extends MultiTickTask<DwarfEntity> {
   final float speedModifier;

   public WHFindPotentialJobTask(float p_i231519_1_) {
      super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleState.VALUE_PRESENT), 1200);
      this.speedModifier = p_i231519_1_;
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity p_212832_2_) {
      return p_212832_2_.getBrain().getFirstPossibleNonCoreActivity().map((p_233904_0_) -> {
         return p_233904_0_ == Activity.IDLE || p_233904_0_ == Activity.WORK || p_233904_0_ == Activity.PLAY;
      }).orElse(true);
   }

   protected boolean canStillUse(ServerWorld p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return p_212834_2_.getBrain().hasMemoryModule(MemoryModuleType.POTENTIAL_JOB_SITE);
   }

   protected void tick(ServerWorld p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      LookTargetUtil.walkTowards(p_212833_2_, p_212833_2_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().getPos(), this.speedModifier, 1);
   }

   protected void stop(ServerWorld p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      Optional<GlobalPos> optional = p_212835_2_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
      optional.ifPresent((p_233905_1_) -> {
         BlockPos blockpos = p_233905_1_.getPos();
         ServerWorld serverworld = p_212835_1_.getServer().getWorld(p_233905_1_.getDimension());
         if (serverworld != null) {
            PointOfInterestStorage pointofinterestmanager = serverworld.getPointOfInterestStorage();
            if (pointofinterestmanager.test(blockpos, (p_241377_0_) -> {
               return true;
            })) {
               pointofinterestmanager.releaseTicket(blockpos);
            }

            DebugInfoSender.sendPointOfInterest(p_212835_1_, blockpos);
         }
      });
      p_212835_2_.getBrain().forget(MemoryModuleType.POTENTIAL_JOB_SITE);
   }
}