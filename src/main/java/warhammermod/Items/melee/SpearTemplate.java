package warhammermod.Items.melee;


import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import warhammermod.Entities.Projectile.SpearEntity;
import warhammermod.utils.reference;

import java.util.Random;


public class SpearTemplate extends SwordItem {
    public SpearTemplate(Tier tier,Properties properties){
        super(tier, 2,-2F,(properties).tab(reference.warhammer));
    }
    public InteractionResultHolder<ItemStack> use(Level world, Player playerIn, InteractionHand handIn)  {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (stack.getDamageValue() >= stack.getMaxDamage() - 2) {
            return InteractionResultHolder.fail(stack);
        } else {
            playerIn.startUsingItem(handIn);
            return InteractionResultHolder.consume(stack);
        }
    }

        public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if(entityLiving instanceof Player){
            Player player = (Player)entityLiving;

            SpearEntity entity = new SpearEntity(player,worldIn,getDamage(),stack);
            entity.shootFromRotation(player,player.getXRot(), player.getYRot(), 0.0F, 1.3F, 1.0F);

            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, stack);
            if (i > 0) {
                entity.setpowerDamage(i);
            }
            int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, stack);
            if (k > 0) {
                entity.setknockbacklevel(k);
            }
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack) > 0) {
                entity.setSecondsOnFire(100);
            }
            worldIn.addFreshEntity(entity);
            Random random = new Random();
            worldIn.playSound(null,player.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,1,1.35F/(random.nextFloat()*0.4F+1.2F)+0.5F);

            stack.hurtAndBreak(1, player, (p_220009_1_) -> {
                p_220009_1_.broadcastBreakEvent(player.getUsedItemHand());
            });
            player.getInventory().removeItem(stack);
        }
    }

    public int getUseDuration(ItemStack stack) {
        return 1000;
    }



    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }


}
