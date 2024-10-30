package warhammermod.Entities.Living.AImanager.sensor;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LordLastSeenSensor extends Sensor<LivingEntity> {


   public LordLastSeenSensor() {
      super(200);
   }

   protected void sense(ServerWorld p_212872_1_, LivingEntity p_212872_2_) {
      checkForNearbyGolem(p_212872_2_);
   }

   public Set<MemoryModuleType<?>> getOutputMemoryModules() {
      return ImmutableSet.of(MemoryModuleType.MOBS);
   }

   public static void checkForNearbyGolem(LivingEntity entity) {
      Optional<List<LivingEntity>> optional = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.MOBS);
      if (optional.isPresent()) {
         boolean flag = optional.get().stream().anyMatch((livingEntity) -> {
            return livingEntity instanceof DwarfEntity && ((DwarfEntity) livingEntity).getProfession().equals(DwarfProfession.Lord);
         });
         if (flag) {
            golemDetected(entity);
         }

      }
   }

   public static void golemDetected(LivingEntity p_242313_0_) {
      p_242313_0_.getBrain().remember(MemoryModuleType.GOLEM_DETECTED_RECENTLY, true, 600L);
   }
}