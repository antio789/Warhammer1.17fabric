package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

public class WHResetProfession extends Behavior<DwarfEntity> {
   public WHResetProfession() {
      super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT));
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity entity) {
      DwarfProfession prof = entity.getProfession();
      return prof != DwarfProfession.Warrior && prof != DwarfProfession.Lord && entity.getVillagerXp() == 0 && entity.getProfessionLevel() <= 1;
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity entity, long p_212831_3_) {
      entity.setVillagerProfession(DwarfProfession.Warrior);
      entity.refreshBrain(p_212831_1_);
   }
}