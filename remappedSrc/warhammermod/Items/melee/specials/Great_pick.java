package warhammermod.Items.melee.specials;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import warhammermod.Items.WHCustomenchantements;


public class Great_pick extends SwordItem implements WHCustomenchantements {

    private final Tag<Block> blocks;
    protected final float speed;

    public Great_pick(Settings properties){
        super(ToolMaterials.DIAMOND,2,-2.6F,properties);
        blocks = BlockTags.PICKAXE_MINEABLE;
        speed=ToolMaterials.DIAMOND.getMiningSpeedMultiplier();
    }
    public float getMiningSpeedMultiplier(ItemStack itemStack, BlockState blockState) {
        return this.blocks.contains(blockState.getBlock()) ? this.speed : 1.0F;
    }


    public boolean isSuitableFor(BlockState blockState) {
        int i = this.getMaterial().getMiningLevel();
        if (i < 3 && blockState.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < 2 && blockState.isIn(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return (i >= 1 || !blockState.isIn(BlockTags.NEEDS_STONE_TOOL)) && blockState.isIn(this.blocks);
        }
    }



    public boolean postMine(ItemStack itemStack, World level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if (!level.isClient && blockState.getHardness(level, blockPos) != 0.0F) {
            itemStack.damage(1, livingEntity, (livingEntityx) -> {
                livingEntityx.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }


    public Boolean isCorrectEnchantementatTable(Enchantment enchantment)
    {
        return enchantment.target == EnchantmentTarget.DIGGER || enchantment.target.isAcceptableItem(this);
    }
    public Boolean isCorrectEnchantement(EnchantmentTarget enchantment, String ID)
    {
        return enchantment == EnchantmentTarget.DIGGER || enchantment.isAcceptableItem(this);
    }



}
