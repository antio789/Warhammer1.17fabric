package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class WHCompetitorScan extends MultiTickTask<DwarfEntity> {
   final DwarfProfession profession;

   public WHCompetitorScan(DwarfProfession p_i231525_1_) {
      super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.MOBS, MemoryModuleState.VALUE_PRESENT));
      this.profession = p_i231525_1_;
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      GlobalPos globalpos = p_212831_2_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).get();
      p_212831_1_.getPointOfInterestStorage().getType(globalpos.getPos()).ifPresent((p_233933_3_) -> {
         getNearbyVillagersWithCondition(p_212831_2_, (p_233935_3_) -> {
            return this.competesForSameJobsite(globalpos, p_233933_3_, p_233935_3_);
         }).reduce(p_212831_2_, WHCompetitorScan::selectWinner);
      });
   }

   public static Stream<DwarfEntity> getNearbyVillagersWithCondition(DwarfEntity p_233872_0_, Predicate<DwarfEntity> p_233872_1_) {
      return p_233872_0_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.MOBS).map((p_233873_2_) -> {
         return p_233873_2_.stream().filter((p_233871_1_) -> {
            return p_233871_1_ instanceof DwarfEntity && p_233871_1_ != p_233872_0_;
         }).map((p_233859_0_) -> {
            return (DwarfEntity)p_233859_0_;
         }).filter(LivingEntity::isAlive).filter(p_233872_1_);
      }).orElseGet(Stream::empty);
   }


   private static DwarfEntity selectWinner(DwarfEntity p_233932_0_, DwarfEntity p_233932_1_) {
      DwarfEntity villagerentity;
      DwarfEntity villagerentity1;
      if (p_233932_0_.getExperience() > p_233932_1_.getExperience()) {
         villagerentity = p_233932_0_;
         villagerentity1 = p_233932_1_;
      } else {
         villagerentity = p_233932_1_;
         villagerentity1 = p_233932_0_;
      }

      villagerentity1.getBrain().forget(MemoryModuleType.JOB_SITE);
      return villagerentity;
   }

   private boolean competesForSameJobsite(GlobalPos p_233934_1_, PointOfInterestType p_233934_2_, DwarfEntity p_233934_3_) {
      return this.hasJobSite(p_233934_3_) && p_233934_1_.equals(p_233934_3_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).get()) && this.hasMatchingProfession(p_233934_2_, p_233934_3_.getProfession());
   }

   private boolean hasMatchingProfession(PointOfInterestType p_233930_1_, DwarfProfession p_233930_2_) {
      return p_233930_2_.getPointOfInterest().getPredicate().test(p_233930_1_);
   }

   private boolean hasJobSite(DwarfEntity p_233931_1_) {
      return p_233931_1_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).isPresent();
   }
}