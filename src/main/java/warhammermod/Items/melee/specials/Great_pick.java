package warhammermod.Items.melee.specials;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import warhammermod.Items.ItemsInit;
import warhammermod.Items.WHCustomenchantements;

import java.util.Set;


public class Great_pick extends SwordItem implements WHCustomenchantements {

    private final Tag<Block> blocks;
    protected final float speed;

    public Great_pick(Properties properties){
        super(Tiers.DIAMOND,2,-2.6F,properties);
        blocks = BlockTags.MINEABLE_WITH_PICKAXE;
        speed=Tiers.DIAMOND.getSpeed();
    }
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        return this.blocks.contains(blockState.getBlock()) ? this.speed : 1.0F;
    }


    public boolean isCorrectToolForDrops(BlockState blockState) {
        int i = this.getTier().getLevel();
        if (i < 3 && blockState.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < 2 && blockState.is(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return (i >= 1 || !blockState.is(BlockTags.NEEDS_STONE_TOOL)) && blockState.is(this.blocks);
        }
    }



    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if (!level.isClientSide && blockState.getDestroySpeed(level, blockPos) != 0.0F) {
            itemStack.hurtAndBreak(1, livingEntity, (livingEntityx) -> {
                livingEntityx.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }


    public Boolean isCorrectEnchantementatTable(Enchantment enchantment)
    {
        return enchantment.category == EnchantmentCategory.DIGGER || enchantment.category.canEnchant(this);
    }
    public Boolean isCorrectEnchantement(EnchantmentCategory enchantment, String ID)
    {
        return enchantment == EnchantmentCategory.DIGGER || enchantment.canEnchant(this);
    }



}
