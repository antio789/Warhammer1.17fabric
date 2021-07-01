package warhammermod.Items.melee;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import warhammermod.Entities.Projectile.HalberdEntity;
import warhammermod.utils.reference;

public class HalberdTemplate extends SwordItem {
    protected float attackDamage;
    protected float attackSpeed;
    private final Multimap<Attribute, AttributeModifier> modifierMultimap;

    public HalberdTemplate(Tier tier, Properties properties) {
        super(tier,5,-2.8F, properties.tab(reference.warhammer));
        this.attackDamage = 4.5F + tier.getAttackDamageBonus()*2;
        attackSpeed=-2.8F;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.modifierMultimap = builder.build();
        FabricModelPredicateProviderRegistry.register(this, new ResourceLocation("powerhit"), (stack, clientWorld, entity,i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : entity.getTicksUsingItem() <20? 0.0F : 1.0F;
            }
        });
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
       ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 2) {
            return InteractionResultHolder.fail(itemstack);
        }
        else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
        if(getUseDuration(stack)-20>=timeLeft && entityLiving instanceof Player){
            Player player = (Player) entityLiving;

            HalberdEntity entity = new HalberdEntity(player,world, getDamage()*1.3F);
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, stack);
            if (i > 0) {
                entity.setpowerDamage(i);
            }
            int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, stack) + 1;
            if (k > 0) {
                entity.setknockbacklevel(k);
            }
            entity.setPos(player.getX(), player.getEyeY() - 0.26, player.getZ());
            entity.shootFromRotation(player,player.getXRot(), player.getYRot(), 0.0F, 3F, 0.5F);

            world.addFreshEntity(entity);
            world.playSound(null,player.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS,1,1);
            stack.hurtAndBreak(1, player, (p_220009_1_) -> {
                p_220009_1_.broadcastBreakEvent(player.getUsedItemHand());
            });
            player.getCooldowns().addCooldown(this,60);
        }
    }

    public int getUseDuration(ItemStack stack) {
        return 1000;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_111205_1_) {
        return p_111205_1_ == EquipmentSlot.MAINHAND ? this.modifierMultimap : super.getDefaultAttributeModifiers(p_111205_1_);
    }

    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    public float getDamage() {
        return this.attackDamage;
    }




}
