package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;


import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.phys.Vec3;
import warhammermod.Entities.Living.DwarfEntity;

public class WHWAlkToClosestVillage extends Behavior<DwarfEntity> {
   private final float speedModifier;
   private final int closeEnoughDistance;

   public WHWAlkToClosestVillage(float p_i51557_1_, int p_i51557_2_) {
      super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
      this.speedModifier = p_i51557_1_;
      this.closeEnoughDistance = p_i51557_2_;
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      return !p_212832_1_.isVillage(p_212832_2_.blockPosition());
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      PoiManager pointofinterestmanager = p_212831_1_.getPoiManager();
      int i = pointofinterestmanager.sectionsToVillage(SectionPos.of(p_212831_2_.blockPosition()));
      Vec3 vector3d = null;

      for(int j = 0; j < 5; ++j) {
         Vec3 vector3d1 = LandRandomPos.getPos(p_212831_2_, 15, 7, (p_225444_1_) -> {
            return (double)(-p_212831_1_.sectionsToVillage(SectionPos.of(p_225444_1_)));
         });
         if (vector3d1 != null) {
            int k = pointofinterestmanager.sectionsToVillage(SectionPos.of(new BlockPos(vector3d1)));
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
         p_212831_2_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vector3d, this.speedModifier, this.closeEnoughDistance));
      }

   }
}