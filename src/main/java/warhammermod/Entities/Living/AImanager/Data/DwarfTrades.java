package warhammermod.Entities.Living.AImanager.Data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.util.Util;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradeOffers.Factory;
import warhammermod.utils.Registry.ItemsInit;
import warhammermod.utils.functions;

import java.util.Map;

public class DwarfTrades {
   public static final Map<DwarfProfessionRecord, Int2ObjectMap<Factory[]>> TRADES = Util.make(Maps.newHashMap(), (professiontrades) -> {
      professiontrades.put(DwarfProfessionRecord.FARMER, copyToFastUtilMap(ImmutableMap.of(1, new Factory[]{new TradeOffers.BuyItemFactory(Items.WHEAT, 20, 16, 2), new TradeOffers.BuyItemFactory(Items.POTATO, 26, 16, 2), new TradeOffers.BuyItemFactory(Items.CARROT, 22, 16, 2), new TradeOffers.SellItemFactory(ItemsInit.BEER, 8, 1, 2)}, 2, new Factory[]{new TradeOffers.BuyItemFactory(Blocks.PUMPKIN, 6, 12, 10), new TradeOffers.SellItemFactory(Items.PUMPKIN_PIE, 1, 4, 5), new TradeOffers.SellItemFactory(Items.APPLE, 1, 4, 16, 5)}, 3, new Factory[]{new TradeOffers.SellItemFactory(Items.COOKIE, 3, 18, 10), new TradeOffers.BuyItemFactory(Blocks.MELON, 4, 12, 20)}, 4, new Factory[]{new TradeOffers.SellItemFactory(Blocks.CAKE, 1, 1, 12, 15), new TradeOffers.SellSuspiciousStewFactory(StatusEffects.NIGHT_VISION, 100, 15), new TradeOffers.SellSuspiciousStewFactory(StatusEffects.JUMP_BOOST, 160, 15), new TradeOffers.SellSuspiciousStewFactory(StatusEffects.WEAKNESS, 140, 15), new TradeOffers.SellSuspiciousStewFactory(StatusEffects.BLINDNESS, 120, 15), new TradeOffers.SellSuspiciousStewFactory(StatusEffects.POISON, 280, 15), new TradeOffers.SellSuspiciousStewFactory(StatusEffects.SATURATION, 7, 15)}, 5, new Factory[]{new TradeOffers.SellItemFactory(Items.GOLDEN_CARROT, 3, 3, 30), new TradeOffers.SellItemFactory(Items.GLISTERING_MELON_SLICE, 4, 3, 30)})));
      professiontrades.put(DwarfProfessionRecord.Miner, copyToFastUtilMap(ImmutableMap.of(1, new Factory[]{new TradeOffers.BuyItemFactory(Items.COAL, 15, 16, 2), new TradeOffers.BuyItemFactory(Items.REDSTONE, 32, 14, 1), new TradeOffers.BuyItemFactory(Items.GOLD_INGOT, 3, 12, 5), new TradeOffers.BuyItemFactory(Items.OBSIDIAN, 3, 12, 15), new TradeOffers.BuyItemFactory(Items.IRON_INGOT, 8, 10, 3)}, 2, new Factory[]{new TradeOffers.SellItemFactory(Items.MINECART, 3, 1, 15), new TradeOffers.SellItemFactory(Items.LANTERN, 1, 2, 10), new TradeOffers.BuyItemFactory(ItemsInit.BEER, 3, 30, 8)}, 3, new Factory[]{new TradeOffers.BuyItemFactory(Items.TNT, 8, 10, 30), new TradeOffers.SellItemFactory(Items.QUARTZ, 35, 4, 5, 30), new TradeOffers.SellItemFactory(Items.RAIL, 1, 2, 5)}, 4, new Factory[]{new TradeOffers.SellItemFactory(Items.POWERED_RAIL, 3, 18, 20)}, 5, new Factory[]{new TradeOffers.SellItemFactory(Items.QUARTZ, 20, 1, 30), new TradeOffers.SellEnchantedToolFactory(ItemsInit.GreatPick, 25, 1, 20)})));
      professiontrades.put(DwarfProfessionRecord.Builder, copyToFastUtilMap(ImmutableMap.of(1, new Factory[]{new TradeOffers.SellItemFactory(Items.CLAY_BALL, 10, 1, 2), new TradeOffers.SellItemFactory(Items.BRICK, 20, 1, 16, 10), new TradeOffers.SellItemFactory(Items.SANDSTONE, 8, 2, 20, 5), new TradeOffers.SellItemFactory(Items.RED_SANDSTONE, 12, 2, 20, 5)}, 2, new Factory[]{new TradeOffers.BuyItemFactory(Blocks.STONE, 20, 16, 10), new TradeOffers.SellItemFactory(Blocks.CHISELED_STONE_BRICKS, 1, 4, 16, 5)}, 3, new Factory[]{new TradeOffers.BuyItemFactory(Blocks.GRANITE, 16, 16, 20), new TradeOffers.BuyItemFactory(Blocks.ANDESITE, 16, 16, 20), new TradeOffers.BuyItemFactory(Blocks.DIORITE, 16, 16, 20), new TradeOffers.SellItemFactory(Blocks.POLISHED_ANDESITE, 1, 4, 16, 10), new TradeOffers.SellItemFactory(Blocks.POLISHED_DIORITE, 1, 4, 16, 10), new TradeOffers.SellItemFactory(Blocks.POLISHED_GRANITE, 1, 4, 16, 10)}, 4, new Factory[]{new TradeOffers.SellItemFactory(Items.BLACK_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.BLUE_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.CYAN_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.BROWN_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.GRAY_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.GREEN_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.LIME_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.MAGENTA_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.ORANGE_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.PINK_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.RED_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.WHITE_CONCRETE, 2, 1, 12, 15), new TradeOffers.SellItemFactory(Items.YELLOW_CONCRETE, 2, 1, 12, 15)}, 5, new Factory[]{new TradeOffers.SellItemFactory(Blocks.QUARTZ_PILLAR, 1, 2, 20, 30), new TradeOffers.SellItemFactory(Blocks.QUARTZ_BLOCK, 1, 2, 20, 30)})));
      professiontrades.put(DwarfProfessionRecord.Slayer, copyToFastUtilMap(ImmutableMap.of(1, new Factory[]{new TradeOffers.BuyItemFactory(Items.COOKED_BEEF, 12, 15, 5), new TradeOffers.BuyItemFactory(Items.COOKED_CHICKEN, 12, 15, 5), new TradeOffers.BuyItemFactory(Items.COOKED_COD, 12, 15, 5), new TradeOffers.BuyItemFactory(Items.COOKED_MUTTON, 12, 15, 5), new TradeOffers.BuyItemFactory(Items.COOKED_PORKCHOP, 12, 15, 5), new TradeOffers.BuyItemFactory(Items.COOKED_RABBIT, 12, 15, 5), new TradeOffers.BuyItemFactory(Items.COOKED_SALMON, 12, 15, 5)}, 2, new Factory[]{new TradeOffers.BuyItemFactory(Items.RABBIT_STEW, 12, 15, 10), new TradeOffers.BuyItemFactory(ItemsInit.BEER, 3, 30, 15)}, 3, new Factory[]{new TradeOffers.SellItemFactory(Items.BONE, 20, 1, 30, 10)}, 4, new Factory[]{ new TradeOffers.BuyItemFactory(Items.PHANTOM_MEMBRANE, 40, 3, 30)}, 5, new Factory[]{new TradeOffers.SellItemFactory(functions.getrandomskull(), 60, 1, 1, 40)})));
      //p_221237_0_.put(DwarfProfession.Lord,func_221238_a(ImmutableMap.of(1,new Villagers.ItemListing[]{new TradeOffers.BuyItemFactory(ItemsInit.Beer,5,10,10)},2,new VillagerTrades.ItemListing[]{new TradeOffers.BuyItemFactory(Items.field_226638_pX_,8,10,15)},3,new VillagerTrades.ItemListing[]{new TradeOffers.BuyItemFactory(Items.BLAZE_ROD,5,5,40)},4,new VillagerTrades.ItemListing[]{new TradeOffers.BuyItemFactory(Items.WITHER_ROSE,5,3,60)},5,new VillagerTrades.ItemListing[]{new EnchantedItemForEmeralds(ItemsInit.Ghal_Maraz,20,1,50)})));
      professiontrades.put(DwarfProfessionRecord.Engineer, copyToFastUtilMap(ImmutableMap.of(1, new Factory[]{new TradeOffers.SellItemFactory(ItemsInit.Cartridge, 5, 1, 5), new TradeOffers.SellItemFactory(ItemsInit.Shotshell, 6, 1, 5)}, 2, new Factory[]{new TradeOffers.BuyItemFactory(ItemsInit.BEER, 2, 30, 8), new TradeOffers.SellItemFactory(functions.getRandomShield(), 20, 1, 20), new TradeOffers.SellItemFactory(Items.PISTON, 8, 1, 30, 10), new TradeOffers.SellItemFactory(Items.DISPENSER, 16, 1, 38, 5)}, 3, new Factory[]{new TradeOffers.SellEnchantedToolFactory(ItemsInit.thunderer_handgun, 15, 1, 30), new TradeOffers.SellItemFactory(functions.getRandomShield(), 15, 1, 1, 15), new TradeOffers.SellEnchantedToolFactory(functions.getRandomarmor(0), 15, 5, 20), new TradeOffers.SellEnchantedToolFactory(functions.getRandomarmor(1), 15, 5, 20)}, 4, new Factory[]{new TradeOffers.SellEnchantedToolFactory(ItemsInit.GrudgeRaker, 16, 1, 20)}, 5, new Factory[]{new TradeOffers.SellEnchantedToolFactory(ItemsInit.DrakeGun, 20, 1, 30)})));
   });

   private static Int2ObjectMap<Factory[]> copyToFastUtilMap(ImmutableMap<Integer, Factory[]> map) {
      return new Int2ObjectOpenHashMap<Factory[]>(map);
   }
}

