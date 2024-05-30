package warhammermod.Items.melee;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.level.material.Material;
import warhammermod.Items.WHCustomenchantements;
import warhammermod.utils.reference;

public class DaggerTemplate extends ToolItem implements Vanishable, WHCustomenchantements {
    protected float attackDamage;
    protected float attackSpeed;
    private final Multimap<EntityAttribute, EntityAttributeModifier> modifierMultimap;

    public DaggerTemplate(ToolMaterial tier, Settings properties) {
        super(tier,properties.tab(reference.warhammer));
        this.attackDamage = 0.5F + tier.getAttackDamage();
        attackSpeed=5F;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADD_VALUE));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE));
        this.modifierMultimap = builder.build();
    }

    public float getDamage() {
        return this.attackDamage;
    }

    public boolean canMine(BlockState blockState, World level, BlockPos blockPos, PlayerEntity player) {
        return !player.isCreative();
    }

    public float getMiningSpeedMultiplier(ItemStack itemStack, BlockState blockState) {
        if (blockState.isOf(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            Material material = blockState.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !blockState.isIn(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
        }
    }

    public boolean postHit(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2) {
        itemStack.damage(1, livingEntity2, (livingEntityx) -> {
            livingEntityx.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean postMine(ItemStack itemStack, World level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if (blockState.getHardness(level, blockPos) != 0.0F) {
            itemStack.damage(2, livingEntity, (livingEntityx) -> {
                livingEntityx.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    public boolean isSuitableFor(BlockState blockState) {
        return blockState.isOf(Blocks.COBWEB);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.modifierMultimap : super.getAttributeModifiers(equipmentSlot);
    }

    public Boolean isCorrectEnchantementatTable(Enchantment enchantment)
    {
        return enchantment.target == EnchantmentTarget.WEAPON || enchantment.target.isAcceptableItem(this);
    }
    public Boolean isCorrectEnchantement(EnchantmentTarget enchantment, String ID)
    {
        return enchantment == EnchantmentTarget.WEAPON || enchantment.isAcceptableItem(this);
    }
}
