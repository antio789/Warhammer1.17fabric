package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.pathfinder.Path;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WHFindJobTask_yieldjobsite extends Behavior<DwarfEntity> {
   private final float speedModifier;

   public WHFindJobTask_yieldjobsite(float p_i231545_1_) {
      super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
      this.speedModifier = p_i231545_1_;
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      if (p_212832_2_.isBaby()) {
         return false;
      } else {
         return p_212832_2_.getProfession() == DwarfProfession.Warrior;
      }
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      BlockPos blockpos = p_212831_2_.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().pos();
      Optional<PoiType> optional = p_212831_1_.getPoiManager().getType(blockpos);
      if (optional.isPresent()) {
         this.getNearbyVillagersWithCondition(p_212831_2_, (p_234021_3_) -> {
            return this.nearbyWantsJobsite(optional.get(), p_234021_3_, blockpos);
         }).findFirst().ifPresent((p_234023_4_) -> {
            this.yieldJobSite(p_212831_1_, p_212831_2_, p_234023_4_, blockpos, p_234023_4_.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent());
         });
      }
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

   private boolean nearbyWantsJobsite(PoiType p_234018_1_, DwarfEntity entity, BlockPos p_234018_3_) {
      boolean flag = entity.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
      if (flag) {
         return false;
      } else {
         Optional<GlobalPos> optional = entity.getBrain().getMemory(MemoryModuleType.JOB_SITE);
         DwarfProfession profession = entity.getProfession();
         if (entity.getProfession() != DwarfProfession.Warrior && profession.getPointOfInterest().getPredicate().test(p_234018_1_)) {
            return !optional.isPresent() ? this.canReachPos(entity, p_234018_3_, p_234018_1_) : optional.get().pos().equals(p_234018_3_);
         } else {
            return false;
         }
      }
   }

   private void yieldJobSite(ServerLevel p_234022_1_, DwarfEntity p_234022_2_, DwarfEntity p_234022_3_, BlockPos p_234022_4_, boolean p_234022_5_) {
      this.eraseMemories(p_234022_2_);
      if (!p_234022_5_) {
         BehaviorUtils.setWalkAndLookTargetMemories(p_234022_3_, p_234022_4_, this.speedModifier, 1);
         p_234022_3_.getBrain().setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.of(p_234022_1_.dimension(), p_234022_4_));
         DebugPackets.sendPoiTicketCountPacket(p_234022_1_, p_234022_4_);
      }

   }

   private boolean canReachPos(DwarfEntity p_234020_1_, BlockPos p_234020_2_, PoiType p_234020_3_) {
      Path path = p_234020_1_.getNavigation().createPath(p_234020_2_, p_234020_3_.getValidRange());
      return path != null && path.canReach();
   }

   private void eraseMemories(DwarfEntity p_234019_1_) {
      p_234019_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
      p_234019_1_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
      p_234019_1_.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
   }
}