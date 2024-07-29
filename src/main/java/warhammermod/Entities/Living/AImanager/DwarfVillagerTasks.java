package warhammermod.Entities.Living.AImanager;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.poi.PointOfInterestTypes;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfessionRecord;
import warhammermod.Entities.Living.AImanager.Data.DwarfTasks.*;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.utils.Registry.Entityinit;

import java.util.Optional;


public class DwarfVillagerTasks {
   /*
   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> createCoreTasks(DwarfProfession profession, float p_220638_1_) {
      return ImmutableList.of(Pair.of(0, new StayAboveWaterTask(0.8F)), Pair.of(0, OpenDoorsTask().create()), Pair.of(0, new LookAroundTask(45, 90)), Pair.of(0, new WHPanicTask()), Pair.of(0, new WakeUpTask()), Pair.of(0, new HideWhenBellRingsTask()), Pair.of(0, new StartRaidTask()), Pair.of(0, new ForgetCompletedPointOfInterestTask(profession.getPointOfInterest(), MemoryModuleType.JOB_SITE)), Pair.of(0, new ForgetCompletedPointOfInterestTask(profession.getPointOfInterest(), MemoryModuleType.POTENTIAL_JOB_SITE)), Pair.of(1, new WanderAroundTask()), Pair.of(2, new WHCompetitorScan(profession)), Pair.of(3, new WHTradeTask_LookAndFollowTradingPlayerSink(p_220638_1_)), Pair.of(5, new WalkToNearestVisibleWantedItemTask<LivingEntity>(p_220638_1_, false, 4)), Pair.of(6, new FindPointOfInterestTask(profession.getPointOfInterest(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())), Pair.of(7, new WHFindPotentialJobTask(p_220638_1_)), Pair.of(8, new WHFindJobTask_yieldjobsite(p_220638_1_)), Pair.of(10, new FindPointOfInterestTask(PointOfInterestType.HOME, MemoryModuleType.HOME, false, Optional.of((byte)14))), Pair.of(10, new FindPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte)14))), Pair.of(10, new WHAssignProfessionTask()), Pair.of(10, new WHResetProfession()));
   }
   */
   public static ImmutableList<Pair<Integer, ? extends Task<? super DwarfEntity>>> createCoreTasks(DwarfProfessionRecord profession, float speed) {
      return ImmutableList.of(Pair.of(0, new StayAboveWaterTask(0.8f)), Pair.of(0, OpenDoorsTask.create()), Pair.of(0, new LookAroundTask(45, 90)), Pair.of(0, new PanicTask()), Pair.of(0, WakeUpTask.create()), Pair.of(0, HideWhenBellRingsTask.create()), Pair.of(0, StartRaidTask.create()), Pair.of(0, ForgetCompletedPointOfInterestTask.create(profession.heldWorkstation(), MemoryModuleType.JOB_SITE)), Pair.of(0, ForgetCompletedPointOfInterestTask.create(profession.acquirableWorkstation(), MemoryModuleType.POTENTIAL_JOB_SITE)), Pair.of(1, new MoveToTargetTask()), Pair.of(2, DwarfWorkStationCompetitionTask.create()), Pair.of(3, new FollowCustomerTask(speed)), new Pair[]{Pair.of(5, WalkToNearestVisibleWantedItemTask.create(speed, false, 4)), Pair.of(6, FindPointOfInterestTask.create(profession.acquirableWorkstation(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())), Pair.of(7, new WalkTowardJobSiteTask(speed)), Pair.of(8, TakeJobSiteTask.create(speed)), Pair.of(10, FindPointOfInterestTask.create(poiType -> poiType.matchesKey(PointOfInterestTypes.HOME), MemoryModuleType.HOME, false, Optional.of((byte)14))), Pair.of(10, FindPointOfInterestTask.create(poiType -> poiType.matchesKey(PointOfInterestTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.of((byte)14))), Pair.of(10, DwarfGoToWorkTask.create()), Pair.of(10, DwarfLoseJobOnSiteLossTask.create())});
   }

   public static ImmutableList<Pair<Integer, ? extends Task<? super DwarfEntity>>> createWorkTasks(DwarfProfessionRecord profession, float speed) {
      VillagerWorkTask villagerWorkTask = profession == DwarfProfessionRecord.FARMER ? new DwarfFarmerWorkTask() : new VillagerWorkTask();
      return ImmutableList.of(createBusyFollowTask(), Pair.of(5, new RandomTask(ImmutableList.of(Pair.of(villagerWorkTask, 7), Pair.of(GoToIfNearbyTask.create(MemoryModuleType.JOB_SITE, 0.4f, 4), 2), Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.JOB_SITE, 0.4f, 1, 10), 5), Pair.of(GoToSecondaryPositionTask.create(MemoryModuleType.SECONDARY_JOB_SITE, speed, 1, 6, MemoryModuleType.JOB_SITE), 5), Pair.of(new DwarfFarmerVillagerTask(), profession == DwarfProfessionRecord.FARMER ? 2 : 5), Pair.of(new BoneMealTask(), profession == DwarfProfessionRecord.FARMER ? 4 : 7)))), Pair.of(10, new HoldTradeOffersTask(400, 1600)), Pair.of(10, FindInteractionTargetTask.create(EntityType.PLAYER, 4)), Pair.of(2, VillagerWalkTowardsTask.create(MemoryModuleType.JOB_SITE, speed, 9, 100, 1200)), Pair.of(3, new GiveGiftsToHeroTask(100)), Pair.of(99, ScheduleActivityTask.create()));
   }


   private static Pair<Integer, Task<LivingEntity>> createBusyFollowTask() {
      return Pair.of(5, new RandomTask(ImmutableList.of(Pair.of(LookAtMobTask.create(Entityinit.DWARF, 8.0f), 2), Pair.of(LookAtMobTask.create(EntityType.PLAYER, 8.0f), 2), Pair.of(new WaitTask(30, 60), 8))));
   }
}