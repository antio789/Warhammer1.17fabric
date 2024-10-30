package warhammermod.Items.melee;


import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import warhammermod.utils.reference;

public class ArmorTemplate extends ArmorItem {
    public ArmorTemplate(ArmorMaterial materialIn, EquipmentSlot slot, Settings builder){
        super(materialIn, slot, builder.tab(reference.warhammer));
    }
}
