package warhammermod.Items.melee;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import warhammermod.utils.reference;


public class HammerTemplate extends SwordItem {
    protected float attackDamage;
    protected float attackSpeed;
    private final Multimap<EntityAttribute, EntityAttributeModifier> modifierMultimap;

    public HammerTemplate(ToolMaterial tier, Settings properties) {
        super(tier,0,0, properties.tab(reference.warhammer));
        this.attackDamage = 2.5F + tier.getAttackDamage()*2;
        attackSpeed=-2.9F;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.modifierMultimap = builder.build();
    }

    public HammerTemplate(ToolMaterial tier, Settings properties, float damage, float attackspeed) {
        super(tier,0,0, properties.tab(reference.warhammer));
        this.attackDamage = damage + tier.getAttackDamage()*2;
        attackSpeed=attackspeed;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        this.modifierMultimap = builder.build();
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot p_111205_1_) {
        return p_111205_1_ == EquipmentSlot.MAINHAND ? this.modifierMultimap : super.getAttributeModifiers(p_111205_1_);
    }
}
