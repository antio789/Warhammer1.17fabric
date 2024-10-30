package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

public class WHResetProfession extends MultiTickTask<DwarfEntity> {
   public WHResetProfession() {
      super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_ABSENT));
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity entity) {
      DwarfProfession prof = entity.getProfession();
      return prof != DwarfProfession.Warrior && prof != DwarfProfession.Lord && entity.getExperience() == 0 && entity.getProfessionLevel() <= 1;
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity entity, long p_212831_3_) {
      entity.setVillagerProfession(DwarfProfession.Warrior);
      entity.refreshBrain(p_212831_1_);
   }
}