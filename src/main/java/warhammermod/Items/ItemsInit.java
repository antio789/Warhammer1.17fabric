package warhammermod.Items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.item.*;
import warhammermod.Items.melee.*;
import warhammermod.Items.melee.specials.Ghal_Maraz;
import warhammermod.Items.melee.specials.Great_pick;
import warhammermod.Items.ranged.*;
import warhammermod.utils.reference;

public class ItemsInit {
    public static FoodComponent BEER_EFFECT= (new FoodComponent.Builder()).hunger(4).saturationModifier(0.7F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 120, 0), 0.8F).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 220, 1), 0.18F).alwaysEdible().build();

    public static Item BEER = new Item((new Item.Settings()).tab(ItemGroup.TAB_FOOD).rarity(Rarity.RARE).food(BEER_EFFECT));

    public static Item Cartridge = new Cartridge();
    public static Item Warpstone = new Cartridge();
    public static Item shotShell = new Cartridge();
    public static Item Grenade= new Cartridge();

    public static final Item netherite_warhammer = new HammerTemplate(ToolMaterials.NETHERITE,new Item.Settings());
    public static final Item diamond_warhammer = new HammerTemplate(ToolMaterials.DIAMOND,new Item.Settings());
    public static final Item wooden_warhammer = new HammerTemplate(ToolMaterials.WOOD,new Item.Settings());
    public static final Item stone_warhammer = new HammerTemplate(ToolMaterials.STONE,new Item.Settings());
    public static final Item iron_warhammer = new HammerTemplate(ToolMaterials.IRON,new Item.Settings());
    public static final Item gold_warhammer = new HammerTemplate(ToolMaterials.GOLD,new Item.Settings());

    public static final Item netherite_dagger = new DaggerTemplate(ToolMaterials.NETHERITE,new Item.Settings());
    public static final Item diamond_dagger = new DaggerTemplate(ToolMaterials.DIAMOND,new Item.Settings());
    public static final Item wooden_dagger = new DaggerTemplate(ToolMaterials.WOOD,new Item.Settings());
    public static final Item stone_dagger = new DaggerTemplate(ToolMaterials.STONE,new Item.Settings());
    public static final Item iron_dagger = new DaggerTemplate(ToolMaterials.IRON,new Item.Settings());
    public static final Item gold_dagger = new DaggerTemplate(ToolMaterials.GOLD,new Item.Settings());

    public static final Item netherite_halberd = new HalberdTemplate(ToolMaterials.NETHERITE,new Item.Settings());
    public static final Item diamond_halberd = new HalberdTemplate(ToolMaterials.DIAMOND,new Item.Settings());
    public static final Item wooden_halberd = new HalberdTemplate(ToolMaterials.WOOD,new Item.Settings());
    public static final Item stone_halberd = new HalberdTemplate(ToolMaterials.STONE,new Item.Settings());
    public static final Item iron_halberd = new HalberdTemplate(ToolMaterials.IRON,new Item.Settings());
    public static final Item gold_halberd = new HalberdTemplate(ToolMaterials.GOLD,new Item.Settings());

    public static final Item netherite_spear = new SpearTemplate(ToolMaterials.NETHERITE,new Item.Settings());
    public static final Item diamond_spear = new SpearTemplate(ToolMaterials.DIAMOND,new Item.Settings());
    public static final Item wooden_spear = new SpearTemplate(ToolMaterials.WOOD,new Item.Settings());
    public static final Item stone_spear = new SpearTemplate(ToolMaterials.STONE,new Item.Settings());
    public static final Item iron_spear = new SpearTemplate(ToolMaterials.IRON,new Item.Settings());
    public static final Item gold_spear = new SpearTemplate(ToolMaterials.GOLD,new Item.Settings());

    public static final SwordItem GreatPick = new Great_pick(new Item.Settings().tab(reference.warhammer));
    public static final Ghal_Maraz GHAL_MARAZ = new Ghal_Maraz(new Item.Settings().rarity(Rarity.EPIC).maxDamage((int)(ToolMaterials.NETHERITE.getDurability()*1.3F)).tab(reference.warhammer));


    public static final Item DIAMOND_CHAINMAIL_HELMET =  new ArmorTemplate(ModArmorMaterial.DIAMOND_CHAINMAIL, EquipmentSlot.HEAD, (new Item.Settings()));
    public static final Item DIAMOND_CHAINMAIL_CHESTPLATE =  new ArmorTemplate(ModArmorMaterial.DIAMOND_CHAINMAIL, EquipmentSlot.CHEST, (new Item.Settings()));
    public static final Item DIAMOND_CHAINMAIL_LEGGINGS = new ArmorTemplate(ModArmorMaterial.DIAMOND_CHAINMAIL, EquipmentSlot.LEGS, (new Item.Settings()));
    public static final Item DIAMOND_CHAINMAIL_BOOTS =  new ArmorTemplate(ModArmorMaterial.DIAMOND_CHAINMAIL, EquipmentSlot.FEET, (new Item.Settings()));

    public static Item musket = new GunTemplate(new Item.Settings().maxDamage(420), Cartridge,40,1,13);
    public static Item pistol = new GunTemplate(new Item.Settings().maxDamage(384), Cartridge,25,1,8);
    public static Item repeater_handgun = new Gun3DTemplate(new Item.Settings().maxDamage(500), Cartridge,92,6,13);
    public static Item thunderer_handgun = new GunTemplate(new Item.Settings().maxDamage(540), Cartridge,35,1,16);

    public static Item blunderbuss = new ShotgunTemplate(new Item.Settings().maxDamage(390), shotShell,40,1,19);
    public static Item GrudgeRaker = new ShotgunTemplate(new Item.Settings().maxDamage(450), shotShell,40,2,19);

    public static Item grenade_launcher = new GrenadeTemplate(new Item.Settings().maxDamage(384), Grenade,75,1);

    public static Item DrakeGun = new DrakeGunTemplate(new Item.Settings().maxDamage(210).tab(reference.warhammer),64,40);
    public static Item RatlingGun = new RatlingGun(new Item.Settings().maxDamage(210).tab(reference.warhammer),64,80);

    public static Item Warplock_jezzail = new WarpgunTemplate(new Item.Settings().maxDamage(384), Warpstone,50,1,14);

    public static Item Sling = new SlingTemplate(new Item.Settings().maxDamage(180).tab(reference.warhammer));

    public static ShieldTemplate High_Elf_Shield= new ShieldTemplate(new Item.Settings());
    public static ShieldTemplate Dark_Elf_Shield = new ShieldTemplate(new Item.Settings());

    public static SmallShieldTemplate Dwarf_shield= new SmallShieldTemplate(new Item.Settings());
    public static SmallShieldTemplate Imperial_shield= new SmallShieldTemplate(new Item.Settings());
    public static SmallShieldTemplate Skaven_shield= new SmallShieldTemplate(new Item.Settings());



    //public static final Item Great_pick = new Great_pick(new Item.Properties().setNoRepair().tab(reference.warhammer));
    //public static final Item Ghal_Maraz = new Ghal_Maraz(new Item.Properties().setNoRepair().durability((int)(Tiers.NETHERITE.getUses()*1.3)));

    public static Item iron_gunsword = new GunSwordTemplate(ToolMaterials.IRON,25,1,8);
    public static Item diamond_gunsword = new GunSwordTemplate(ToolMaterials.DIAMOND,25,1,8);

    public static void registeritems(){
        BEER_EFFECT = (new FoodComponent.Builder()).hunger(4).saturationModifier(0.7F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 120, 0), 0.8F).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 220, 1), 0.18F).alwaysEdible().build();

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "beer"), BEER);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "cartridge"), Cartridge);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "warpstone"), Warpstone);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "shot_shell"), shotShell);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "grenade"), Grenade);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "flintlock_pistol"), pistol);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "musket"), musket);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "nuln_repeater_handgun"), repeater_handgun);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "thunderer_handgun"), thunderer_handgun);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "blunderbuss"), blunderbuss);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "grudgeraker"), GrudgeRaker);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "grenade_launcher"), grenade_launcher);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "drakegun"), DrakeGun);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "ratling_gun"), RatlingGun);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "warplock_jezzail"), Warplock_jezzail);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "sling"), Sling);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "diamond_gunsword"), diamond_gunsword);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "iron_gunsword"), iron_gunsword);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "high_elf_shield"), High_Elf_Shield);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "dark_elf_shield"), Dark_Elf_Shield);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "dwarf_shield"), Dwarf_shield);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "imperial_shield"), Imperial_shield);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "skaven_shield"), Skaven_shield);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "netherite_warhammer"), netherite_warhammer);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "diamond_warhammer"), diamond_warhammer);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "wooden_warhammer"), wooden_warhammer);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "stone_warhammer"), stone_warhammer);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "iron_warhammer"), iron_warhammer);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "gold_warhammer"), gold_warhammer);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "netherite_dagger"), netherite_dagger);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "diamond_dagger"), diamond_dagger);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "wooden_dagger"), wooden_dagger);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "stone_dagger"), stone_dagger);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "iron_dagger"), iron_dagger);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "gold_dagger"), gold_dagger);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "netherite_halberd"), netherite_halberd);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "diamond_halberd"), diamond_halberd);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "wooden_halberd"), wooden_halberd);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "stone_halberd"), stone_halberd);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "iron_halberd"), iron_halberd);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "gold_halberd"), gold_halberd);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "netherite_spear"), netherite_spear);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "diamond_spear"), diamond_spear);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "wooden_spear"), wooden_spear);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "stone_spear"), stone_spear);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "iron_spear"), iron_spear);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "gold_spear"), gold_spear);

        Registry.register(Registry.ITEM, new Identifier(reference.modid, "ghal_maraz"), GHAL_MARAZ);
        Registry.register(Registry.ITEM, new Identifier(reference.modid, "war_pick"), GreatPick);
    }

}
