package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.schedule.Activity;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHFindPotentialJobTask extends Behavior<DwarfEntity> {
   final float speedModifier;

   public WHFindPotentialJobTask(float p_i231519_1_) {
      super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT), 1200);
      this.speedModifier = p_i231519_1_;
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      return p_212832_2_.getBrain().getActiveNonCoreActivity().map((p_233904_0_) -> {
         return p_233904_0_ == Activity.IDLE || p_233904_0_ == Activity.WORK || p_233904_0_ == Activity.PLAY;
      }).orElse(true);
   }

   protected boolean canStillUse(ServerLevel p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return p_212834_2_.getBrain().hasMemoryValue(MemoryModuleType.POTENTIAL_JOB_SITE);
   }

   protected void tick(ServerLevel p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      BehaviorUtils.setWalkAndLookTargetMemories(p_212833_2_, p_212833_2_.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().pos(), this.speedModifier, 1);
   }

   protected void stop(ServerLevel p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      Optional<GlobalPos> optional = p_212835_2_.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
      optional.ifPresent((p_233905_1_) -> {
         BlockPos blockpos = p_233905_1_.pos();
         ServerLevel serverworld = p_212835_1_.getServer().getLevel(p_233905_1_.dimension());
         if (serverworld != null) {
            PoiManager pointofinterestmanager = serverworld.getPoiManager();
            if (pointofinterestmanager.exists(blockpos, (p_241377_0_) -> {
               return true;
            })) {
               pointofinterestmanager.release(blockpos);
            }

            DebugPackets.sendPoiTicketCountPacket(p_212835_1_, blockpos);
         }
      });
      p_212835_2_.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
   }
}