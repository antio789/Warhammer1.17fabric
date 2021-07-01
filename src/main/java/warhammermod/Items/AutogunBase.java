package warhammermod.Items;


import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Random;

public class AutogunBase extends Item implements IReloadItem {
    public int timetoreload;
    public int Magsize;
    private final Item AmmoType;
    protected Random rand= new Random();
    public boolean hasshield=false;
    private final int FireRate;
    private final int AmmoConsumption;

    public AutogunBase(Properties properties, Item ammotype, int time, int magsize, int ammoconsumption, int firerate) {
        super(properties);
        Magsize=magsize;
        timetoreload=time;
        AmmoType=ammotype;
        FireRate=firerate;
        AmmoConsumption=ammoconsumption;


        FabricModelPredicateProviderRegistry.register(this,new ResourceLocation("reloading"),(stack, worldIn, entityIn,i) ->  {
            CompoundTag ammocounter = stack.getTag();
            if (entityIn != null && entityIn instanceof Player && !((Player) entityIn).isCreative() && entityIn.isUsingItem() && entityIn.getUseItem() == stack && (ammocounter == null || ammocounter.getInt("ammo") <= 0)) {
                if (ammocounter!=null && !isCharged(stack) && (entityIn.getTicksUsingItem())<timetoreload) return 2.0F;
                else return 1.0F;
            }
            else return 0.0F;

        });

        FabricModelPredicateProviderRegistry.register(this,new ResourceLocation("aiming3d"),(p_210309_0_, p_210309_1_, p_210309_2_,i) -> {
            return p_210309_2_ != null && p_210309_2_.isUsingItem() && p_210309_2_.getUseItem() == p_210309_0_ ? 1.0F : 0.0F;
        });
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player playerIn, InteractionHand hand) {

        ItemStack stack = playerIn.getItemInHand(hand);
        if(isCharged(stack) ||playerIn.isCreative()|| !findAmmo(playerIn).isEmpty()){
            playerIn.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
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
        return stack.getItem().equals(AmmoType);
    }

    public void onUseTick(Level world, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof Player){
            Player entityplayer = (Player) player;
            if((isCharged(stack) && (getUseDuration(stack)-count)/getFireRate()<=getCharge(stack))|| entityplayer.isCreative()) {
                if((getUseDuration(stack)-count)!=0 && (getUseDuration(stack)-count)%getFireRate()==0){
                    fire( entityplayer,world,stack);
                }
            }
            else if ((count == getUseDuration(stack) - timetoreload) && !isCharged(stack)) {
                world.playSound(null,player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS,1,1);
            }
        }
    }
    public int getFireRate() {
        return FireRate;
    }
    public void fire(Player player, Level world, ItemStack stack){}

    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player && !worldIn.isClientSide() && !((Player) entityLiving).isCreative()) {

            if(isCharged(stack)) {
                setCharge(stack,getCharge(stack)-(getUseDuration(stack)-timeLeft)/getFireRate());
            }else{
                Player playerIn = (Player)entityLiving;
                stack.hurtAndBreak(1, playerIn, (p_220009_1_) -> {
                    p_220009_1_.broadcastBreakEvent(playerIn.getUsedItemHand());
                });
                int ammoreserve = this.findAmmo(playerIn).getCount();
                if ((ammoreserve >= 0)) {
                    this.findAmmo(playerIn).shrink(Math.min(ammoreserve,AmmoConsumption));
                    setCharge(stack,(int)((float)Magsize/AmmoConsumption* Math.min(ammoreserve,AmmoConsumption)));
                }
            }
        }
    }

    public static boolean isCharged(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getInt("ammo")>0;
    }

    public static int getCharge(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTag();
        if(compoundnbt == null){return 0;}
        return compoundnbt.getInt("ammo");
    }

    public static void setCharge(ItemStack stack, int ammo) {
        CompoundTag compoundnbt = stack.getOrCreateTag();
        compoundnbt.putInt("ammo", ammo);
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public int getTimetoreload(){ return timetoreload; }

    public boolean isReadytoFire(ItemStack stack){
        return isCharged(stack);
    }

    public int getItemEnchantability() {
        return 1;
    }
}
