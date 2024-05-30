package warhammermod.Entities.Living.AImanager.sensor;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;
import warhammermod.utils.Registry.Entityinit;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class dwarfBabiesSensor extends Sensor<LivingEntity> {
   public Set<MemoryModuleType<?>> getOutputMemoryModules() {
      return ImmutableSet.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
   }

   protected void sense(ServerWorld p_212872_1_, LivingEntity p_212872_2_) {
      p_212872_2_.getBrain().remember(MemoryModuleType.VISIBLE_VILLAGER_BABIES, this.getNearestVillagerBabies(p_212872_2_));
   }

   private List<LivingEntity> getNearestVillagerBabies(LivingEntity p_220994_1_) {
      return this.getVisibleEntities(p_220994_1_).stream().filter(this::isVillagerBaby).collect(Collectors.toList());
   }

   private boolean isVillagerBaby(LivingEntity p_220993_1_) {
      return p_220993_1_.getType() == Entityinit.DWARF && p_220993_1_.isBaby();
   }

   private List<LivingEntity> getVisibleEntities(LivingEntity p_220992_1_) {
      return p_220992_1_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.VISIBLE_MOBS).orElse(Lists.newArrayList());
   }
}