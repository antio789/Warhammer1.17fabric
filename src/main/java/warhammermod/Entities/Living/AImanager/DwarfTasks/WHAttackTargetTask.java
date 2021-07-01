package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;


public class WHAttackTargetTask extends Behavior<Mob> {
   private final int cooldownBetweenAttacks;

   public WHAttackTargetTask(int cd) {
      super(ImmutableMap.of( MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
      this.cooldownBetweenAttacks = cd;
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, Mob p_212832_2_) {
      LivingEntity livingentity = this.getAttackTarget(p_212832_2_);
      return !this.isHoldingUsableProjectileWeapon(p_212832_2_) && BehaviorUtils.canSee(p_212832_2_, livingentity) && BehaviorUtils.isWithinMeleeAttackRange(p_212832_2_, livingentity);
   }

   private boolean isHoldingUsableProjectileWeapon(Mob mob) {
      return mob.isHolding((itemStack) -> {
         Item item = itemStack.getItem();
         return item instanceof ProjectileWeaponItem && mob.canFireProjectileWeapon((ProjectileWeaponItem)item);
      });
   }

   protected void start(ServerLevel p_212831_1_, Mob p_212831_2_, long p_212831_3_) {
      LivingEntity livingentity = this.getAttackTarget(p_212831_2_);
      BehaviorUtils.lookAtEntity(p_212831_2_, livingentity);
      p_212831_2_.swing(InteractionHand.MAIN_HAND);
      p_212831_2_.doHurtTarget(livingentity);
      p_212831_2_.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, (long)this.cooldownBetweenAttacks);
   }

   private LivingEntity getAttackTarget(Mob p_233923_1_) {
      return p_233923_1_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
   }
}