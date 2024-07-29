package warhammermod.Items.melee.specials;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class Great_pick extends SwordItem {//enchantement pick + sword

    public Great_pick(Settings properties){
        super(ToolMaterials.DIAMOND,properties.attributeModifiers(createAttributeModifiers(ToolMaterials.DIAMOND,2,-2.6F)).component(DataComponentTypes.TOOL,ToolMaterials.DIAMOND.createComponent(BlockTags.PICKAXE_MINEABLE)));
    }




}
