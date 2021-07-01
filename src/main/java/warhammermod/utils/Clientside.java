package warhammermod.utils;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import warhammermod.Entities.Living.Renders.DwarfRenderer;
import warhammermod.Entities.Living.Renders.Models.DwarfModel;
import warhammermod.Entities.Living.Renders.Models.Pegasusmodel;
import warhammermod.Entities.Living.Renders.Models.SkavenModel;
import warhammermod.Entities.Living.Renders.PegasusRenderer;
import warhammermod.Entities.Living.Renders.SkavenRenderer;
import warhammermod.Entities.Projectile.Render.*;
import warhammermod.Entities.Projectile.Render.Model.BulletModel;
import warhammermod.Entities.Projectile.Render.Model.WarpBulletModel;
import warhammermod.Entities.Projectile.Render.Model.GrapeshotModel;
import warhammermod.Entities.Projectile.Render.Model.GrenadeModel;
import warhammermod.Items.GunBase;
import warhammermod.Items.ItemsInit;
import warhammermod.Items.Render.Model.*;
import warhammermod.Items.Render.RenderRatlingGun;
import warhammermod.Items.Render.RenderRepeater;
import warhammermod.Items.Render.RenderShield;
import warhammermod.Items.Render.RenderSling;
import warhammermod.utils.Registry.Entityinit;

import java.util.UUID;

import static warhammermod.Items.GunBase.isCharged;

@Environment(EnvType.CLIENT)
public class Clientside implements ClientModInitializer {
    public static final ResourceLocation PacketID = new ResourceLocation(reference.modid, "spawn_packet");
    public static KeyMapping pegasus_down;
    public static KeyMapping Wiki_Map;

    private final BuiltinItemRendererRegistry.DynamicItemRenderer renderer = new RenderRepeater();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer RatlingRender = new RenderRatlingGun();

    private final BuiltinItemRendererRegistry.DynamicItemRenderer HEShieldRender = new RenderShield();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer DEShieldRender = new RenderShield();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer EMShieldRender = new RenderShield();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer SKShieldRender = new RenderShield();
    private final BuiltinItemRendererRegistry.DynamicItemRenderer DwarfShieldRender = new RenderShield();

    private final BuiltinItemRendererRegistry.DynamicItemRenderer SlingRender = new RenderSling();

    public static final ModelLayerLocation DEShield = new ModelLayerLocation(location("deshield"), "main");
    public static final ModelLayerLocation HEShield = new ModelLayerLocation(location("heshield"), "main");
    public static final ModelLayerLocation EMShield = new ModelLayerLocation(location("emshield"), "main");
    public static final ModelLayerLocation SKShield = new ModelLayerLocation(location("skshield"), "main");
    public static final ModelLayerLocation DWShield = new ModelLayerLocation(location("dwshield"), "main");
    public static final ModelLayerLocation Repeaterlayer = new ModelLayerLocation(location("repeaterlayer"), "main");
    public static final ModelLayerLocation RAtlingLayer = new ModelLayerLocation(location("ratlinglayer"), "main");

    public static final ModelLayerLocation Pegasus = new ModelLayerLocation(location("pegasus"), "main");
    public static final ModelLayerLocation Skaven = new ModelLayerLocation(location("skaven"), "main");
    public static final ModelLayerLocation Dwarf = new ModelLayerLocation(location("dwarf"), "main");

    public static final ModelLayerLocation Bullet = new ModelLayerLocation(location("bullet"),"main");
    public static final ModelLayerLocation WarpBullet = new ModelLayerLocation(location("warpbullet"),"main");
    public static final ModelLayerLocation Grenade = new ModelLayerLocation(location("grenade"),"main");
    public static final ModelLayerLocation Grapeshot = new ModelLayerLocation(location("grapeshot"),"main");

    public static final ModelLayerLocation Sling = new ModelLayerLocation(location("sling"),"main");

    public static final ModelLayerLocation pegasus_armor = new ModelLayerLocation(location("pegasus_armor"),"main");

    private static ResourceLocation location(String string){
        return new ResourceLocation(reference.modid,string);
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
        EntityRendererRegistry.INSTANCE.register(Entityinit.STONEENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.Flame, ThrownItemRenderer::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.Grenade, GrenadeRender::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.Shotentity, ShotRender::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.WarpBullet, WarpbulletRender::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.Pegasus, PegasusRenderer::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.SKAVEN, SkavenRenderer::new);
        EntityRendererRegistry.INSTANCE.register(Entityinit.DWARF, DwarfRenderer::new);


    }

    public void registerkeys(){
        pegasus_down = KeyBindingHelper.registerKeyBinding(new KeyMapping("key"+reference.modid+"pegasusdown",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category"+reference.modid
                ));
        Wiki_Map = KeyBindingHelper.registerKeyBinding(new KeyMapping("key"+reference.modid+"wiki_map",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                "category"+reference.modid
        ));
    }

    public void receiveEntityPacket() {

        ClientSidePacketRegistry.INSTANCE.register(PacketID, (ctx, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.byId(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUUID();
            int entityId = byteBuf.readVarInt();
            Vec3 pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.getTaskQueue().execute(() -> {
                if (Minecraft.getInstance().level == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(Minecraft.getInstance().level);
                if (e == null)
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                e.setPacketCoordinates(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.setXRot(pitch);
                e.setYRot(yaw);
                e.setId(entityId);
                e.setUUID(uuid);
                Minecraft.getInstance().level.putNonPlayerEntity(entityId, e);
            });
        });
    }
}
