package warhammermod.Entities.Living.AImanager;


import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.AImanager.DwarfTasks.TargetTask;
import warhammermod.Entities.Living.AImanager.DwarfTasks.WHAttackTargetTask;
import warhammermod.Entities.Living.AImanager.DwarfTasks.WHClearHurtTask;
import warhammermod.Entities.Living.DwarfEntity;

import static warhammermod.Entities.Living.AImanager.DwarfVillagerTasks.getMinimalLookBehavior;


public class DwarfCombattasks {
    public static void updateActivity(DwarfEntity p_234486_0_) {
        Brain<DwarfEntity> brain = p_234486_0_.getBrain();
        p_234486_0_.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }


    public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getAttackPackage(DwarfProfession p_220636_0_, float p_220636_1_) {
        float f = p_220636_1_ * 1.5F;
        return ImmutableList.of(Pair.of(0, new WHClearHurtTask()), Pair.of(1, new TargetTask(MemoryModuleType.NEAREST_HOSTILE)),Pair.of(1, new TargetTask(MemoryModuleType.HURT_BY_ENTITY)),Pair.of(3,new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0F)),Pair.of(2,new WHAttackTargetTask(20)), getMinimalLookBehavior());
    }



// ,Pair.of(1, new TargetTask(MemoryModuleType.NEAREST_HOSTILE)), Pair.of(3, new MoveToTargetTask(f)), Pair.of(3, new AttackTargetTask(20))




}
