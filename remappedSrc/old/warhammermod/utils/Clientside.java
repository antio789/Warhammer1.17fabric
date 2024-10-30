package warhammermod.utils;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import warhammermod.Entities.Living.Renders.DwarfRenderer;
import warhammermod.Entities.Living.Renders.Models.DwarfModel;
import warhammermod.Entities.Living.Renders.Models.Pegasusmodel;
import warhammermod.Entities.Living.Renders.Models.SkavenModel;
import warhammermod.Entities.Living.Renders.PegasusRenderer;
import warhammermod.Entities.Living.Renders.SkavenRenderer;
import warhammermod.Entities.Projectile.Render.*;
import warhammermod.Entities.Projectile.Render.Model.BulletModel;
import warhammermod.Entities.Projectile.Render.Model.GrapeshotModel;
import warhammermod.Entities.Projectile.Render.Model.GrenadeModel;
import warhammermod.Entities.Projectile.Render.Model.WarpBulletModel;
import warhammermod.Items.ItemsInit;
import warhammermod.Items.Render.Model.*;
import warhammermod.Items.Render.RenderRatlingGun;
import warhammermod.Items.Render.RenderRepeater;
import warhammermod.Items.Render.RenderShield;
import warhammermod.Items.Render.RenderSling;
import warhammermod.utils.Registry.Entityinit;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class Clientside implements ClientModInitializer {
    public static final Identifier PacketID = new Identifier(reference.modid, "spawn_packet");
    public static KeyBinding pegasus_down;
    public static KeyBinding Wiki_Map;

    private final BuiltinItemRendererRegistry.DynamicItemRenderer renderer = new RenderRepeater();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer RatlingRender = new RenderRatlingGun();

    private final BuiltinItemRendererRegistry.DynamicItemRenderer HEShieldRender = new RenderShield();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer DEShieldRender = new RenderShield();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer EMShieldRender = new RenderShield();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer SKShieldRender = new RenderShield();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer DwarfShieldRender = new RenderShield();

    private final BuiltinItemRendererRegistry.DynamicItemRenderer SlingRender = new RenderSling();

    public static final EntityModelLayer DEShield = new EntityModelLayer(location("deshield"), "main");
    public static final EntityModelLayer HEShield = new EntityModelLayer(location("heshield"), "main");
    public static final EntityModelLayer EMShield = new EntityModelLayer(location("emshield"), "main");
    public static final EntityModelLayer SKShield = new EntityModelLayer(location("skshield"), "main");
    public static final EntityModelLayer DWShield = new EntityModelLayer(location("dwshield"), "main");
    public static final EntityModelLayer Repeaterlayer = new EntityModelLayer(location("repeaterlayer"), "main");
    public static final EntityModelLayer RAtlingLayer = new EntityModelLayer(location("ratlinglayer"), "main");

    public static final EntityModelLayer Pegasus = new EntityModelLayer(location("pegasus"), "main");
    public static final EntityModelLayer Skaven = new EntityModelLayer(location("skaven"), "main");
    public static final EntityModelLayer Dwarf = new EntityModelLayer(location("dwarf"), "main");

    public static final EntityModelLayer Bullet = new EntityModelLayer(location("bullet"),"main");
    public static final EntityModelLayer WarpBullet = new EntityModelLayer(location("warpbullet"),"main");
    public static final EntityModelLayer Grenade = new EntityModelLayer(location("grenade"),"main");
    public static final EntityModelLayer Grapeshot = new EntityModelLayer(location("grapeshot"),"main");

    public static final EntityModelLayer Sling = new EntityModelLayer(location("sling"),"main");

    public static final EntityModelLayer pegasus_armor = new EntityModelLayer(location("pegasus_armor"),"main");

    private static Identifier location(String string){
        return new Identifier(reference.modid,string);
    }
    @Override
    public void onInitializeClient(){

        EntityModelLayerRegistry.registerModelLayer(DEShield, DarkElfshieldmodel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(HEShield, HighelfshieldModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(EMShield, EmpireShieldmodel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(SKShield, SkavenShieldModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(DWShield, DwarfshieldModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(Repeaterlayer, RepeaterModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(RAtlingLayer, RatlingGunModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(Bullet, (BulletModel::createLayer));
        EntityModelLayerRegistry.registerModelLayer(WarpBullet, (WarpBulletModel::createLayer));
        EntityModelLayerRegistry.registerModelLayer(Grenade, (GrenadeModel::createLayer));
        EntityModelLayerRegistry.registerModelLayer(Grapeshot, (GrapeshotModel::createLayer));
        EntityModelLayerRegistry.registerModelLayer(Sling, (SlingModel::createLayer));
        EntityModelLayerRegistry.registerModelLayer(Pegasus, Pegasusmodel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(pegasus_armor, Pegasusmodel::createarmorLayer);

        EntityModelLayerRegistry.registerModelLayer(Skaven, SkavenModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(Dwarf, DwarfModel::createBodyLayer);

        BuiltinItemRendererRegistry.INSTANCE.register(ItemsInit.repeater_handgun,renderer);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsInit.RatlingGun,RatlingRender);

        BuiltinItemRendererRegistry.INSTANCE.register(ItemsInit.High_Elf_Shield,HEShieldRender);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsInit.Dark_Elf_Shield,DEShieldRender);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsInit.Dwarf_shield,DwarfShieldRender);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsInit.Imperial_shield,EMShieldRender);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsInit.Skaven_shield,SKShieldRender);
        
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsInit.Sling,SlingRender);
        registerkeys();
        receiveEntityPacket();

        EntityRendererRegistry.INSTANCE.register(Entityinit.halberdthrust, HalberdRender::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.Bullet, BulletRender::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.SpearProjectile, RenderSpear::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.STONEENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.Flame, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.Grenade, GrenadeRender::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.Shotentity, ShotRender::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.WarpBullet, WarpbulletRender::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.Pegasus, PegasusRenderer::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.SKAVEN, SkavenRenderer::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.DWARF, DwarfRenderer::new);


    }

    public void registerkeys(){
        pegasus_down = KeyBindingHelper.registerKeyBinding(new KeyBinding("key"+reference.modid+"pegasusdown",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category"+reference.modid
                ));
        Wiki_Map = KeyBindingHelper.registerKeyBinding(new KeyBinding("key"+reference.modid+"wiki_map",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                "category"+reference.modid
        ));
    }

    public void receiveEntityPacket() {

        ClientSidePacketRegistry.INSTANCE.register(PacketID, (ctx, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(MinecraftClient.getInstance().world);
                if (e == null)
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getRawId(et) + "\"!");
                e.setPacketCoordinates(pos);
                e.setPosition(pos.x, pos.y, pos.z);
                e.setPitch(pitch);
                e.setYaw(yaw);
                e.setId(entityId);
                e.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
        });
    }
}
