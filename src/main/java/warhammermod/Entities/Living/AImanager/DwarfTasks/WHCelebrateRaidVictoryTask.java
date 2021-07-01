package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.MoveToSkySeeingSpot;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.List;
import java.util.Random;

public class WHCelebrateRaidVictoryTask extends Behavior<DwarfEntity> {
   @Nullable
   private Raid currentRaid;

   public WHCelebrateRaidVictoryTask(int p_i50370_1_, int p_i50370_2_) {
      super(ImmutableMap.of(), p_i50370_1_, p_i50370_2_);
   }

   protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, DwarfEntity p_212832_2_) {
      BlockPos blockpos = p_212832_2_.blockPosition();
      this.currentRaid = p_212832_1_.getRaidAt(blockpos);
      return this.currentRaid != null && this.currentRaid.isVictory() && MoveToSkySeeingSpot.hasNoBlocksAbove(p_212832_1_, p_212832_2_, blockpos);
   }

   protected boolean canStillUse(ServerLevel p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return this.currentRaid != null && !this.currentRaid.isStopped();
   }

   protected void stop(ServerLevel p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      this.currentRaid = null;
      p_212835_2_.getBrain().updateActivityFromSchedule(p_212835_1_.getDayTime(), p_212835_1_.getGameTime());
   }

   protected void tick(ServerLevel p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      Random random = p_212833_2_.getRandom();
      if (random.nextInt(100) == 0) {
         p_212833_2_.playCelebrateSound();
      }

      if (random.nextInt(200) == 0 && MoveToSkySeeingSpot.hasNoBlocksAbove(p_212833_1_, p_212833_2_, p_212833_2_.blockPosition())) {
         DyeColor dyecolor = Util.getRandom(DyeColor.values(), random);
         int i = random.nextInt(3);
         ItemStack itemstack = this.getFirework(dyecolor, i);
         FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(p_212833_2_.level, p_212833_2_, p_212833_2_.getX(), p_212833_2_.getEyeY(), p_212833_2_.getZ(), itemstack);
         p_212833_2_.level.addFreshEntity(fireworkrocketentity);
      }

   }

   private ItemStack getFirework(DyeColor p_220391_1_, int p_220391_2_) {
      ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, 1);
      ItemStack itemstack1 = new ItemStack(Items.FIREWORK_STAR);
      CompoundTag compoundnbt = itemstack1.getOrCreateTagElement("Explosion");
      List<Integer> list = Lists.newArrayList();
      list.add(p_220391_1_.getFireworkColor());
      compoundnbt.putIntArray("Colors", list);
      compoundnbt.putByte("Type", (byte) FireworkRocketItem.Shape.BURST.getId());
      CompoundTag compoundnbt1 = itemstack.getOrCreateTagElement("Fireworks");
      ListTag listnbt = new ListTag();
      CompoundTag compoundnbt2 = itemstack1.getTagElement("Explosion");
      if (compoundnbt2 != null) {
         listnbt.add(compoundnbt2);
      }

      compoundnbt1.putByte("Flight", (byte)p_220391_2_);
      if (!listnbt.isEmpty()) {
         compoundnbt1.put("Explosions", listnbt);
      }

      return itemstack;
   }
}