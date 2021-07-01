package warhammermod.Items.ranged;


import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import warhammermod.Entities.Projectile.StoneEntity;

import java.util.Random;


public class SlingTemplate extends BowItem {
    public float charging;

    private final double baseDamage = 1.0D;

    public SlingTemplate(Properties p_i50052_1_) {
        super(p_i50052_1_);
    }

    public void onUseTick(Level world, LivingEntity player, ItemStack stack, int count) {
        charging = Math.min(1,(stack.getUseDuration()- count) / 20.0F);
    }


    public ItemStack findAmmo(Player player) {
        if (this.isAmmo(player.getItemInHand(InteractionHand.OFF_HAND))) {
            return player.getItemInHand(InteractionHand.OFF_HAND);
        } else if (this.isAmmo(player.getItemInHand(InteractionHand.MAIN_HAND))) {
            return player.getItemInHand(InteractionHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack itemstack = player.getInventory().getItem(i);
                if (this.isAmmo(itemstack)) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
    }
    private boolean isAmmo(ItemStack stack) {
        return stack.getItem().equals(Items.COBBLESTONE);
    }

    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeleft) {
        if (entity instanceof Player) {
            Player playerentity = (Player)entity;
            boolean shoulduseammo = playerentity.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = findAmmo(playerentity);
            if (!itemstack.isEmpty() || shoulduseammo) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.COBBLESTONE);
                }


                int j = this.getUseDuration(stack) - timeleft;
                float f = getPowerForTime(j);
                if (!((double)f < 0.1D)) {

                    if (!world.isClientSide) {

                        StoneEntity stone = new StoneEntity(playerentity,world,2.5F);
                        stone.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F, f * 2.75F, 1.0F);
                        if (f == 1.0F) {
                            stone.setCritArrow(true);
                        }

                        int p = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                        if (p > 0) {
                            stone.setBaseDamage(stone.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                        if (k > 0) {
                            stone.setKnockback(k);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            stone.setSecondsOnFire(100);
                        }

                        stack.hurtAndBreak(1, playerentity, (p_220009_1_) -> {
                            p_220009_1_.broadcastBreakEvent(playerentity.getUsedItemHand());
                        });

                        world.addFreshEntity(stone);
                    }
                    Random random = new Random();
                    world.playSound(null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!shoulduseammo) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.getInventory().removeItem(itemstack);
                        }
                    }

                    playerentity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player playerIn, InteractionHand hand) {
        ItemStack itemstack = playerIn.getItemInHand(hand);
        boolean hasammo = !findAmmo(playerIn).isEmpty();

        if (!playerIn.getAbilities().instabuild && !hasammo) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            playerIn.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }





}
