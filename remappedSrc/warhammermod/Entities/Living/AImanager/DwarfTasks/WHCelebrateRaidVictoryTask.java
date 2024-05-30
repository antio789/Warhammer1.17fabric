package warhammermod.Entities.Living.AImanager.DwarfTasks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.SeekSkyTask;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import org.jetbrains.annotations.Nullable;
import warhammermod.Entities.Living.DwarfEntity;

import java.util.List;
import java.util.Random;

public class WHCelebrateRaidVictoryTask extends MultiTickTask<DwarfEntity> {
   @Nullable
   private Raid currentRaid;

   public WHCelebrateRaidVictoryTask(int p_i50370_1_, int p_i50370_2_) {
      super(ImmutableMap.of(), p_i50370_1_, p_i50370_2_);
   }

   protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, DwarfEntity p_212832_2_) {
      BlockPos blockpos = p_212832_2_.getBlockPos();
      this.currentRaid = p_212832_1_.getRaidAt(blockpos);
      return this.currentRaid != null && this.currentRaid.hasWon() && SeekSkyTask.isSkyVisible(p_212832_1_, p_212832_2_, blockpos);
   }

   protected boolean canStillUse(ServerWorld p_212834_1_, DwarfEntity p_212834_2_, long p_212834_3_) {
      return this.currentRaid != null && !this.currentRaid.hasStopped();
   }

   protected void stop(ServerWorld p_212835_1_, DwarfEntity p_212835_2_, long p_212835_3_) {
      this.currentRaid = null;
      p_212835_2_.getBrain().refreshActivities(p_212835_1_.getTimeOfDay(), p_212835_1_.getTime());
   }

   protected void tick(ServerWorld p_212833_1_, DwarfEntity p_212833_2_, long p_212833_3_) {
      Random random = p_212833_2_.getRandom();
      if (random.nextInt(100) == 0) {
         p_212833_2_.playCelebrateSound();
      }

      if (random.nextInt(200) == 0 && SeekSkyTask.isSkyVisible(p_212833_1_, p_212833_2_, p_212833_2_.getBlockPos())) {
         DyeColor dyecolor = Util.getRandom(DyeColor.values(), random);
         int i = random.nextInt(3);
         ItemStack itemstack = this.getFirework(dyecolor, i);
         FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(p_212833_2_.world, p_212833_2_, p_212833_2_.getX(), p_212833_2_.getEyeY(), p_212833_2_.getZ(), itemstack);
         p_212833_2_.world.spawnEntity(fireworkrocketentity);
      }

   }

   private ItemStack getFirework(DyeColor p_220391_1_, int p_220391_2_) {
      ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, 1);
      ItemStack itemstack1 = new ItemStack(Items.FIREWORK_STAR);
      NbtCompound compoundnbt = itemstack1.getOrCreateSubNbt("Explosion");
      List<Integer> list = Lists.newArrayList();
      list.add(p_220391_1_.getFireworkColor());
      compoundnbt.putIntArray("Colors", list);
      compoundnbt.putByte("Type", (byte) FireworkRocketItem.Type.BURST.getId());
      NbtCompound compoundnbt1 = itemstack.getOrCreateSubNbt("Fireworks");
      NbtList listnbt = new NbtList();
      NbtCompound compoundnbt2 = itemstack1.getSubNbt("Explosion");
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