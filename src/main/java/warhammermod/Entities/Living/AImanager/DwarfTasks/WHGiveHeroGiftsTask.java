package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.utils.functions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WHGiveHeroGiftsTask extends Behavior<DwarfEntity> {
   private static ResourceLocation Lordloot = functions.location("gameplay/hero_of_the_village/lord_gift");
   private static final Map<DwarfProfession, ResourceLocation> gifts = Util.make(Maps.newHashMap(), (p_220395_0_) -> {
      p_220395_0_.put(DwarfProfession.Miner, BuiltInLootTables.CLERIC_GIFT);
      p_220395_0_.put(DwarfProfession.FARMER, BuiltInLootTables.FARMER_GIFT);
      p_220395_0_.put(DwarfProfession.Slayer, BuiltInLootTables.BUTCHER_GIFT);
      p_220395_0_.put(DwarfProfession.Engineer, BuiltInLootTables.ARMORER_GIFT);
      p_220395_0_.put(DwarfProfession.Builder, BuiltInLootTables.MASON_GIFT);
      p_220395_0_.put(DwarfProfession.Lord,Lordloot);
      p_220395_0_.put(DwarfProfession.Warrior, BuiltInLootTables.FARMER_GIFT);
   });
   private int timeUntilNextGift = 600;
   private boolean giftGivenDuringThisRun;
   private long timeSinceStart;

   public WHGiveHeroGiftsTask(int p_i50366_1_) {
      super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryStatus.VALUE_PRESENT), p_i50366_1_);
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      if (!this.isHeroVisible(p_212832_2_)) {
         return false;
      } else if (this.timeUntilNextGift > 0) {
         --this.timeUntilNextGift;
         return false;
      } else {
         return true;
      }
   }

   protected void start(ServerLevel p_212831_1_, DwarfEntity p_212831_2_, long p_212831_3_) {
      this.giftGivenDuringThisRun = false;
      this.timeSinceStart = p_212831_3_;
      Player playerentity = this.getNearestTargetableHero(p_212831_2_).get();
      p_212831_2_.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, playerentity);
      BehaviorUtils.lookAtEntity(p_212831_2_, playerentity);
   }

   protected boolean canStillUse(ServerLevel p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return this.isHeroVisible(p_212834_2_) && !this.giftGivenDuringThisRun;
   }

   protected void tick(ServerLevel p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      Player playerentity = this.getNearestTargetableHero(p_212833_2_).get();
      BehaviorUtils.lookAtEntity(p_212833_2_, playerentity);
      if (this.isWithinThrowingDistance(p_212833_2_, playerentity)) {
         if (p_212833_3_ - this.timeSinceStart > 20L) {
            this.throwGift(p_212833_2_, playerentity);
            this.giftGivenDuringThisRun = true;
         }
      } else {
         BehaviorUtils.setWalkAndLookTargetMemories(p_212833_2_, playerentity, 0.5F, 5);
      }

   }

   protected void stop(ServerLevel p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      this.timeUntilNextGift = calculateTimeUntilNextGift(p_212835_1_);
      p_212835_2_.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
      p_212835_2_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
      p_212835_2_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
   }

   private void throwGift(DwarfEntity p_220398_1_, LivingEntity p_220398_2_) {
      for(ItemStack itemstack : this.getItemToThrow(p_220398_1_)) {
         BehaviorUtils.throwItem(p_220398_1_, itemstack, p_220398_2_.position());
      }

   }

   private List<ItemStack> getItemToThrow(DwarfEntity p_220399_1_) {
      if (p_220399_1_.isBaby()) {
         return ImmutableList.of(new ItemStack(Items.POPPY));
      } else {
         DwarfProfession villagerprofession = p_220399_1_.getProfession();
         if (gifts.containsKey(villagerprofession)) {
            LootTable loottable = p_220399_1_.level.getServer().getLootTables().get(gifts.get(villagerprofession));
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel)p_220399_1_.level)).withParameter(LootContextParams.ORIGIN, p_220399_1_.position()).withParameter(LootContextParams.THIS_ENTITY, p_220399_1_).withRandom(p_220399_1_.getRandom());
            return loottable.getRandomItems(lootcontext$builder.create(LootContextParamSets.GIFT));
         } else {
            return ImmutableList.of(new ItemStack(Items.WHEAT_SEEDS));
         }
      }
   }

   private boolean isHeroVisible(DwarfEntity p_220396_1_) {
      return this.getNearestTargetableHero(p_220396_1_).isPresent();
   }

   private Optional<Player> getNearestTargetableHero(DwarfEntity p_220400_1_) {
      return p_220400_1_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).filter(this::isHero);
   }

   private boolean isHero(Player p_220402_1_) {
      return p_220402_1_.hasEffect(MobEffects.HERO_OF_THE_VILLAGE);
   }

   private boolean isWithinThrowingDistance(DwarfEntity p_220401_1_, Player p_220401_2_) {
      BlockPos blockpos = p_220401_2_.blockPosition();
      BlockPos blockpos1 = p_220401_1_.blockPosition();
      return blockpos1.closerThan(blockpos, 5.0D);
   }

   private static int calculateTimeUntilNextGift(ServerLevel p_220397_0_) {
      return 600 + p_220397_0_.random.nextInt(6001);
   }
}