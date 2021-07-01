package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.pathfinder.Path;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.utils.Registry.Entityinit;


import java.util.Optional;

;

public class WHDwarfMakeLove extends Behavior<DwarfEntity> {
   private long birthTimestamp;

   public WHDwarfMakeLove() {
      super(ImmutableMap.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT), 350, 350);
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      return this.isBreedingPossible(p_212832_2_);
   }

   protected boolean canStillUse(ServerLevel p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return p_212834_3_ <= this.birthTimestamp && this.isBreedingPossible(p_212834_2_);
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      AgeableMob ageableentity = p_212831_2_.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
      BehaviorUtils.lockGazeAndWalkToEachOther(p_212831_2_, ageableentity, 0.5F);
      p_212831_1_.broadcastEntityEvent(ageableentity, (byte)18);
      p_212831_1_.broadcastEntityEvent(p_212831_2_, (byte)18);
      int i = 275 + p_212831_2_.getRandom().nextInt(50);
      this.birthTimestamp = p_212831_3_ + (long)i;
   }

   protected void tick(ServerLevel p_212833_1_, DwarfEntity owner, long p_212833_3_) {
      DwarfEntity dwarfEntity = (DwarfEntity)owner.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
      if (!(owner.distanceToSqr(dwarfEntity) > 5.0D)) {
         BehaviorUtils.lockGazeAndWalkToEachOther(owner,dwarfEntity, 0.5F);
         if (p_212833_3_ >= this.birthTimestamp) {
            owner.eatAndDigestFood();
            dwarfEntity.eatAndDigestFood();
            this.tryToGiveBirth(p_212833_1_, owner, dwarfEntity);
         } else if (owner.getRandom().nextInt(35) == 0) {
            p_212833_1_.broadcastEntityEvent(dwarfEntity, (byte)12);
            p_212833_1_.broadcastEntityEvent(owner, (byte)12);
         }

      }
   }

   private void tryToGiveBirth(ServerLevel p_223521_1_, DwarfEntity p_223521_2_, DwarfEntity p_223521_3_) {
      Optional<BlockPos> optional = this.takeVacantBed(p_223521_1_, p_223521_2_);
      if (!optional.isPresent()) {
         p_223521_1_.broadcastEntityEvent(p_223521_3_, (byte)13);
         p_223521_1_.broadcastEntityEvent(p_223521_2_, (byte)13);
      } else {
         Optional<DwarfEntity> optional1 = this.breed(p_223521_1_, p_223521_2_, p_223521_3_);
         if (optional1.isPresent()) {
            this.giveBedToChild(p_223521_1_, optional1.get(), optional.get());
         } else {
            p_223521_1_.getPoiManager().release(optional.get());
            DebugPackets.sendPoiTicketCountPacket(p_223521_1_, optional.get());
         }
      }

   }

   protected void stop(ServerLevel p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      p_212835_2_.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
   }

   private boolean isBreedingPossible(DwarfEntity p_220478_1_) {
      Brain<DwarfEntity> brain = p_220478_1_.getBrain();
      Optional<AgeableMob> optional = brain.getMemory(MemoryModuleType.BREED_TARGET).filter((p_233999_0_) -> {
         return p_233999_0_.getType() == Entityinit.DWARF;
      });
      if (!optional.isPresent()) {
         return false;
      } else {
         return BehaviorUtils.targetIsValid(brain, MemoryModuleType.BREED_TARGET, Entityinit.DWARF) && p_220478_1_.canBreed() && optional.get().canBreed();
      }
   }

   private Optional<BlockPos> takeVacantBed(ServerLevel p_220479_1_, DwarfEntity p_220479_2_) {
      return p_220479_1_.getPoiManager().take(PoiType.HOME.getPredicate(), (p_220481_2_) -> {
         return this.canReach(p_220479_2_, p_220481_2_);
      }, p_220479_2_.blockPosition(), 48);
   }

   private boolean canReach(DwarfEntity p_223520_1_, BlockPos p_223520_2_) {
      Path path = p_223520_1_.getNavigation().createPath(p_223520_2_, PoiType.HOME.getValidRange());
      return path != null && path.canReach();
   }

   private Optional<DwarfEntity> breed(ServerLevel p_242307_1_, DwarfEntity p_242307_2_, DwarfEntity p_242307_3_) {
      DwarfEntity dwarfEntity = p_242307_2_.getBreedOffspring(p_242307_1_, p_242307_3_);
      if (dwarfEntity == null) {
         return Optional.empty();
      } else {
         p_242307_2_.setAge(8000);
         p_242307_3_.setAge(8000);
         dwarfEntity.setAge(-28000);
         dwarfEntity.moveTo(p_242307_2_.getX(), p_242307_2_.getY(), p_242307_2_.getZ(), 0.0F, 0.0F);
         p_242307_1_.addFreshEntityWithPassengers(dwarfEntity);
         p_242307_1_.broadcastEntityEvent(dwarfEntity, (byte)12);
         return Optional.of(dwarfEntity);
      }
   }

   private void giveBedToChild(ServerLevel p_220477_1_, DwarfEntity p_220477_2_, BlockPos p_220477_3_) {
      GlobalPos globalpos = GlobalPos.of(p_220477_1_.dimension(), p_220477_3_);
      p_220477_2_.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
   }
}