package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;

import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class WHCompetitorScan extends Behavior<DwarfEntity> {
   final DwarfProfession profession;

   public WHCompetitorScan(DwarfProfession p_i231525_1_) {
      super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
      this.profession = p_i231525_1_;
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      GlobalPos globalpos = p_212831_2_.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
      p_212831_1_.getPoiManager().getType(globalpos.pos()).ifPresent((p_233933_3_) -> {
         getNearbyVillagersWithCondition(p_212831_2_, (p_233935_3_) -> {
            return this.competesForSameJobsite(globalpos, p_233933_3_, p_233935_3_);
         }).reduce(p_212831_2_, WHCompetitorScan::selectWinner);
      });
   }

   public static Stream<DwarfEntity> getNearbyVillagersWithCondition(DwarfEntity p_233872_0_, Predicate<DwarfEntity> p_233872_1_) {
      return p_233872_0_.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).map((p_233873_2_) -> {
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
      if (p_233932_0_.getVillagerXp() > p_233932_1_.getVillagerXp()) {
         villagerentity = p_233932_0_;
         villagerentity1 = p_233932_1_;
      } else {
         villagerentity = p_233932_1_;
         villagerentity1 = p_233932_0_;
      }

      villagerentity1.getBrain().eraseMemory(MemoryModuleType.JOB_SITE);
      return villagerentity;
   }

   private boolean competesForSameJobsite(GlobalPos p_233934_1_, PoiType p_233934_2_, DwarfEntity p_233934_3_) {
      return this.hasJobSite(p_233934_3_) && p_233934_1_.equals(p_233934_3_.getBrain().getMemory(MemoryModuleType.JOB_SITE).get()) && this.hasMatchingProfession(p_233934_2_, p_233934_3_.getProfession());
   }

   private boolean hasMatchingProfession(PoiType p_233930_1_, DwarfProfession p_233930_2_) {
      return p_233930_2_.getPointOfInterest().getPredicate().test(p_233930_1_);
   }

   private boolean hasJobSite(DwarfEntity p_233931_1_) {
      return p_233931_1_.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent();
   }
}