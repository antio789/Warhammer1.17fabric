package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.apache.logging.log4j.core.jmx.Server;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHWorkAtPoi_spawngolemold extends Behavior<DwarfEntity> {
   private long lastCheck;

   public WHWorkAtPoi_spawngolemold() {
      super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      if (p_212832_1_.getGameTime() - this.lastCheck < 300L) {
         return false;
      } else if (p_212832_1_.random.nextInt(2) != 0) {
         return false;
      } else {
         this.lastCheck = p_212832_1_.getGameTime();
         GlobalPos globalpos = p_212832_2_.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
         return globalpos.dimension() == p_212832_1_.dimension() && globalpos.pos().closerThan(p_212832_2_.position(), 1.73D);
      }
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      Brain<DwarfEntity> brain = p_212831_2_.getBrain();
      brain.setMemory(MemoryModuleType.LAST_WORKED_AT_POI, p_212831_3_);
      brain.getMemory(MemoryModuleType.JOB_SITE).ifPresent((p_225460_1_) -> {
         brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(p_225460_1_.pos()));
      });
      p_212831_2_.playWorkSound();
      this.useWorkstation(p_212831_1_, p_212831_2_);
      if (p_212831_2_.shouldRestock()) {
         p_212831_2_.restock();
      }

   }

   protected void useWorkstation(ServerLevel p_230251_1_, DwarfEntity p_230251_2_) {
   }

   protected boolean canStillUse(ServerLevel p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      Optional<GlobalPos> optional = p_212834_2_.getBrain().getMemory(MemoryModuleType.JOB_SITE);
      if (!optional.isPresent()) {
         return false;
      } else {
         GlobalPos globalpos = optional.get();
         return globalpos.dimension() == p_212834_1_.dimension() && globalpos.pos().closerThan(p_212834_2_.position(), 1.73D);
      }
   }
}