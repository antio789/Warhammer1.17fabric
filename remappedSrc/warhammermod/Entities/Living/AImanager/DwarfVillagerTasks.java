package warhammermod.Entities.Living.AImanager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.poi.PointOfInterestType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.AImanager.DwarfTasks.*;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.utils.Registry.Entityinit;

import java.util.Optional;


public class DwarfVillagerTasks {
   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getCorePackage(DwarfProfession profession, float p_220638_1_) {
      return ImmutableList.of(Pair.of(0, new StayAboveWaterTask(0.8F)), Pair.of(0, new OpenDoorsTask()), Pair.of(0, new LookAroundTask(45, 90)), Pair.of(0, new WHPanicTask()), Pair.of(0, new WakeUpTask()), Pair.of(0, new HideWhenBellRingsTask()), Pair.of(0, new StartRaidTask()), Pair.of(0, new ForgetCompletedPointOfInterestTask(profession.getPointOfInterest(), MemoryModuleType.JOB_SITE)), Pair.of(0, new ForgetCompletedPointOfInterestTask(profession.getPointOfInterest(), MemoryModuleType.POTENTIAL_JOB_SITE)), Pair.of(1, new WanderAroundTask()), Pair.of(2, new WHCompetitorScan(profession)), Pair.of(3, new WHTradeTask_LookAndFollowTradingPlayerSink(p_220638_1_)), Pair.of(5, new WalkToNearestVisibleWantedItemTask<LivingEntity>(p_220638_1_, false, 4)), Pair.of(6, new FindPointOfInterestTask(profession.getPointOfInterest(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())), Pair.of(7, new WHFindPotentialJobTask(p_220638_1_)), Pair.of(8, new WHFindJobTask_yieldjobsite(p_220638_1_)), Pair.of(10, new FindPointOfInterestTask(PointOfInterestType.HOME, MemoryModuleType.HOME, false, Optional.of((byte)14))), Pair.of(10, new FindPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte)14))), Pair.of(10, new WHAssignProfessionTask()), Pair.of(10, new WHResetProfession()));
   }

   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getWorkPackage(DwarfProfession p_220639_0_, float p_220639_1_) {
      WHWorkAtPoi_spawngolemold spawngolemtask;
      if (p_220639_0_ == DwarfProfession.FARMER) {
         spawngolemtask = new WHWorkATComposter();
      } else {
         spawngolemtask = new WHWorkAtPoi_spawngolemold();
      }

      return ImmutableList.of(getMinimalLookBehavior(), Pair.of(5, new RandomTask<>(ImmutableList.of(Pair.of(spawngolemtask, 7), Pair.of(new GoToIfNearbyTask(MemoryModuleType.JOB_SITE, 0.4F, 4), 2), Pair.of(new GoToNearbyPositionTask(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5), Pair.of(new WHWalkTowardsRandomSecondaryPosTask(MemoryModuleType.SECONDARY_JOB_SITE, p_220639_1_, 1, 6, MemoryModuleType.JOB_SITE), 5), Pair.of(new WHFarmTask(), p_220639_0_ == DwarfProfession.FARMER ? 2 : 5), Pair.of(new WHBoneMealCropsTask(), p_220639_0_ == DwarfProfession.FARMER ? 4 : 7)))), Pair.of(10, new FindInteractionTargetTask(EntityType.PLAYER, 4)), Pair.of(2, new WHsetWAlkTargetFromBlockMemory(MemoryModuleType.JOB_SITE, p_220639_1_, 9, 100, 1200)), Pair.of(3, new WHGiveHeroGiftsTask(100)), Pair.of(99, new ScheduleActivityTask()));
   }

   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getPlayPackage(float p_220645_0_) {
      return ImmutableList.of(Pair.of(0, new WanderAroundTask(80, 120)), getFullLookBehavior(), Pair.of(5, new PlayWithVillagerBabiesTask()), Pair.of(5, new RandomTask(ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleState.VALUE_ABSENT), ImmutableList.of(Pair.of(FindEntityTask.create(Entityinit.DWARF, 8, MemoryModuleType.INTERACTION_TARGET, p_220645_0_, 2), 2), Pair.of(FindEntityTask.create(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, p_220645_0_, 2), 1), Pair.of(new FindWalkTargetTask(p_220645_0_), 1), Pair.of(new GoTowardsLookTargetTask(p_220645_0_, 2), 1), Pair.of(new JumpInBedTask(p_220645_0_), 2), Pair.of(new WaitTask(20, 40), 2)))), Pair.of(99, new ScheduleActivityTask()));
   }

   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getRestPackage(DwarfProfession p_220635_0_, float p_220635_1_) {
      return ImmutableList.of(Pair.of(2, new WHsetWAlkTargetFromBlockMemory(MemoryModuleType.HOME, p_220635_1_, 1, 150, 1200)), Pair.of(3, new ForgetCompletedPointOfInterestTask(PointOfInterestType.HOME, MemoryModuleType.HOME)), Pair.of(3, new SleepTask()), Pair.of(5, new RandomTask<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryModuleState.VALUE_ABSENT), ImmutableList.of(Pair.of(new WalkHomeTask(p_220635_1_), 1), Pair.of(new WanderIndoorsTask(p_220635_1_), 4), Pair.of(new WHWAlkToClosestVillage(p_220635_1_, 4), 2), Pair.of(new WaitTask(20, 40), 2)))), getMinimalLookBehavior(), Pair.of(99, new ScheduleActivityTask()));
   }

   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getMeetPackage(DwarfProfession p_220637_0_, float p_220637_1_) {
      return ImmutableList.of(Pair.of(2, new RandomTask<>(ImmutableList.of(Pair.of(new GoToIfNearbyTask(MemoryModuleType.MEETING_POINT,0.4F, 40), 2), Pair.of(new MeetVillagerTask(), 2)))), Pair.of(10, new FindInteractionTargetTask(Entityinit.DWARF, 4)), Pair.of(2, new WHsetWAlkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, p_220637_1_, 6, 100, 200)), Pair.of(3, new WHGiveHeroGiftsTask(100)), Pair.of(3, new ForgetCompletedPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT)), Pair.of(3, new WHShareItemsTask()), getFullLookBehavior(), Pair.of(99, new ScheduleActivityTask()));
   }

   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getIdlePackage(DwarfEntity entity,DwarfProfession p_220641_0_, float p_220641_1_) {
      return ImmutableList.of(Pair.of(2, new RandomTask(ImmutableList.of(Pair.of(FindEntityTask.create(Entityinit.DWARF, 8, MemoryModuleType.INTERACTION_TARGET, p_220641_1_, 2), 2), Pair.of(new FindEntityTask(Entityinit.DWARF, 8, DwarfEntity::canDwarfBreed,DwarfEntity::canDwarfBreed, MemoryModuleType.BREED_TARGET, p_220641_1_, 2), 1), Pair.of(FindEntityTask.create(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, p_220641_1_, 2), 1), Pair.of(new FindWalkTargetTask(p_220641_1_), 1), Pair.of(new GoTowardsLookTargetTask(p_220641_1_, 2), 1), Pair.of(new JumpInBedTask(p_220641_1_), 1), Pair.of(new WaitTask(30, 60), 1)))), Pair.of(3, new WHGiveHeroGiftsTask(100)), Pair.of(3, new FindInteractionTargetTask(EntityType.PLAYER, 4)), Pair.of(3, new WHShareItemsTask()), Pair.of(3,new WHDwarfMakeLove()), getFullLookBehavior(), Pair.of(99, new ScheduleActivityTask()));
   }

   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getPanicPackage(DwarfProfession p_220636_0_, float p_220636_1_) {
      float f = p_220636_1_ * 1.5F;
      return ImmutableList.of(Pair.of(0, new WHClearHurtTask()), Pair.of(1, GoToRememberedPositionTask.createEntityBased(MemoryModuleType.NEAREST_HOSTILE, f, 6, false)), Pair.of(1, GoToRememberedPositionTask.createEntityBased(MemoryModuleType.HURT_BY_ENTITY, f, 6, false)), Pair.of(3, new FindWalkTargetTask(f, 2, 2)), getMinimalLookBehavior());
   }

   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getPreRaidPackage(DwarfProfession p_220642_0_, float p_220642_1_) {
      return ImmutableList.of(Pair.of(0, new RingBellTask()), Pair.of(0, new RandomTask<>(ImmutableList.of(Pair.of(new WHsetWAlkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, p_220642_1_ * 1.5F, 2, 150, 200), 6), Pair.of(new FindWalkTargetTask(p_220642_1_ * 1.5F), 2)))), getMinimalLookBehavior(), Pair.of(99, new EndRaidTask()));
   }

   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getRaidPackage(DwarfProfession p_220640_0_, float p_220640_1_) {
      return ImmutableList.of(Pair.of(0, new RandomTask<>(ImmutableList.of(Pair.of(new GoOutsideToCelebrate(p_220640_1_), 5), Pair.of(new VictoryStroll(p_220640_1_ * 1.1F), 2)))), Pair.of(0, new WHCelebrateRaidVictoryTask(600, 600)), Pair.of(2, new LocateHidingPlaceDuringRaid(24, p_220640_1_ * 1.4F)), getMinimalLookBehavior(), Pair.of(99, new EndRaidTask()));
   }

   public static ImmutableList<Pair<Integer, ? extends MultiTickTask<? super DwarfEntity>>> getHidePackage(DwarfProfession p_220644_0_, float p_220644_1_) {
      int i = 2;
      return ImmutableList.of(Pair.of(0, new ForgetBellRingTask(15, 3)), Pair.of(1, new HideInHomeTask(32, p_220644_1_ * 1.25F, 2)), getMinimalLookBehavior());
   }

   private static Pair<Integer, MultiTickTask<LivingEntity>> getFullLookBehavior() {
      return Pair.of(5, new RandomTask<>(ImmutableList.of(Pair.of(new LookAtMobTask(EntityType.CAT, 8.0F), 8), Pair.of(new LookAtMobTask(Entityinit.DWARF, 8.0F), 2), Pair.of(new LookAtMobTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new LookAtMobTask(SpawnGroup.CREATURE, 8.0F), 1), Pair.of(new LookAtMobTask(SpawnGroup.WATER_CREATURE, 8.0F), 1), Pair.of(new LookAtMobTask(SpawnGroup.WATER_AMBIENT, 8.0F), 1), Pair.of(new LookAtMobTask(SpawnGroup.MONSTER, 8.0F), 1), Pair.of(new WaitTask(30, 60), 2))));
   }

   public static Pair<Integer, MultiTickTask<LivingEntity>> getMinimalLookBehavior() {
      return Pair.of(5, new RandomTask<>(ImmutableList.of(Pair.of(new LookAtMobTask(Entityinit.DWARF, 8.0F), 2), Pair.of(new LookAtMobTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new WaitTask(30, 60), 8))));
   }
}