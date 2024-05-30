package warhammermod.Items.ranged;


import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import warhammermod.Entities.Projectile.StoneEntity;

import java.util.Random;


public class SlingTemplate extends BowItem {
    public float charging;

    private final double baseDamage = 1.0D;

    public SlingTemplate(Settings p_i50052_1_) {
        super(p_i50052_1_);
    }

    public void usageTick(World world, LivingEntity player, ItemStack stack, int count) {
        charging = Math.min(1,(stack.getMaxUseTime()- count) / 20.0F);
    }


    public ItemStack findAmmo(PlayerEntity player) {
        if (this.isAmmo(player.getStackInHand(Hand.OFF_HAND))) {
            return player.getStackInHand(Hand.OFF_HAND);
        } else if (this.isAmmo(player.getStackInHand(Hand.MAIN_HAND))) {
            return player.getStackInHand(Hand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().size(); ++i) {
                ItemStack itemstack = player.getInventory().getStack(i);
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

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeleft) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)entity;
            boolean shoulduseammo = playerentity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = findAmmo(playerentity);
            if (!itemstack.isEmpty() || shoulduseammo) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.COBBLESTONE);
                }


                int j = this.getMaxUseTime(stack) - timeleft;
                float f = getPullProgress(j);
                if (!((double)f < 0.1D)) {

                    if (!world.isClient) {

                        StoneEntity stone = new StoneEntity(playerentity,world,2.5F);
                        stone.setVelocity(playerentity, playerentity.getPitch(), playerentity.getYaw(), 0.0F, f * 2.75F, 1.0F);
                        if (f == 1.0F) {
                            stone.setCritical(true);
                        }

                        int p = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                        if (p > 0) {
                            stone.setDamage(stone.getDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            stone.setPunch(k);
                        }

                        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                            stone.setOnFireFor(100);
                        }

                        stack.damage(1, playerentity, (p_220009_1_) -> {
                            p_220009_1_.sendToolBreakStatus(playerentity.getActiveHand());
                        });

                        world.spawnEntity(stone);
                    }
                    Random random = new Random();
                    world.playSound(null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!shoulduseammo) {
                        itemstack.decrement(1);
                        if (itemstack.isEmpty()) {
                            playerentity.getInventory().removeOne(itemstack);
                        }
                    }

                    playerentity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getStackInHand(hand);
        boolean hasammo = !findAmmo(playerIn).isEmpty();

        if (!playerIn.getAbilities().creativeMode && !hasammo) {
            return TypedActionResult.fail(itemstack);
        } else {
            playerIn.setCurrentHand(hand);
            return TypedActionResult.consume(itemstack);
        }
    }





}
