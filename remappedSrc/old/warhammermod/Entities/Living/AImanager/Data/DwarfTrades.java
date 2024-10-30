package warhammermod.Entities.Living.AImanager.Data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import warhammermod.utils.Registry.ItemsInit;
import warhammermod.utils.functions;

import java.util.Map;
import net.minecraft.Util;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class DwarfTrades {
   public static final Map<DwarfProfessionRecord, Int2ObjectMap<ItemListing[]>> TRADES = Util.make(Maps.newHashMap(), (professiontrades) -> {
      professiontrades.put(DwarfProfessionRecord.FARMER, copyToFastUtilMap(ImmutableMap.of(1, new ItemListing[]{new VillagerTrades.EmeraldForItems(Items.WHEAT, 20, 16, 2), new VillagerTrades.EmeraldForItems(Items.POTATO, 26, 16, 2), new VillagerTrades.EmeraldForItems(Items.CARROT, 22, 16, 2), new VillagerTrades.ItemsForEmeralds(ItemsInit.BEER, 8, 1, 2)}, 2, new ItemListing[]{new VillagerTrades.EmeraldForItems(Blocks.PUMPKIN, 6, 12, 10), new VillagerTrades.ItemsForEmeralds(Items.PUMPKIN_PIE, 1, 4, 5), new VillagerTrades.ItemsForEmeralds(Items.APPLE, 1, 4, 16, 5)}, 3, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.COOKIE, 3, 18, 10), new VillagerTrades.EmeraldForItems(Blocks.MELON, 4, 12, 20)}, 4, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Blocks.CAKE, 1, 1, 12, 15), new VillagerTrades.SuspiciousStewForEmerald(MobEffects.NIGHT_VISION, 100, 15), new VillagerTrades.SuspiciousStewForEmerald(MobEffects.JUMP, 160, 15), new VillagerTrades.SuspiciousStewForEmerald(MobEffects.WEAKNESS, 140, 15), new VillagerTrades.SuspiciousStewForEmerald(MobEffects.BLINDNESS, 120, 15), new VillagerTrades.SuspiciousStewForEmerald(MobEffects.POISON, 280, 15), new VillagerTrades.SuspiciousStewForEmerald(MobEffects.SATURATION, 7, 15)}, 5, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.GOLDEN_CARROT, 3, 3, 30), new VillagerTrades.ItemsForEmeralds(Items.GLISTERING_MELON_SLICE, 4, 3, 30)})));
      professiontrades.put(DwarfProfessionRecord.Miner, copyToFastUtilMap(ImmutableMap.of(1, new ItemListing[]{new VillagerTrades.EmeraldForItems(Items.COAL, 15, 16, 2), new VillagerTrades.EmeraldForItems(Items.REDSTONE, 32, 14, 1), new VillagerTrades.EmeraldForItems(Items.GOLD_INGOT, 3, 12, 5), new VillagerTrades.EmeraldForItems(Items.OBSIDIAN, 3, 12, 15), new VillagerTrades.EmeraldForItems(Items.IRON_INGOT, 8, 10, 3)}, 2, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.MINECART, 3, 1, 15), new VillagerTrades.ItemsForEmeralds(Items.LANTERN, 1, 2, 10), new VillagerTrades.EmeraldForItems(ItemsInit.BEER, 3, 30, 8)}, 3, new ItemListing[]{new VillagerTrades.EmeraldForItems(Items.TNT, 8, 10, 30), new VillagerTrades.ItemsForEmeralds(Items.QUARTZ, 35, 4, 5, 30), new VillagerTrades.ItemsForEmeralds(Items.RAIL, 1, 2, 5)}, 4, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.POWERED_RAIL, 3, 18, 20)}, 5, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.QUARTZ, 20, 1, 30), new VillagerTrades.EnchantedItemForEmeralds(ItemsInit.GreatPick, 25, 1, 20)})));
      professiontrades.put(DwarfProfessionRecord.Builder, copyToFastUtilMap(ImmutableMap.of(1, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.CLAY_BALL, 10, 1, 2), new VillagerTrades.ItemsForEmeralds(Items.BRICK, 20, 1, 16, 10), new VillagerTrades.ItemsForEmeralds(Items.SANDSTONE, 8, 2, 20, 5), new VillagerTrades.ItemsForEmeralds(Items.RED_SANDSTONE, 12, 2, 20, 5)}, 2, new ItemListing[]{new VillagerTrades.EmeraldForItems(Blocks.STONE, 20, 16, 10), new VillagerTrades.ItemsForEmeralds(Blocks.CHISELED_STONE_BRICKS, 1, 4, 16, 5)}, 3, new ItemListing[]{new VillagerTrades.EmeraldForItems(Blocks.GRANITE, 16, 16, 20), new VillagerTrades.EmeraldForItems(Blocks.ANDESITE, 16, 16, 20), new VillagerTrades.EmeraldForItems(Blocks.DIORITE, 16, 16, 20), new VillagerTrades.ItemsForEmeralds(Blocks.POLISHED_ANDESITE, 1, 4, 16, 10), new VillagerTrades.ItemsForEmeralds(Blocks.POLISHED_DIORITE, 1, 4, 16, 10), new VillagerTrades.ItemsForEmeralds(Blocks.POLISHED_GRANITE, 1, 4, 16, 10)}, 4, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.BLACK_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.BLUE_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.CYAN_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.BROWN_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.GRAY_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.GREEN_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.LIGHT_BLUE_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.LIGHT_GRAY_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.LIME_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.MAGENTA_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.ORANGE_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.PINK_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.RED_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.WHITE_CONCRETE, 2, 1, 12, 15), new VillagerTrades.ItemsForEmeralds(Items.YELLOW_CONCRETE, 2, 1, 12, 15)}, 5, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Blocks.QUARTZ_PILLAR, 1, 2, 20, 30), new VillagerTrades.ItemsForEmeralds(Blocks.QUARTZ_BLOCK, 1, 2, 20, 30)})));
      professiontrades.put(DwarfProfessionRecord.Slayer, copyToFastUtilMap(ImmutableMap.of(1, new ItemListing[]{new VillagerTrades.EmeraldForItems(Items.COOKED_BEEF, 12, 15, 5), new VillagerTrades.EmeraldForItems(Items.COOKED_CHICKEN, 12, 15, 5), new VillagerTrades.EmeraldForItems(Items.COOKED_COD, 12, 15, 5), new VillagerTrades.EmeraldForItems(Items.COOKED_MUTTON, 12, 15, 5), new VillagerTrades.EmeraldForItems(Items.COOKED_PORKCHOP, 12, 15, 5), new VillagerTrades.EmeraldForItems(Items.COOKED_RABBIT, 12, 15, 5), new VillagerTrades.EmeraldForItems(Items.COOKED_SALMON, 12, 15, 5)}, 2, new ItemListing[]{new VillagerTrades.EmeraldForItems(Items.RABBIT_STEW, 12, 15, 10), new VillagerTrades.EmeraldForItems(ItemsInit.BEER, 3, 30, 15)}, 3, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.BONE, 20, 1, 30, 10)}, 4, new ItemListing[]{ new VillagerTrades.EmeraldForItems(Items.PHANTOM_MEMBRANE, 40, 3, 30)}, 5, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(functions.getrandomskull(), 60, 1, 1, 40)})));
      //p_221237_0_.put(DwarfProfession.Lord,func_221238_a(ImmutableMap.of(1,new Villagers.ItemListing[]{new TradeOffers.BuyItemFactory(ItemsInit.Beer,5,10,10)},2,new VillagerTrades.ItemListing[]{new TradeOffers.BuyItemFactory(Items.field_226638_pX_,8,10,15)},3,new VillagerTrades.ItemListing[]{new TradeOffers.BuyItemFactory(Items.BLAZE_ROD,5,5,40)},4,new VillagerTrades.ItemListing[]{new TradeOffers.BuyItemFactory(Items.WITHER_ROSE,5,3,60)},5,new VillagerTrades.ItemListing[]{new EnchantedItemForEmeralds(ItemsInit.Ghal_Maraz,20,1,50)})));
      professiontrades.put(DwarfProfessionRecord.Engineer, copyToFastUtilMap(ImmutableMap.of(1, new ItemListing[]{new VillagerTrades.ItemsForEmeralds(ItemsInit.Cartridge, 5, 1, 5), new VillagerTrades.ItemsForEmeralds(ItemsInit.Shotshell, 6, 1, 5)}, 2, new ItemListing[]{new VillagerTrades.EmeraldForItems(ItemsInit.BEER, 2, 30, 8), new VillagerTrades.ItemsForEmeralds(functions.getRandomShield(), 20, 1, 20), new VillagerTrades.ItemsForEmeralds(Items.PISTON, 8, 1, 30, 10), new VillagerTrades.ItemsForEmeralds(Items.DISPENSER, 16, 1, 38, 5)}, 3, new ItemListing[]{new VillagerTrades.EnchantedItemForEmeralds(ItemsInit.thunderer_handgun, 15, 1, 30), new VillagerTrades.ItemsForEmeralds(functions.getRandomShield(), 15, 1, 1, 15), new VillagerTrades.EnchantedItemForEmeralds(functions.getRandomarmor(0), 15, 5, 20), new VillagerTrades.EnchantedItemForEmeralds(functions.getRandomarmor(1), 15, 5, 20)}, 4, new ItemListing[]{new VillagerTrades.EnchantedItemForEmeralds(ItemsInit.GrudgeRaker, 16, 1, 20)}, 5, new ItemListing[]{new VillagerTrades.EnchantedItemForEmeralds(ItemsInit.DrakeGun, 20, 1, 30)})));
   });

   private static Int2ObjectMap<ItemListing[]> copyToFastUtilMap(ImmutableMap<Integer, ItemListing[]> map) {
      return new Int2ObjectOpenHashMap<ItemListing[]>(map);
   }
}

