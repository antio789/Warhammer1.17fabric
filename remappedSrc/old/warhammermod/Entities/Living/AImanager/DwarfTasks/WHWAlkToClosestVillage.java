package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.poi.PointOfInterestStorage;
import warhammermod.Entities.Living.DwarfEntity;

public class WHWAlkToClosestVillage extends MultiTickTask<DwarfEntity> {
   private final float speedModifier;
   private final int closeEnoughDistance;

   public WHWAlkToClosestVillage(float p_i51557_1_, int p_i51557_2_) {
      super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT));
      this.speedModifier = p_i51557_1_;
      this.closeEnoughDistance = p_i51557_2_;
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity p_212832_2_) {
      return !p_212832_1_.isNearOccupiedPointOfInterest(p_212832_2_.getBlockPos());
   }

   protected void start(ServerWorld p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      PointOfInterestStorage pointofinterestmanager = p_212831_1_.getPointOfInterestStorage();
      int i = pointofinterestmanager.getDistanceFromNearestOccupied(ChunkSectionPos.from(p_212831_2_.getBlockPos()));
      Vec3d vector3d = null;

      for(int j = 0; j < 5; ++j) {
         Vec3d vector3d1 = FuzzyTargeting.find(p_212831_2_, 15, 7, (p_225444_1_) -> {
            return (double)(-p_212831_1_.getOccupiedPointOfInterestDistance(ChunkSectionPos.from(p_225444_1_)));
         });
         if (vector3d1 != null) {
            int k = pointofinterestmanager.getDistanceFromNearestOccupied(ChunkSectionPos.from(new BlockPos(vector3d1)));
            if (k < i) {
               vector3d = vector3d1;
               break;
            }

            if (k == i) {
               vector3d = vector3d1;
            }
         }
      }

      if (vector3d != null) {
         p_212831_2_.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(vector3d, this.speedModifier, this.closeEnoughDistance));
      }

   }
}