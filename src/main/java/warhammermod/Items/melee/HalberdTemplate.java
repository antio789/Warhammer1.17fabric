package warhammermod.Items.melee;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import warhammermod.Entities.Projectile.HalberdEntity;
import warhammermod.utils.reference;

public class HalberdTemplate extends SwordItem {
    protected float attackDamage;
    protected float attackSpeed;
    private final Multimap<EntityAttribute, EntityAttributeModifier> modifierMultimap;

    public HalberdTemplate(ToolMaterial tier, Settings properties) {
        super(tier,5,-2.8F, properties.tab(reference.warhammer));
        this.attackDamage = 4.5F + tier.getAttackDamage()*2;
        attackSpeed=-2.8F;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.modifierMultimap = builder.build();
        FabricModelPredicateProviderRegistry.register(this, new Identifier("powerhit"), (stack, clientWorld, entity,i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : entity.getItemUseTime() <20? 0.0F : 1.0F;
            }
        });
    }

    public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
       ItemStack itemstack = player.getStackInHand(hand);
        if (itemstack.getDamage() >= itemstack.getMaxDamage() - 2) {
            return TypedActionResult.fail(itemstack);
        }
        else {
            player.setCurrentHand(hand);
            return TypedActionResult.consume(itemstack);
        }
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
        if(getMaxUseTime(stack)-20>=timeLeft && entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;

            HalberdEntity entity = new HalberdEntity(player,world, getAttackDamage()*1.3F);
            int i = EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack);
            if (i > 0) {
                entity.setpowerDamage(i);
            }
            int k = EnchantmentHelper.getLevel(Enchantments.KNOCKBACK, stack) + 1;
            if (k > 0) {
                entity.setknockbacklevel(k);
            }
            entity.setPosition(player.getX(), player.getEyeY() - 0.26, player.getZ());
            entity.setVelocity(player,player.getPitch(), player.getYaw(), 0.0F, 3F, 0.5F);

            world.spawnEntity(entity);
            world.playSound(null,player.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS,1,1);
            stack.damage(1, player, (p_220009_1_) -> {
                p_220009_1_.sendToolBreakStatus(player.getActiveHand());
            });
            player.getItemCooldownManager().set(this,60);
        }
    }

    public int getMaxUseTime(ItemStack stack) {
        return 1000;
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot p_111205_1_) {
        return p_111205_1_ == EquipmentSlot.MAINHAND ? this.modifierMultimap : super.getAttributeModifiers(p_111205_1_);
    }

    public UseAction getUseAction(ItemStack itemStack) {
        return UseAction.BOW;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }




}
