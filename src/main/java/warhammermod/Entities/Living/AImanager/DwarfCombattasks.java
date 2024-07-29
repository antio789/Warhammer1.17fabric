package warhammermod.Entities.Living.AImanager;


import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.VillagerEntity;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfessionRecord;
import warhammermod.Entities.Living.DwarfEntity;



public class DwarfCombattasks {
    public static void updateActivity(DwarfEntity dwarfEntity) {
        Brain<VillagerEntity> brain = dwarfEntity.getBrain();
        dwarfEntity.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
    }


    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> getAttackPackage(DwarfProfessionRecord p_220636_0_, float p_220636_1_) {
        float f = p_220636_1_ * 1.5F;
        return ImmutableList.of(Pair.of(0, StopPanickingTask.create()),Pair.of(2,MeleeAttackTask.create(20)));
    }





// ,Pair.of(1, new TargetTask(MemoryModuleType.NEAREST_HOSTILE)), Pair.of(3, new MoveToTargetTask(f)), Pair.of(3, new AttackTargetTask(20))




}
