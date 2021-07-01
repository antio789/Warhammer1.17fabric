package warhammermod.utils;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import warhammermod.Items.ItemsInit;

public class reference {
    public static final String modid = "warhammermod";
    public static final String cartridge = "warhammermod:cartridge";
    public static final String shotgun_shells = "warhammermod:shotgun shells";
    public static final String grenade = "warhammermod:grenade";
    public static final String warpstone = "warhammermod:warpstone";
    public static final String Cartridge = "cartridge";
    public static final String Shotgun_Shells = "shotgun_shell";
    public static final String Grenade = "grenade";
    public static final String Warpstone = "warpstone";
    public static final String slave = "slave";
    public static final String clanrat = "clanrat";
    public static final String stormvermin = "stormvermin";
    public static final String gutter_runner = "gutter runner";
    public static final String globadier = "globadier";
    public static final String ratling_gunner = "ratling gunner";
    public static final CreativeModeTab warhammer = FabricItemGroupBuilder.build(new ResourceLocation(reference.modid,"warhammer"),()-> new ItemStack(ItemsInit.GHAL_MARAZ));



}
