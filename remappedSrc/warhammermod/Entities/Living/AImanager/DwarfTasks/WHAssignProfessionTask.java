package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHAssignProfessionTask extends MultiTickTask<DwarfEntity> {
   public WHAssignProfessionTask() {
      super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleState.VALUE_PRESENT));
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity p_212832_2_) {
      BlockPos blockpos = p_212832_2_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().getPos();
      return blockpos.isWithinDistance(p_212832_2_.getPos(), 2.0D) || p_212832_2_.assignProfessionWhenSpawned();
   }

   protected void start(ServerWorld world, DwarfEntity entity, long p_212831_3_) {
      GlobalPos globalpos = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get();
      entity.getBrain().forget(MemoryModuleType.POTENTIAL_JOB_SITE);
      entity.getBrain().remember(MemoryModuleType.JOB_SITE, globalpos);
      world.sendEntityStatus(entity, (byte)14);
      if (entity.getProfession() == DwarfProfession.Warrior) {
         MinecraftServer minecraftserver = world.getServer();
         Optional<PointOfInterestType> POI =  minecraftserver.getWorld(globalpos.getDimension()).getPointOfInterestStorage().getType(globalpos.getPos());
         if(POI.isPresent()){
            for(DwarfProfession prof:DwarfProfession.Profession){
               if(prof.getPointOfInterest()==POI.get()){
                  entity.setVillagerProfession(prof);
                  entity.refreshBrain(world);
               }
            }
         }
      }
   }
}