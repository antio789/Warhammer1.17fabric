package warhammermod.utils.Registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
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
        Pegasus = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(reference.modid,"pegasus"), FabricEntityTypeBuilder.<PegasusEntity>create(
                MobCategory.CREATURE,PegasusEntity::new).trackRangeBlocks(10).dimensions(EntityDimensions.scalable(1.3964844F, 1.6F)).build());
        Registry.register(Registry.ITEM, new ResourceLocation(reference.modid, "pegasus_egg"), Pegasus_SPAWN_EGG = new SpawnEggItem(Pegasus, 15528173,15395562, (new Item.Properties()).tab(reference.warhammer)));

        SKAVEN = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(reference.modid,"skaven"), FabricEntityTypeBuilder.<SkavenEntity>create(
                MobCategory.MONSTER,SkavenEntity::new).trackRangeBlocks(10).dimensions(EntityDimensions.scalable(0.6F, 1.6F)).build());
        Registry.register(Registry.ITEM, new ResourceLocation(reference.modid, "skaven_egg"), SKAVEN_SPAWN_EGG = new SpawnEggItem(SKAVEN, 13698049,894731, (new Item.Properties()).tab(reference.warhammer)));
        DWARF = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(reference.modid,"dwarf"), FabricEntityTypeBuilder.<DwarfEntity>create(
                MobCategory.MISC,DwarfEntity::new).trackRangeBlocks(10).dimensions(EntityDimensions.scalable(0.6F, 1.7F)).build());
        Registry.register(Registry.ITEM, new ResourceLocation(reference.modid, "dwarf_egg"), DWARF_SPAWN_EGG = new SpawnEggItem(DWARF, 1599971,15721509, (new Item.Properties()).tab(reference.warhammer)));


        halberdthrust = Registry.register(Registry.ENTITY_TYPE,new ResourceLocation(reference.modid,"halberdentity"), FabricEntityTypeBuilder.<HalberdEntity>create(MobCategory.MISC,HalberdEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        Bullet = Registry.register(Registry.ENTITY_TYPE,new ResourceLocation(reference.modid,"bullet"), FabricEntityTypeBuilder.<Bullet>create(MobCategory.MISC,Bullet::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        SpearProjectile = Registry.register(Registry.ENTITY_TYPE,new ResourceLocation(reference.modid,"spearentity"), FabricEntityTypeBuilder.<SpearEntity>create(MobCategory.MISC,SpearEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        STONEENTITY= Registry.register(Registry.ENTITY_TYPE,new ResourceLocation(reference.modid,"stoneentity"), FabricEntityTypeBuilder.<StoneEntity>create(MobCategory.MISC,StoneEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        Flame= Registry.register(Registry.ENTITY_TYPE,new ResourceLocation(reference.modid,"flameentity"), FabricEntityTypeBuilder.<FlameEntity>create(MobCategory.MISC,FlameEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        Grenade= Registry.register(Registry.ENTITY_TYPE,new ResourceLocation(reference.modid,"grenadeentity"), FabricEntityTypeBuilder.<GrenadeEntity>create(MobCategory.MISC,GrenadeEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        Shotentity = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(reference.modid,"shotentity"), FabricEntityTypeBuilder.<ShotEntity>create(MobCategory.MISC,ShotEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
        WarpBullet = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(reference.modid,"warpbullet"), FabricEntityTypeBuilder.<WarpBulletEntity>create(MobCategory.MISC,WarpBulletEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F,0.25F)).trackRangeBlocks(4).trackRangeBlocks(4).build()
        );
    }

}
