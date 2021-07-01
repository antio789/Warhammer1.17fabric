package warhammermod.Items.ranged;


import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
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



    public GunSwordTemplate(Tier tier, int time, int magsize, float damagein){
        super(tier,3 ,-2.4F,(new Properties()).tab(reference.warhammer));
        damage=damagein;
        timetoreload=time;
        Magsize=magsize;
        FabricModelPredicateProviderRegistry.register(this,new ResourceLocation("firing"),(stack, worldIn, entityIn,i) ->  {
            ;
            return stack != null && entityIn instanceof Player && entityIn.isUsingItem() && entityIn.getUseItem() == stack && (isCharged(stack) || ((Player) entityIn).isCreative())? 1.0F : 0.0F;

        });
        FabricModelPredicateProviderRegistry.register(this,new ResourceLocation("aimwithshield"),(stack, worldIn, entityIn,i) -> {
            CompoundTag ammocounter = stack.getTag();
            if (entityIn instanceof Player && !(((Player) entityIn).isCreative()) && entityIn.isUsingItem() && entityIn.getUseItem() == stack && isCharged(stack) && hasshield((Player) entityIn)) {
                return 1.0F;
            }
            else return 0.0F;

        });
    }

    public boolean isReadytoFire(ItemStack stack){
        return isCharged(stack);
    }

    public ItemStack findAmmo(Player player) {
        if (this.isAmmo(player.getItemInHand(InteractionHand.OFF_HAND))) {
            return player.getItemInHand(InteractionHand.OFF_HAND);
        } else if (this.isAmmo(player.getItemInHand(InteractionHand.MAIN_HAND))) {
            return player.getItemInHand(InteractionHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack itemstack = player.getInventory().getItem(i);
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

    public InteractionResultHolder<ItemStack> use(Level world, Player playerIn, InteractionHand hand) {
        ItemStack stack = playerIn.getItemInHand(hand);
        if(isCharged(stack) || playerIn.isCreative()|| !findAmmo(playerIn).isEmpty()){
            playerIn.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }


    @Override
    public void onUseTick(Level world, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof Player){
            if ((count == getUseDuration(stack) - timetoreload) && !isCharged(stack) && !((Player) player).isCreative() && !world.isClientSide()) {
                world.playSound(null,player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS,1,1);
            }
        }
    }


    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player) {
            Player entityplayer = (Player) entityLiving;
            CompoundTag tag = stack.getOrCreateTag();
            if (isCharged(stack) || entityplayer.isCreative()) {
                fire(entityplayer,worldIn,stack);
                if(!entityplayer.isCreative())setCharge(stack,getCharge(stack,tag)-1,tag);
            }
            else if(timetoreload<=getUseDuration(stack)-timeLeft && !worldIn.isClientSide()) {
                int ammoreserve = this.findAmmo(entityplayer).getCount();
                int infinitylevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack);
                if (ammoreserve < Magsize) {
                    if (infinitylevel == 0) {
                        this.findAmmo(entityplayer).shrink(ammoreserve);
                    }
                    setCharge(stack,ammoreserve,tag);
                } else {
                    if (infinitylevel == 0) {
                        this.findAmmo(entityplayer).shrink(Magsize);
                    }
                    setCharge(stack,Magsize,tag);
                }
            }
        }

    }

    public void inventoryTick(ItemStack p_77663_1_, Level p_77663_2_, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        if(entity instanceof Player)  setshield((Player) entity);
    }

    public void fire(Player player, Level world, ItemStack stack){}

    public static boolean isCharged(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getInt("ammo")>0;
    }

    public static int getCharge(ItemStack stack, CompoundTag nbt) {
        CompoundTag compoundnbt = stack.getTag();
        if(compoundnbt == null){return 0;}
        return compoundnbt.getInt("ammo");
    }

    public static void setCharge(ItemStack stack, int ammo, CompoundTag nbt) {
        CompoundTag compoundnbt = stack.getOrCreateTag();
        compoundnbt.putInt("ammo", ammo);
    }

    public int getUseDuration(ItemStack stack) { return 72000; }

    public UseAnim getUseAnimation(ItemStack stack) {
        return (isCharged(stack) && hasshield) ? UseAnim.BLOCK:UseAnim.BOW;
    }

    public Boolean hasshield(Player player){
        return player.getOffhandItem().getItem() instanceof ShieldTemplate;
    }
    private void setshield(Player player){ hasshield = hasshield(player); }

    public int getTimetoreload(){ return timetoreload; }

    @Override
    public Boolean isCorrectEnchantementatTable(Enchantment enchantment) {
        return this.isCorrectEnchantement(enchantment.category,enchantment.getDescriptionId());
    }

    @Override
    public Boolean isCorrectEnchantement(EnchantmentCategory enchantmentCategory, String ID) {
        return enchantmentCategory==EnchantmentCategory.BOW || enchantmentCategory.canEnchant(this);
    }
}
