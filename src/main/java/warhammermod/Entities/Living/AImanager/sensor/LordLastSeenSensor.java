package warhammermod.Entities.Living.AImanager.sensor;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LordLastSeenSensor extends Sensor<LivingEntity> {


   public LordLastSeenSensor() {
      super(200);
   }

   protected void doTick(ServerLevel p_212872_1_, LivingEntity p_212872_2_) {
      checkForNearbyGolem(p_212872_2_);
   }

   public Set<MemoryModuleType<?>> requires() {
      return ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES);
   }

   public static void checkForNearbyGolem(LivingEntity entity) {
      Optional<List<LivingEntity>> optional = entity.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
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
      p_242313_0_.getBrain().setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, true, 600L);
   }
}