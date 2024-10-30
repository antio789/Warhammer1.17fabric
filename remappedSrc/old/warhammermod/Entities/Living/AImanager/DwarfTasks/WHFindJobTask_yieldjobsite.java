package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WHFindJobTask_yieldjobsite extends MultiTickTask<DwarfEntity> {
   private final float speedModifier;

   public WHFindJobTask_yieldjobsite(float p_i231545_1_) {
      super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.MOBS, MemoryModuleState.VALUE_PRESENT));
      this.speedModifier = p_i231545_1_;
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity p_212832_2_) {
      if (p_212832_2_.isBaby()) {
         return false;
      } else {
         return p_212832_2_.getProfession() == DwarfProfession.Warrior;
      }
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      BlockPos blockpos = p_212831_2_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().getPos();
      Optional<PointOfInterestType> optional = p_212831_1_.getPointOfInterestStorage().getType(blockpos);
      if (optional.isPresent()) {
         this.getNearbyVillagersWithCondition(p_212831_2_, (p_234021_3_) -> {
            return this.nearbyWantsJobsite(optional.get(), p_234021_3_, blockpos);
         }).findFirst().ifPresent((p_234023_4_) -> {
            this.yieldJobSite(p_212831_1_, p_212831_2_, p_234023_4_, blockpos, p_234023_4_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).isPresent());
         });
      }
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

   private boolean nearbyWantsJobsite(PointOfInterestType p_234018_1_, DwarfEntity entity, BlockPos p_234018_3_) {
      boolean flag = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
      if (flag) {
         return false;
      } else {
         Optional<GlobalPos> optional = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE);
         DwarfProfession profession = entity.getProfession();
         if (entity.getProfession() != DwarfProfession.Warrior && profession.getPointOfInterest().getPredicate().test(p_234018_1_)) {
            return !optional.isPresent() ? this.canReachPos(entity, p_234018_3_, p_234018_1_) : optional.get().getPos().equals(p_234018_3_);
         } else {
            return false;
         }
      }
   }

   private void yieldJobSite(ServerWorld p_234022_1_, DwarfEntity p_234022_2_, DwarfEntity p_234022_3_, BlockPos p_234022_4_, boolean p_234022_5_) {
      this.eraseMemories(p_234022_2_);
      if (!p_234022_5_) {
         LookTargetUtil.walkTowards(p_234022_3_, p_234022_4_, this.speedModifier, 1);
         p_234022_3_.getBrain().remember(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.create(p_234022_1_.getRegistryKey(), p_234022_4_));
         DebugInfoSender.sendPointOfInterest(p_234022_1_, p_234022_4_);
      }

   }

   private boolean canReachPos(DwarfEntity p_234020_1_, BlockPos p_234020_2_, PointOfInterestType p_234020_3_) {
      Path path = p_234020_1_.getNavigation().findPathTo(p_234020_2_, p_234020_3_.getValidRange());
      return path != null && path.reachesTarget();
   }

   private void eraseMemories(DwarfEntity p_234019_1_) {
      p_234019_1_.getBrain().forget(MemoryModuleType.WALK_TARGET);
      p_234019_1_.getBrain().forget(MemoryModuleType.LOOK_TARGET);
      p_234019_1_.getBrain().forget(MemoryModuleType.POTENTIAL_JOB_SITE);
   }
}