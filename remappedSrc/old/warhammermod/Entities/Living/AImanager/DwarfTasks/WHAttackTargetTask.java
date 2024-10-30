package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;


public class WHAttackTargetTask extends MultiTickTask<MobEntity> {
   private final int cooldownBetweenAttacks;

   public WHAttackTargetTask(int cd) {
      super(ImmutableMap.of( MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT));
      this.cooldownBetweenAttacks = cd;
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, MobEntity p_212832_2_) {
      LivingEntity livingentity = this.getAttackTarget(p_212832_2_);
      return !this.isHoldingUsableProjectileWeapon(p_212832_2_) && LookTargetUtil.isVisibleInMemory(p_212832_2_, livingentity) && LookTargetUtil.isWithinMeleeAttackRange(p_212832_2_, livingentity);
   }

   private boolean isHoldingUsableProjectileWeapon(MobEntity mob) {
      return mob.isHolding((itemStack) -> {
         Item item = itemStack.getItem();
         return item instanceof RangedWeaponItem && mob.canUseRangedWeapon((RangedWeaponItem)item);
      });
   }

   protected void start(ServerWorld p_212831_1_, MobEntity p_212831_2_, long p_212831_3_) {
      LivingEntity livingentity = this.getAttackTarget(p_212831_2_);
      LookTargetUtil.lookAt(p_212831_2_, livingentity);
      p_212831_2_.swingHand(Hand.MAIN_HAND);
      p_212831_2_.tryAttack(livingentity);
      p_212831_2_.getBrain().remember(MemoryModuleType.ATTACK_COOLING_DOWN, true, (long)this.cooldownBetweenAttacks);
   }

   private LivingEntity getAttackTarget(MobEntity p_233923_1_) {
      return p_233923_1_.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).get();
   }
}