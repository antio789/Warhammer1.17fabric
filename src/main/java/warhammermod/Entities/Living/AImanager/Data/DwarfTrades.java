package warhammermod.Entities.Living.AImanager.Data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import warhammermod.Items.ItemsInit;
import warhammermod.utils.functions;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class DwarfTrades {
   public static final Map<DwarfProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>> TRADES = Util.make(Maps.newHashMap(), (p_221237_0_) -> {
       p_221237_0_.put(DwarfProfession.FARMER, toIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new EmeraldForItems(Items.WHEAT, 20, 16, 2), new EmeraldForItems(Items.POTATO, 26, 16, 2), new EmeraldForItems(Items.CARROT, 22, 16, 2), new ItemsForEmeralds(ItemsInit.BEER, 8, 1, 2)}, 2, new VillagerTrades.ItemListing[]{new EmeraldForItems(Blocks.PUMPKIN, 6, 12, 10), new ItemsForEmeralds(Items.PUMPKIN_PIE, 1, 4, 5), new ItemsForEmeralds(Items.APPLE, 1, 4, 16, 5)}, 3, new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Items.COOKIE, 3, 18, 10), new EmeraldForItems(Blocks.MELON, 4, 12, 20)}, 4, new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Blocks.CAKE, 1, 1, 12, 15), new SuspisciousStewForEmerald(MobEffects.NIGHT_VISION, 100, 15), new SuspisciousStewForEmerald(MobEffects.JUMP, 160, 15), new SuspisciousStewForEmerald(MobEffects.WEAKNESS, 140, 15), new SuspisciousStewForEmerald(MobEffects.BLINDNESS, 120, 15), new SuspisciousStewForEmerald(MobEffects.POISON, 280, 15), new SuspisciousStewForEmerald(MobEffects.SATURATION, 7, 15)}, 5, new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Items.GOLDEN_CARROT, 3, 3, 30), new ItemsForEmeralds(Items.GLISTERING_MELON_SLICE, 4, 3, 30)})));
       p_221237_0_.put(DwarfProfession.Miner, toIntMap(ImmutableMap.of(1,new VillagerTrades.ItemListing[]{new EmeraldForItems(Items.COAL,15,16,2),new EmeraldForItems(Items.REDSTONE,32,14,1),new EmeraldForItems(Items.GOLD_INGOT,3,12,5),new EmeraldForItems(Items.OBSIDIAN,3,12,15),new EmeraldForItems(Items.IRON_INGOT,8,10,3)},2,new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Items.MINECART,3,1,15),new ItemsForEmeralds(Items.LANTERN,1,2,10),new EmeraldForItems(ItemsInit.BEER,3,30,8)},3,new VillagerTrades.ItemListing[]{new EmeraldForItems(Items.TNT,8,10,30),new ItemsForEmeralds(Items.QUARTZ,35,4,5,30),new ItemsForEmeralds(Items.RAIL,1,2,5)},4,new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Items.POWERED_RAIL,3,18,20)},5, new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Items.QUARTZ, 20, 1, 30),new EnchantedItemForEmeralds(ItemsInit.GreatPick,25,1,20)})));
      p_221237_0_.put(DwarfProfession.Builder, toIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Items.CLAY_BALL, 10, 1, 2), new ItemsForEmeralds(Items.BRICK, 20, 1, 16, 10),new ItemsForEmeralds(Items.SANDSTONE,8,2,20,5),new ItemsForEmeralds(Items.RED_SANDSTONE,12,2,20,5)}, 2, new VillagerTrades.ItemListing[]{new EmeraldForItems(Blocks.STONE, 20, 16, 10), new ItemsForEmeralds(Blocks.CHISELED_STONE_BRICKS, 1, 4, 16, 5)}, 3, new VillagerTrades.ItemListing[]{new EmeraldForItems(Blocks.GRANITE, 16, 16, 20), new EmeraldForItems(Blocks.ANDESITE, 16, 16, 20), new EmeraldForItems(Blocks.DIORITE, 16, 16, 20), new ItemsForEmeralds(Blocks.POLISHED_ANDESITE, 1, 4, 16, 10), new ItemsForEmeralds(Blocks.POLISHED_DIORITE, 1, 4, 16, 10), new ItemsForEmeralds(Blocks.POLISHED_GRANITE, 1, 4, 16, 10)}, 4, new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Items.BLACK_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.BLUE_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.CYAN_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.BROWN_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.GRAY_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.GREEN_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.LIGHT_BLUE_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.LIGHT_GRAY_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.LIME_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.MAGENTA_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.ORANGE_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.PINK_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.RED_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.WHITE_CONCRETE,2,1,12,15),new ItemsForEmeralds(Items.YELLOW_CONCRETE,2,1,12,15)}, 5, new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Blocks.QUARTZ_PILLAR, 1, 2, 20, 30), new ItemsForEmeralds(Blocks.QUARTZ_BLOCK, 1, 2, 20, 30)})));
      p_221237_0_.put(DwarfProfession.Slayer, toIntMap(ImmutableMap.of(1,new VillagerTrades.ItemListing[]{new EmeraldForItems(Items.COOKED_BEEF,12,15,5),new EmeraldForItems(Items.COOKED_CHICKEN,12,15,5),new EmeraldForItems(Items.COOKED_COD,12,15,5),new EmeraldForItems(Items.COOKED_MUTTON,12,15,5),new EmeraldForItems(Items.COOKED_PORKCHOP,12,15,5),new EmeraldForItems(Items.COOKED_RABBIT,12,15,5),new EmeraldForItems(Items.COOKED_SALMON,12,15,5)},2,new VillagerTrades.ItemListing[]{new EmeraldForItems(Items.RABBIT_STEW,12,15,10),new EmeraldForItems(ItemsInit.BEER,3,30,15)},3,new VillagerTrades.ItemListing[]{new ItemsForEmeralds(Items.BONE,20,1,30,10)},4,new VillagerTrades.ItemListing[]{new EmeraldForItems(Items.STRING,18,1,20),new EmeraldForItems(Items.PHANTOM_MEMBRANE,40,3,30)},5,new VillagerTrades.ItemListing[]{new ItemsForEmeralds(functions.getrandomskull(),60,1,1,40)})));
      //p_221237_0_.put(DwarfProfession.Lord,func_221238_a(ImmutableMap.of(1,new Villagers.ItemListing[]{new EmeraldForItems(ItemsInit.Beer,5,10,10)},2,new VillagerTrades.ItemListing[]{new EmeraldForItems(Items.field_226638_pX_,8,10,15)},3,new VillagerTrades.ItemListing[]{new EmeraldForItems(Items.BLAZE_ROD,5,5,40)},4,new VillagerTrades.ItemListing[]{new EmeraldForItems(Items.WITHER_ROSE,5,3,60)},5,new VillagerTrades.ItemListing[]{new EnchantedItemForEmeralds(ItemsInit.Ghal_Maraz,20,1,50)})));
      p_221237_0_.put(DwarfProfession.Engineer, toIntMap(ImmutableMap.of(1,new VillagerTrades.ItemListing[]{new ItemsForEmeralds(ItemsInit.Cartridge,5,1,5),new ItemsForEmeralds(ItemsInit.Grenade,9,1,5),new ItemsForEmeralds(ItemsInit.shotShell,6,1,5)},2,new VillagerTrades.ItemListing[]{new EmeraldForItems(ItemsInit.BEER,2,30,8),new ItemsForEmeralds(functions.getRandomShield(),20,1,20),new ItemsForEmeralds(Items.PISTON,8,1,30,10),new ItemsForEmeralds(Items.DISPENSER,16,1,38,5)},3,new VillagerTrades.ItemListing[]{new EnchantedItemForEmeralds(ItemsInit.thunderer_handgun,15,1,30),new ItemsForEmeralds(functions.getRandomShield(),15,1,1,15),new EnchantedItemForEmeralds(functions.getRandomarmor(0),15,5,20),new EnchantedItemForEmeralds(functions.getRandomarmor(1),15,5,20)},4,new VillagerTrades.ItemListing[]{new EnchantedItemForEmeralds(ItemsInit.GrudgeRaker,16,1,20)},5,new VillagerTrades.ItemListing[]{new EnchantedItemForEmeralds(ItemsInit.DrakeGun,20,1,30)})));
   });

   private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> p_221238_0_) {
      return new Int2ObjectOpenHashMap(p_221238_0_);
   }

   private static class EmeraldForItems implements VillagerTrades.ItemListing {
      private final Item item;
      private final int cost;
      private final int maxUses;
      private final int villagerXp;
      private final float priceMultiplier;

      public EmeraldForItems(ItemLike itemLike, int i, int j, int k) {
         this.item = itemLike.asItem();
         this.cost = i;
         this.maxUses = j;
         this.villagerXp = k;
         this.priceMultiplier = 0.05F;
      }

      public MerchantOffer getOffer(Entity entity, Random random) {
         ItemStack itemStack = new ItemStack(this.item, this.cost);
         return new MerchantOffer(itemStack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, this.priceMultiplier);
      }
   }

   static class ItemsForEmeralds implements VillagerTrades.ItemListing {
      private final ItemStack itemStack;
      private final int emeraldCost;
      private final int numberOfItems;
      private final int maxUses;
      private final int villagerXp;
      private final float priceMultiplier;

      public ItemsForEmeralds(Block block, int i, int j, int k, int l) {
         this(new ItemStack(block), i, j, k, l);
      }

      public ItemsForEmeralds(Item item, int i, int j, int k) {
         this((ItemStack)(new ItemStack(item)), i, j, 12, k);
      }

      public ItemsForEmeralds(Item item, int i, int j, int k, int l) {
         this(new ItemStack(item), i, j, k, l);
      }

      public ItemsForEmeralds(ItemStack itemStack, int i, int j, int k, int l) {
         this(itemStack, i, j, k, l, 0.05F);
      }

      public ItemsForEmeralds(ItemStack itemStack, int i, int j, int k, int l, float f) {
         this.itemStack = itemStack;
         this.emeraldCost = i;
         this.numberOfItems = j;
         this.maxUses = k;
         this.villagerXp = l;
         this.priceMultiplier = f;
      }

      public MerchantOffer getOffer(Entity entity, Random random) {
         return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
      }
   }

   private static class SuspisciousStewForEmerald implements VillagerTrades.ItemListing {
      final MobEffect effect;
      final int duration;
      final int xp;
      private final float priceMultiplier;

      public SuspisciousStewForEmerald(MobEffect mobEffect, int i, int j) {
         this.effect = mobEffect;
         this.duration = i;
         this.xp = j;
         this.priceMultiplier = 0.05F;
      }

      @Nullable
      public MerchantOffer getOffer(Entity entity, Random random) {
         ItemStack itemStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
         SuspiciousStewItem.saveMobEffect(itemStack, this.effect, this.duration);
         return new MerchantOffer(new ItemStack(Items.EMERALD, 1), itemStack, 12, this.xp, this.priceMultiplier);
      }
   }

   private static class ItemsAndEmeraldsToItems implements VillagerTrades.ItemListing {
      private final ItemStack fromItem;
      private final int fromCount;
      private final int emeraldCost;
      private final ItemStack toItem;
      private final int toCount;
      private final int maxUses;
      private final int villagerXp;
      private final float priceMultiplier;

      public ItemsAndEmeraldsToItems(ItemLike itemLike, int i, Item item, int j, int k, int l) {
         this(itemLike, i, 1, item, j, k, l);
      }

      public ItemsAndEmeraldsToItems(ItemLike itemLike, int i, int j, Item item, int k, int l, int m) {
         this.fromItem = new ItemStack(itemLike);
         this.fromCount = i;
         this.emeraldCost = j;
         this.toItem = new ItemStack(item);
         this.toCount = k;
         this.maxUses = l;
         this.villagerXp = m;
         this.priceMultiplier = 0.05F;
      }

      @Nullable
      public MerchantOffer getOffer(Entity entity, Random random) {
         return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.fromItem.getItem(), this.fromCount), new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.villagerXp, this.priceMultiplier);
      }
   }

   private static class EnchantedItemForEmeralds implements VillagerTrades.ItemListing {
      private final ItemStack itemStack;
      private final int baseEmeraldCost;
      private final int maxUses;
      private final int villagerXp;
      private final float priceMultiplier;

      public EnchantedItemForEmeralds(Item item, int i, int j, int k) {
         this(item, i, j, k, 0.05F);
      }

      public EnchantedItemForEmeralds(Item item, int i, int j, int k, float f) {
         this.itemStack = new ItemStack(item);
         this.baseEmeraldCost = i;
         this.maxUses = j;
         this.villagerXp = k;
         this.priceMultiplier = f;
      }

      public MerchantOffer getOffer(Entity entity, Random random) {
         int i = 5 + random.nextInt(15);
         ItemStack itemStack = EnchantmentHelper.enchantItem(random, new ItemStack(this.itemStack.getItem()), i, false);
         int j = Math.min(this.baseEmeraldCost + i, 64);
         ItemStack itemStack2 = new ItemStack(Items.EMERALD, j);
         return new MerchantOffer(itemStack2, itemStack, this.maxUses, this.villagerXp, this.priceMultiplier);
      }
   }

   private static class EmeraldsForVillagerTypeItem implements VillagerTrades.ItemListing {
      private final Map<VillagerType, Item> trades;
      private final int cost;
      private final int maxUses;
      private final int villagerXp;

      public EmeraldsForVillagerTypeItem(int i, int j, int k, Map<VillagerType, Item> map) {
         Registry.VILLAGER_TYPE.stream().filter((villagerType) -> {
            return !map.containsKey(villagerType);
         }).findAny().ifPresent((villagerType) -> {
            throw new IllegalStateException("Missing trade for villager type: " + Registry.VILLAGER_TYPE.getKey(villagerType));
         });
         this.trades = map;
         this.cost = i;
         this.maxUses = j;
         this.villagerXp = k;
      }

      @Nullable
      public MerchantOffer getOffer(Entity entity, Random random) {
         if (entity instanceof VillagerDataHolder) {
            ItemStack itemStack = new ItemStack((ItemLike)this.trades.get(((VillagerDataHolder)entity).getVillagerData().getType()), this.cost);
            return new MerchantOffer(itemStack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, 0.05F);
         } else {
            return null;
         }
      }
   }

   private static class TippedArrowForItemsAndEmeralds implements VillagerTrades.ItemListing {
      private final ItemStack toItem;
      private final int toCount;
      private final int emeraldCost;
      private final int maxUses;
      private final int villagerXp;
      private final Item fromItem;
      private final int fromCount;
      private final float priceMultiplier;

      public TippedArrowForItemsAndEmeralds(Item item, int i, Item item2, int j, int k, int l, int m) {
         this.toItem = new ItemStack(item2);
         this.emeraldCost = k;
         this.maxUses = l;
         this.villagerXp = m;
         this.fromItem = item;
         this.fromCount = i;
         this.toCount = j;
         this.priceMultiplier = 0.05F;
      }

      public MerchantOffer getOffer(Entity entity, Random random) {
         ItemStack itemStack = new ItemStack(Items.EMERALD, this.emeraldCost);
         List<Potion> list = (List)Registry.POTION.stream().filter((potionx) -> {
            return !potionx.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(potionx);
         }).collect(Collectors.toList());
         Potion potion = (Potion)list.get(random.nextInt(list.size()));
         ItemStack itemStack2 = PotionUtils.setPotion(new ItemStack(this.toItem.getItem(), this.toCount), potion);
         return new MerchantOffer(itemStack, new ItemStack(this.fromItem, this.fromCount), itemStack2, this.maxUses, this.villagerXp, this.priceMultiplier);
      }
   }

   private static class EnchantBookForEmeralds implements VillagerTrades.ItemListing {
      private final int villagerXp;

      public EnchantBookForEmeralds(int i) {
         this.villagerXp = i;
      }

      public MerchantOffer getOffer(Entity entity, Random random) {
         List<Enchantment> list = (List)Registry.ENCHANTMENT.stream().filter(Enchantment::isTradeable).collect(Collectors.toList());
         Enchantment enchantment = (Enchantment)list.get(random.nextInt(list.size()));
         int i = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
         ItemStack itemStack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));
         int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
         if (enchantment.isTreasureOnly()) {
            j *= 2;
         }

         if (j > 64) {
            j = 64;
         }

         return new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 12, this.villagerXp, 0.2F);
      }
   }

   private static class TreasureMapForEmeralds implements VillagerTrades.ItemListing {
      private final int emeraldCost;
      private final StructureFeature<?> destination;
      private final MapDecoration.Type destinationType;
      private final int maxUses;
      private final int villagerXp;

      public TreasureMapForEmeralds(int i, StructureFeature<?> structureFeature, MapDecoration.Type type, int j, int k) {
         this.emeraldCost = i;
         this.destination = structureFeature;
         this.destinationType = type;
         this.maxUses = j;
         this.villagerXp = k;
      }

      @Nullable
      public MerchantOffer getOffer(Entity entity, Random random) {
         if (!(entity.level instanceof ServerLevel)) {
            return null;
         } else {
            ServerLevel serverLevel = (ServerLevel)entity.level;
            BlockPos blockPos = serverLevel.findNearestMapFeature(this.destination, entity.blockPosition(), 100, true);
            if (blockPos != null) {
               ItemStack itemStack = MapItem.create(serverLevel, blockPos.getX(), blockPos.getZ(), (byte)2, true, true);
               MapItem.renderBiomePreviewMap(serverLevel, itemStack);
               MapItemSavedData.addTargetDecoration(itemStack, blockPos, "+", this.destinationType);
               String var10003 = this.destination.getFeatureName();
               itemStack.setHoverName(new TranslatableComponent("filled_map." + var10003.toLowerCase(Locale.ROOT)));
               return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(Items.COMPASS), itemStack, this.maxUses, this.villagerXp, 0.2F);
            } else {
               return null;
            }
         }
      }
   }

   private static class DyedArmorForEmeralds implements VillagerTrades.ItemListing {
      private final Item item;
      private final int value;
      private final int maxUses;
      private final int villagerXp;

      public DyedArmorForEmeralds(Item item, int i) {
         this(item, i, 12, 1);
      }

      public DyedArmorForEmeralds(Item item, int i, int j, int k) {
         this.item = item;
         this.value = i;
         this.maxUses = j;
         this.villagerXp = k;
      }

      public MerchantOffer getOffer(Entity entity, Random random) {
         ItemStack itemStack = new ItemStack(Items.EMERALD, this.value);
         ItemStack itemStack2 = new ItemStack(this.item);
         if (this.item instanceof DyeableArmorItem) {
            List<DyeItem> list = Lists.newArrayList();
            list.add(getRandomDye(random));
            if (random.nextFloat() > 0.7F) {
               list.add(getRandomDye(random));
            }

            if (random.nextFloat() > 0.8F) {
               list.add(getRandomDye(random));
            }

            itemStack2 = DyeableLeatherItem.dyeArmor(itemStack2, list);
         }

         return new MerchantOffer(itemStack, itemStack2, this.maxUses, this.villagerXp, 0.2F);
      }

      private static DyeItem getRandomDye(Random random) {
         return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
      }
   }
}