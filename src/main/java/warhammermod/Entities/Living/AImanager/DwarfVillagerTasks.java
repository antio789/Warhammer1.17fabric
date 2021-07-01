package warhammermod.Entities.Living.AImanager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.AImanager.DwarfTasks.*;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.utils.Registry.Entityinit;


import java.util.Optional;


public class DwarfVillagerTasks {
   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getCorePackage(DwarfProfession profession, float p_220638_1_) {
      return ImmutableList.of(Pair.of(0, new Swim(0.8F)), Pair.of(0, new InteractWithDoor()), Pair.of(0, new LookAtTargetSink(45, 90)), Pair.of(0, new WHPanicTask()), Pair.of(0, new WakeUp()), Pair.of(0, new ReactToBell()), Pair.of(0, new SetRaidStatus()), Pair.of(0, new ValidateNearbyPoi(profession.getPointOfInterest(), MemoryModuleType.JOB_SITE)), Pair.of(0, new ValidateNearbyPoi(profession.getPointOfInterest(), MemoryModuleType.POTENTIAL_JOB_SITE)), Pair.of(1, new MoveToTargetSink()), Pair.of(2, new WHCompetitorScan(profession)), Pair.of(3, new WHTradeTask_LookAndFollowTradingPlayerSink(p_220638_1_)), Pair.of(5, new GoToWantedItem<LivingEntity>(p_220638_1_, false, 4)), Pair.of(6, new AcquirePoi(profession.getPointOfInterest(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())), Pair.of(7, new WHFindPotentialJobTask(p_220638_1_)), Pair.of(8, new WHFindJobTask_yieldjobsite(p_220638_1_)), Pair.of(10, new AcquirePoi(PoiType.HOME, MemoryModuleType.HOME, false, Optional.of((byte)14))), Pair.of(10, new AcquirePoi(PoiType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte)14))), Pair.of(10, new WHAssignProfessionTask()), Pair.of(10, new WHResetProfession()));
   }

   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getWorkPackage(DwarfProfession p_220639_0_, float p_220639_1_) {
      WHWorkAtPoi_spawngolemold spawngolemtask;
      if (p_220639_0_ == DwarfProfession.FARMER) {
         spawngolemtask = new WHWorkATComposter();
      } else {
         spawngolemtask = new WHWorkAtPoi_spawngolemold();
      }

      return ImmutableList.of(getMinimalLookBehavior(), Pair.of(5, new RunOne<>(ImmutableList.of(Pair.of(spawngolemtask, 7), Pair.of(new StrollAroundPoi(MemoryModuleType.JOB_SITE, 0.4F, 4), 2), Pair.of(new StrollToPoi(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5), Pair.of(new WHWalkTowardsRandomSecondaryPosTask(MemoryModuleType.SECONDARY_JOB_SITE, p_220639_1_, 1, 6, MemoryModuleType.JOB_SITE), 5), Pair.of(new WHFarmTask(), p_220639_0_ == DwarfProfession.FARMER ? 2 : 5), Pair.of(new WHBoneMealCropsTask(), p_220639_0_ == DwarfProfession.FARMER ? 4 : 7)))), Pair.of(10, new SetLookAndInteract(EntityType.PLAYER, 4)), Pair.of(2, new WHsetWAlkTargetFromBlockMemory(MemoryModuleType.JOB_SITE, p_220639_1_, 9, 100, 1200)), Pair.of(3, new WHGiveHeroGiftsTask(100)), Pair.of(99, new UpdateActivityFromSchedule()));
   }

   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getPlayPackage(float p_220645_0_) {
      return ImmutableList.of(Pair.of(0, new MoveToTargetSink(80, 120)), getFullLookBehavior(), Pair.of(5, new PlayTagWithOtherKids()), Pair.of(5, new RunOne(ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(InteractWith.of(Entityinit.DWARF, 8, MemoryModuleType.INTERACTION_TARGET, p_220645_0_, 2), 2), Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, p_220645_0_, 2), 1), Pair.of(new VillageBoundRandomStroll(p_220645_0_), 1), Pair.of(new SetWalkTargetFromLookTarget(p_220645_0_, 2), 1), Pair.of(new JumpOnBed(p_220645_0_), 2), Pair.of(new DoNothing(20, 40), 2)))), Pair.of(99, new UpdateActivityFromSchedule()));
   }

   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getRestPackage(DwarfProfession p_220635_0_, float p_220635_1_) {
      return ImmutableList.of(Pair.of(2, new WHsetWAlkTargetFromBlockMemory(MemoryModuleType.HOME, p_220635_1_, 1, 150, 1200)), Pair.of(3, new ValidateNearbyPoi(PoiType.HOME, MemoryModuleType.HOME)), Pair.of(3, new SleepInBed()), Pair.of(5, new RunOne<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(new SetClosestHomeAsWalkTarget(p_220635_1_), 1), Pair.of(new InsideBrownianWalk(p_220635_1_), 4), Pair.of(new WHWAlkToClosestVillage(p_220635_1_, 4), 2), Pair.of(new DoNothing(20, 40), 2)))), getMinimalLookBehavior(), Pair.of(99, new UpdateActivityFromSchedule()));
   }

   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getMeetPackage(DwarfProfession p_220637_0_, float p_220637_1_) {
      return ImmutableList.of(Pair.of(2, new RunOne<>(ImmutableList.of(Pair.of(new StrollAroundPoi(MemoryModuleType.MEETING_POINT,0.4F, 40), 2), Pair.of(new SocializeAtBell(), 2)))), Pair.of(10, new SetLookAndInteract(Entityinit.DWARF, 4)), Pair.of(2, new WHsetWAlkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, p_220637_1_, 6, 100, 200)), Pair.of(3, new WHGiveHeroGiftsTask(100)), Pair.of(3, new ValidateNearbyPoi(PoiType.MEETING, MemoryModuleType.MEETING_POINT)), Pair.of(3, new WHShareItemsTask()), getFullLookBehavior(), Pair.of(99, new UpdateActivityFromSchedule()));
   }

   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getIdlePackage(DwarfEntity entity,DwarfProfession p_220641_0_, float p_220641_1_) {
      return ImmutableList.of(Pair.of(2, new RunOne(ImmutableList.of(Pair.of(InteractWith.of(Entityinit.DWARF, 8, MemoryModuleType.INTERACTION_TARGET, p_220641_1_, 2), 2), Pair.of(new InteractWith(Entityinit.DWARF, 8, DwarfEntity::canDwarfBreed,DwarfEntity::canDwarfBreed, MemoryModuleType.BREED_TARGET, p_220641_1_, 2), 1), Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, p_220641_1_, 2), 1), Pair.of(new VillageBoundRandomStroll(p_220641_1_), 1), Pair.of(new SetWalkTargetFromLookTarget(p_220641_1_, 2), 1), Pair.of(new JumpOnBed(p_220641_1_), 1), Pair.of(new DoNothing(30, 60), 1)))), Pair.of(3, new WHGiveHeroGiftsTask(100)), Pair.of(3, new SetLookAndInteract(EntityType.PLAYER, 4)), Pair.of(3, new WHShareItemsTask()), Pair.of(3,new WHDwarfMakeLove()), getFullLookBehavior(), Pair.of(99, new UpdateActivityFromSchedule()));
   }

   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getPanicPackage(DwarfProfession p_220636_0_, float p_220636_1_) {
      float f = p_220636_1_ * 1.5F;
      return ImmutableList.of(Pair.of(0, new WHClearHurtTask()), Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_HOSTILE, f, 6, false)), Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, f, 6, false)), Pair.of(3, new VillageBoundRandomStroll(f, 2, 2)), getMinimalLookBehavior());
   }

   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getPreRaidPackage(DwarfProfession p_220642_0_, float p_220642_1_) {
      return ImmutableList.of(Pair.of(0, new RingBell()), Pair.of(0, new RunOne<>(ImmutableList.of(Pair.of(new WHsetWAlkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, p_220642_1_ * 1.5F, 2, 150, 200), 6), Pair.of(new VillageBoundRandomStroll(p_220642_1_ * 1.5F), 2)))), getMinimalLookBehavior(), Pair.of(99, new ResetRaidStatus()));
   }

   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getRaidPackage(DwarfProfession p_220640_0_, float p_220640_1_) {
      return ImmutableList.of(Pair.of(0, new RunOne<>(ImmutableList.of(Pair.of(new GoOutsideToCelebrate(p_220640_1_), 5), Pair.of(new VictoryStroll(p_220640_1_ * 1.1F), 2)))), Pair.of(0, new WHCelebrateRaidVictoryTask(600, 600)), Pair.of(2, new LocateHidingPlaceDuringRaid(24, p_220640_1_ * 1.4F)), getMinimalLookBehavior(), Pair.of(99, new ResetRaidStatus()));
   }

   public static ImmutableList<Pair<Integer, ? extends Behavior<? super DwarfEntity>>> getHidePackage(DwarfProfession p_220644_0_, float p_220644_1_) {
      int i = 2;
      return ImmutableList.of(Pair.of(0, new SetHiddenState(15, 3)), Pair.of(1, new LocateHidingPlace(32, p_220644_1_ * 1.25F, 2)), getMinimalLookBehavior());
   }

   private static Pair<Integer, Behavior<LivingEntity>> getFullLookBehavior() {
      return Pair.of(5, new RunOne<>(ImmutableList.of(Pair.of(new SetEntityLookTarget(EntityType.CAT, 8.0F), 8), Pair.of(new SetEntityLookTarget(Entityinit.DWARF, 8.0F), 2), Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 2), Pair.of(new SetEntityLookTarget(MobCategory.CREATURE, 8.0F), 1), Pair.of(new SetEntityLookTarget(MobCategory.WATER_CREATURE, 8.0F), 1), Pair.of(new SetEntityLookTarget(MobCategory.WATER_AMBIENT, 8.0F), 1), Pair.of(new SetEntityLookTarget(MobCategory.MONSTER, 8.0F), 1), Pair.of(new DoNothing(30, 60), 2))));
   }

   public static Pair<Integer, Behavior<LivingEntity>> getMinimalLookBehavior() {
      return Pair.of(5, new RunOne<>(ImmutableList.of(Pair.of(new SetEntityLookTarget(Entityinit.DWARF, 8.0F), 2), Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 2), Pair.of(new DoNothing(30, 60), 8))));
   }
}