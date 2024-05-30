package warhammermod.Items.melee;


import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import warhammermod.Entities.Projectile.SpearEntity;
import warhammermod.utils.reference;

import java.util.Random;


public class SpearTemplate extends SwordItem {
    public SpearTemplate(ToolMaterial tier,Settings properties){
        super(tier, 2,-2F,(properties).tab(reference.warhammer));
    }
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand handIn)  {
        ItemStack stack = playerIn.getStackInHand(handIn);
        if (stack.getDamage() >= stack.getMaxDamage() - 2) {
            return TypedActionResult.fail(stack);
        } else {
            playerIn.setCurrentHand(handIn);
            return TypedActionResult.consume(stack);
        }
    }

        public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if(entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity)entityLiving;

            SpearEntity entity = new SpearEntity(player,worldIn,getAttackDamage(),stack);
            entity.setVelocity(player,player.getPitch(), player.getYaw(), 0.0F, 1.3F, 1.0F);

            int i = EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack);
            if (i > 0) {
                entity.setpowerDamage(i);
            }
            int k = EnchantmentHelper.getLevel(Enchantments.KNOCKBACK, stack);
            if (k > 0) {
                entity.setknockbacklevel(k);
            }
            if (EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack) > 0) {
                entity.setOnFireFor(100);
            }
            worldIn.spawnEntity(entity);
            Random random = new Random();
            worldIn.playSound(null,player.getBlockPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,1,1.35F/(random.nextFloat()*0.4F+1.2F)+0.5F);

            stack.damage(1, player, (p_220009_1_) -> {
                p_220009_1_.sendToolBreakStatus(player.getActiveHand());
            });
            player.getInventory().removeOne(stack);
        }
    }

    public int getMaxUseTime(ItemStack stack) {
        return 1000;
    }



    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }


}
