package warhammermod.Items;


import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.Random;

public class AutogunBase extends Item implements IReloadItem {
    public int timetoreload;
    public int Magsize;
    private final Item AmmoType;
    protected Random rand= new Random();
    public boolean hasshield=false;
    private final int FireRate;
    private final int AmmoConsumption;

    public AutogunBase(Settings properties, Item ammotype, int time, int magsize, int ammoconsumption, int firerate) {
        super(properties);
        Magsize=magsize;
        timetoreload=time;
        AmmoType=ammotype;
        FireRate=firerate;
        AmmoConsumption=ammoconsumption;


        FabricModelPredicateProviderRegistry.register(this,new Identifier("reloading"),(stack, worldIn, entityIn,i) ->  {
            NbtCompound ammocounter = stack.getNbt();
            if (entityIn != null && entityIn instanceof PlayerEntity && !((PlayerEntity) entityIn).isCreative() && entityIn.isUsingItem() && entityIn.getActiveItem() == stack && (ammocounter == null || ammocounter.getInt("ammo") <= 0)) {
                if (ammocounter!=null && !isCharged(stack) && (entityIn.getItemUseTime())<timetoreload) return 2.0F;
                else return 1.0F;
            }
            else return 0.0F;

        });

        FabricModelPredicateProviderRegistry.register(this,new Identifier("aiming3d"),(p_210309_0_, p_210309_1_, p_210309_2_,i) -> {
            return p_210309_2_ != null && p_210309_2_.isUsingItem() && p_210309_2_.getActiveItem() == p_210309_0_ ? 1.0F : 0.0F;
        });
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand hand) {

        ItemStack stack = playerIn.getStackInHand(hand);
        if(isCharged(stack) ||playerIn.isCreative()|| !findAmmo(playerIn).isEmpty()){
            playerIn.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
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
        return stack.getItem().equals(AmmoType);
    }

    public void usageTick(World world, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof PlayerEntity){
            PlayerEntity entityplayer = (PlayerEntity) player;
            if((isCharged(stack) && (getMaxUseTime(stack)-count)/getFireRate()<=getCharge(stack))|| entityplayer.isCreative()) {
                if((getMaxUseTime(stack)-count)!=0 && (getMaxUseTime(stack)-count)%getFireRate()==0){
                    fire( entityplayer,world,stack);
                }
            }
            else if ((count == getMaxUseTime(stack) - timetoreload) && !isCharged(stack)) {
                world.playSound(null,player.getBlockPos(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS,1,1);
            }
        }
    }
    public int getFireRate() {
        return FireRate;
    }
    public void fire(PlayerEntity player, World world, ItemStack stack){}

    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity && !worldIn.isClient() && !((PlayerEntity) entityLiving).isCreative()) {

            if(isCharged(stack)) {
                setCharge(stack,getCharge(stack)-(getMaxUseTime(stack)-timeLeft)/getFireRate());
            }else{
                PlayerEntity playerIn = (PlayerEntity)entityLiving;
                stack.damage(1, playerIn, (p_220009_1_) -> {
                    p_220009_1_.sendToolBreakStatus(playerIn.getActiveHand());
                });
                int ammoreserve = this.findAmmo(playerIn).getCount();
                if ((ammoreserve >= 0)) {
                    this.findAmmo(playerIn).decrement(Math.min(ammoreserve,AmmoConsumption));
                    setCharge(stack,(int)((float)Magsize/AmmoConsumption* Math.min(ammoreserve,AmmoConsumption)));
                }
            }
        }
    }

    public static boolean isCharged(ItemStack stack) {
        NbtCompound compoundnbt = stack.getNbt();
        return compoundnbt != null && compoundnbt.getInt("ammo")>0;
    }

    public static int getCharge(ItemStack stack) {
        NbtCompound compoundnbt = stack.getNbt();
        if(compoundnbt == null){return 0;}
        return compoundnbt.getInt("ammo");
    }

    public static void setCharge(ItemStack stack, int ammo) {
        NbtCompound compoundnbt = stack.getOrCreateNbt();
        compoundnbt.putInt("ammo", ammo);
    }

    @Override
    public int getMaxUseTime(ItemStack p_77626_1_) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public int getTimetoreload(){ return timetoreload; }

    public boolean isReadytoFire(ItemStack stack){
        return isCharged(stack);
    }

    public int getItemEnchantability() {
        return 1;
    }
}
