package warhammermod.Items.melee;


import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import warhammermod.utils.reference;

public class ArmorTemplate extends ArmorItem {
    public ArmorTemplate(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder){
        super(materialIn, slot, builder.tab(reference.warhammer));
    }
}
