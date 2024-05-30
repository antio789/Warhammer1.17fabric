package warhammermod.Items.ranged;


import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import warhammermod.Items.IReloadItem;
import warhammermod.Items.ItemsInit;
import warhammermod.Items.WHCustomenchantements;
import warhammermod.Items.melee.ShieldTemplate;
import warhammermod.utils.reference;

import java.util.Random;


public class GunSwordTemplate extends SwordItem implements WHCustomenchantements, IReloadItem {
    float damage;
    public int timetoreload;
    public int Magsize;
    protected Random rand= new Random();
    public boolean hasshield=false; //use player.getitemoffhand instead
    public float width = 1.5F;
    public float height = 1.5F;



    public GunSwordTemplate(ToolMaterial tier, int time, int magsize, float damagein){
        super(tier,3 ,-2.4F,(new Settings()).tab(reference.warhammer));
        damage=damagein;
        timetoreload=time;
        Magsize=magsize;
        FabricModelPredicateProviderRegistry.register(this,new Identifier("firing"),(stack, worldIn, entityIn,i) ->  {
            ;
            return stack != null && entityIn instanceof PlayerEntity && entityIn.isUsingItem() && entityIn.getActiveItem() == stack && (isCharged(stack) || ((PlayerEntity) entityIn).isCreative())? 1.0F : 0.0F;

        });
        FabricModelPredicateProviderRegistry.register(this,new Identifier("aimwithshield"),(stack, worldIn, entityIn,i) -> {
            NbtCompound ammocounter = stack.getNbt();
            if (entityIn instanceof PlayerEntity && !(((PlayerEntity) entityIn).isCreative()) && entityIn.isUsingItem() && entityIn.getActiveItem() == stack && isCharged(stack) && hasshield((PlayerEntity) entityIn)) {
                return 1.0F;
            }
            else return 0.0F;

        });
    }

    public boolean isReadytoFire(ItemStack stack){
        return isCharged(stack);
    }

    public ItemStack findAmmo(PlayerEntity player) {
        if (this.isAmmo(player.getStackInHand(Hand.OFF_HAND))) {
            return player.getStackInHand(Hand.OFF_HAND);
        } else if (this.isAmmo(player.getStackInHand(Hand.MAIN_HAND))) {
            return player.getStackInHand(Hand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().size(); ++i) {
                ItemStack itemstack = player.getInventory().getStack(i);
                if (this.isAmmo(itemstack)) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    private boolean isAmmo(ItemStack stack) {
        return stack.getItem().equals(ItemsInit.Cartridge);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand hand) {
        ItemStack stack = playerIn.getStackInHand(hand);
        if(isCharged(stack) || playerIn.isCreative()|| !findAmmo(playerIn).isEmpty()){
            playerIn.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }


    @Override
    public void usageTick(World world, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof PlayerEntity){
            if ((count == getMaxUseTime(stack) - timetoreload) && !isCharged(stack) && !((PlayerEntity) player).isCreative() && !world.isClient()) {
                world.playSound(null,player.getBlockPos(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS,1,1);
            }
        }
    }


    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity entityplayer = (PlayerEntity) entityLiving;
            NbtCompound tag = stack.getOrCreateNbt();
            if (isCharged(stack) || entityplayer.isCreative()) {
                fire(entityplayer,worldIn,stack);
                if(!entityplayer.isCreative())setCharge(stack,getCharge(stack,tag)-1,tag);
            }
            else if(timetoreload<=getMaxUseTime(stack)-timeLeft && !worldIn.isClient()) {
                int ammoreserve = this.findAmmo(entityplayer).getCount();
                int infinitylevel = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack);
                if (ammoreserve < Magsize) {
                    if (infinitylevel == 0) {
                        this.findAmmo(entityplayer).decrement(ammoreserve);
                    }
                    setCharge(stack,ammoreserve,tag);
                } else {
                    if (infinitylevel == 0) {
                        this.findAmmo(entityplayer).decrement(Magsize);
                    }
                    setCharge(stack,Magsize,tag);
                }
            }
        }

    }

    public void inventoryTick(ItemStack p_77663_1_, World p_77663_2_, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        if(entity instanceof PlayerEntity)  setshield((PlayerEntity) entity);
    }

    public void fire(PlayerEntity player, World world, ItemStack stack){}

    public static boolean isCharged(ItemStack stack) {
        NbtCompound compoundnbt = stack.getNbt();
        return compoundnbt != null && compoundnbt.getInt("ammo")>0;
    }

    public static int getCharge(ItemStack stack, NbtCompound nbt) {
        NbtCompound compoundnbt = stack.getNbt();
        if(compoundnbt == null){return 0;}
        return compoundnbt.getInt("ammo");
    }

    public static void setCharge(ItemStack stack, int ammo, NbtCompound nbt) {
        NbtCompound compoundnbt = stack.getOrCreateNbt();
        compoundnbt.putInt("ammo", ammo);
    }

    public int getMaxUseTime(ItemStack stack) { return 72000; }

    public UseAction getUseAction(ItemStack stack) {
        return (isCharged(stack) && hasshield) ? UseAction.BLOCK:UseAction.BOW;
    }

    public Boolean hasshield(PlayerEntity player){
        return player.getOffHandStack().getItem() instanceof ShieldTemplate;
    }
    private void setshield(PlayerEntity player){ hasshield = hasshield(player); }

    public int getTimetoreload(){ return timetoreload; }

    @Override
    public Boolean isCorrectEnchantementatTable(Enchantment enchantment) {
        return this.isCorrectEnchantement(enchantment.target,enchantment.getTranslationKey());
    }

    @Override
    public Boolean isCorrectEnchantement(EnchantmentTarget enchantmentCategory, String ID) {
        return enchantmentCategory==EnchantmentTarget.BOW || enchantmentCategory.isAcceptableItem(this);
    }
}
