package warhammermod.Entities.Living.AImanager.sensor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.List;
import java.util.Set;

public class WHSecondaryPositionSensor extends Sensor<DwarfEntity> {
   public WHSecondaryPositionSensor() {
      super(40);
   }

   protected void doTick(ServerWorld p_212872_1_, DwarfEntity p_212872_2_) {
      RegistryKey<World> registrykey = p_212872_1_.getRegistryKey();
      BlockPos blockpos = p_212872_2_.getBlockPos();
      List<GlobalPos> list = Lists.newArrayList();
      int i = 4;

      for(int j = -4; j <= 4; ++j) {
         for(int k = -2; k <= 2; ++k) {
            for(int l = -4; l <= 4; ++l) {
               BlockPos blockpos1 = blockpos.add(j, k, l);
               if (p_212872_2_.getProfession().getRelatedWorldBlocks().contains(p_212872_1_.getBlockState(blockpos1).getBlock())) {
                  list.add(GlobalPos.create(registrykey, blockpos1));
               }
            }
         }
      }

      Brain<?> brain = p_212872_2_.getBrain();
      if (!list.isEmpty()) {
         brain.remember(MemoryModuleType.SECONDARY_JOB_SITE, list);
      } else {
         brain.forget(MemoryModuleType.SECONDARY_JOB_SITE);
      }

   }

   public Set<MemoryModuleType<?>> getOutputMemoryModules() {
      return ImmutableSet.of(MemoryModuleType.SECONDARY_JOB_SITE);
   }
}