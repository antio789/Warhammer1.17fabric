package warhammermod.utils.Registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import warhammermod.Entities.Living.DwarfEntity;
import warhammermod.Entities.Living.PegasusEntity;
import warhammermod.Entities.Living.SkavenEntity;
import warhammermod.Entities.Projectile.*;
import warhammermod.utils.reference;


public class Entityinit {
    public static EntityType<HalberdEntity> halberdthrust;
    public static EntityType<Bullet> Bullet;
    public static EntityType<SpearEntity> SpearProjectile;
    public static EntityType<StoneEntity> STONEENTITY;
    public static EntityType<FlameEntity> Flame;
    public static EntityType<GrenadeEntity> Grenade;
    public static EntityType<ShotEntity> Shotentity;
    public static EntityType<WarpBulletEntity> WarpBullet;


    public static EntityType<PegasusEntity> Pegasus;
    public static Item Pegasus_SPAWN_EGG;

    public static EntityType<SkavenEntity> SKAVEN;
    public static Item SKAVEN_SPAWN_EGG;

    public static EntityType<DwarfEntity> DWARF;
    public static Item DWARF_SPAWN_EGG;

    public static void initializeEntities(){
        Pegasus = Registry.register(Registries.ENTITY_TYPE, Identifier.of(reference.modid,"pegasus"), FabricEntityTypeBuilder.<PegasusEntity>create(
                SpawnGroup.CREATURE,PegasusEntity::new).trackRangeBlocks(10).dimensions(EntityDimensions.changing(1.3964844F, 1.6F)).build());
        Registry.register(Registries.ITEM, Identifier.of(reference.modid, "pegasus_egg"), Pegasus_SPAWN_EGG = new SpawnEggItem(Pegasus, 15528173,15395562,new Item.Settings()));

        SKAVEN = Registry.register(Registries.ENTITY_TYPE, Identifier.of(reference.modid,"skaven"), FabricEntityTypeBuilder.<SkavenEntity>create(
                SpawnGroup.MONSTER,SkavenEntity::new).trackRangeBlocks(10).dimensions(EntityDimensions.changing(0.6F, 1.6F)).build());
        Registry.register(Registries.ITEM, Identifier.of(reference.modid, "skaven_egg"), SKAVEN_SPAWN_EGG = new SpawnEggItem(SKAVEN, 13698049,894731, new Item.Settings()));

        DWARF = Registry.register(Registries.ENTITY_TYPE, Identifier.of(reference.modid,"dwarf"), FabricEntityTypeBuilder.<DwarfEntity>create(
                SpawnGroup.MISC,DwarfEntity::new).trackRangeBlocks(10).dimensions(EntityDimensions.changing(0.6F, 1.7F)).build());
        Registry.register(Registries.ITEM, Identifier.of(reference.modid, "dwarf_egg"), DWARF_SPAWN_EGG = new SpawnEggItem(DWARF, 1599971,15721509, new Item.Settings()));


        halberdthrust = Registry.register(Registries.ENTITY_TYPE,Identifier.of(reference.modid,"halberdentity"), FabricEntityTypeBuilder.<HalberdEntity>create(SpawnGroup.MISC,HalberdEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        Bullet = Registry.register(Registries.ENTITY_TYPE,Identifier.of(reference.modid,"bullet"), FabricEntityTypeBuilder.<Bullet>create(SpawnGroup.MISC,Bullet::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        SpearProjectile = Registry.register(Registries.ENTITY_TYPE,Identifier.of(reference.modid,"spearentity"), FabricEntityTypeBuilder.<SpearEntity>create(SpawnGroup.MISC,SpearEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        STONEENTITY= Registry.register(Registries.ENTITY_TYPE,Identifier.of(reference.modid,"stoneentity"), FabricEntityTypeBuilder.<StoneEntity>create(SpawnGroup.MISC,StoneEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        Flame= Registry.register(Registries.ENTITY_TYPE,Identifier.of(reference.modid,"flameentity"), FabricEntityTypeBuilder.<FlameEntity>create(SpawnGroup.MISC,FlameEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        Grenade= Registry.register(Registries.ENTITY_TYPE,Identifier.of(reference.modid,"grenadeentity"), FabricEntityTypeBuilder.<GrenadeEntity>create(SpawnGroup.MISC,GrenadeEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        Shotentity = Registry.register(Registries.ENTITY_TYPE, Identifier.of(reference.modid,"shotentity"), FabricEntityTypeBuilder.<ShotEntity>create(SpawnGroup.MISC,ShotEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        WarpBullet = Registry.register(Registries.ENTITY_TYPE, Identifier.of(reference.modid,"warpbullet"), FabricEntityTypeBuilder.<WarpBulletEntity>create(SpawnGroup.MISC,WarpBulletEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
    }

}
