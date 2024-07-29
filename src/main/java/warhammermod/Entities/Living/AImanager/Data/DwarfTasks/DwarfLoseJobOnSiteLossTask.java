/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package warhammermod.Entities.Living.AImanager.Data.DwarfTasks;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfessionRecord;
import warhammermod.Entities.Living.DwarfEntity;

public class DwarfLoseJobOnSiteLossTask {
    public static Task<DwarfEntity> create() {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryAbsent(MemoryModuleType.JOB_SITE)).apply(context, jobSite -> (world, entity, time) -> {
            if (entity.getProfession() != DwarfProfessionRecord.Warrior && entity.getProfession() != DwarfProfessionRecord.Lord && entity.getExperience() == 0 && entity.getProfessionLevel() <= 1) {
                entity.setProfession(DwarfProfessionRecord.Warrior.ID());
                entity.reinitializeBrain(world);
                return true;
            }
            return false;
        }));
    }
}

