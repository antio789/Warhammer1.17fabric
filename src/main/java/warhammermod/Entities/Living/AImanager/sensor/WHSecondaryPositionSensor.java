package warhammermod.Entities.Living.AImanager.sensor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.Level;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.List;
import java.util.Set;

public class WHSecondaryPositionSensor extends Sensor<DwarfEntity> {
   public WHSecondaryPositionSensor() {
      super(40);
   }

   protected void doTick(ServerLevel p_212872_1_, DwarfEntity p_212872_2_) {
      ResourceKey<Level> registrykey = p_212872_1_.dimension();
      BlockPos blockpos = p_212872_2_.blockPosition();
      List<GlobalPos> list = Lists.newArrayList();
      int i = 4;

      for(int j = -4; j <= 4; ++j) {
         for(int k = -2; k <= 2; ++k) {
            for(int l = -4; l <= 4; ++l) {
               BlockPos blockpos1 = blockpos.offset(j, k, l);
               if (p_212872_2_.getProfession().getRelatedWorldBlocks().contains(p_212872_1_.getBlockState(blockpos1).getBlock())) {
                  list.add(GlobalPos.of(registrykey, blockpos1));
               }
            }
         }
      }

      Brain<?> brain = p_212872_2_.getBrain();
      if (!list.isEmpty()) {
         brain.setMemory(MemoryModuleType.SECONDARY_JOB_SITE, list);
      } else {
         brain.eraseMemory(MemoryModuleType.SECONDARY_JOB_SITE);
      }

   }

   public Set<MemoryModuleType<?>> requires() {
      return ImmutableSet.of(MemoryModuleType.SECONDARY_JOB_SITE);
   }
}