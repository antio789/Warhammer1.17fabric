package warhammermod.Items.melee;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import warhammermod.utils.reference;


public class HammerTemplate extends SwordItem {
    protected float attackDamage;
    protected float attackSpeed;
    private final Multimap<Attribute, AttributeModifier> modifierMultimap;

    public HammerTemplate(Tier tier, Properties properties) {
        super(tier,0,0, properties.tab(reference.warhammer));
        this.attackDamage = 2.5F + tier.getAttackDamageBonus()*2;
        attackSpeed=-2.9F;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.modifierMultimap = builder.build();
    }

    public HammerTemplate(Tier tier, Properties properties, float damage, float attackspeed) {
        super(tier,0,0, properties.tab(reference.warhammer));
        this.attackDamage = damage + tier.getAttackDamageBonus()*2;
        attackSpeed=attackspeed;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.modifierMultimap = builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_111205_1_) {
        return p_111205_1_ == EquipmentSlot.MAINHAND ? this.modifierMultimap : super.getDefaultAttributeModifiers(p_111205_1_);
    }
}
