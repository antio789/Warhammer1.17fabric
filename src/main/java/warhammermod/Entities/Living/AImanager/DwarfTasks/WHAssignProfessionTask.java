package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.Optional;

public class WHAssignProfessionTask extends Behavior<DwarfEntity> {
   public WHAssignProfessionTask() {
      super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT));
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      BlockPos blockpos = p_212832_2_.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().pos();
      return blockpos.closerThan(p_212832_2_.position(), 2.0D) || p_212832_2_.assignProfessionWhenSpawned();
   }

   protected void start(ServerLevel world, DwarfEntity entity, long p_212831_3_) {
      GlobalPos globalpos = entity.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get();
      entity.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
      entity.getBrain().setMemory(MemoryModuleType.JOB_SITE, globalpos);
      world.broadcastEntityEvent(entity, (byte)14);
      if (entity.getProfession() == DwarfProfession.Warrior) {
         MinecraftServer minecraftserver = world.getServer();
         Optional<PoiType> POI =  minecraftserver.getLevel(globalpos.dimension()).getPoiManager().getType(globalpos.pos());
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