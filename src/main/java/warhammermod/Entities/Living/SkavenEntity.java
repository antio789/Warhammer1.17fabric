package warhammermod.Entities.Living;


import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;
import warhammermod.Entities.Living.AImanager.RangedSkavenAttackGoal;
import warhammermod.Entities.Projectile.StoneEntity;
import warhammermod.Entities.Projectile.WarpBulletEntity;
import warhammermod.Items.IReloadItem;
import warhammermod.Items.ItemsInit;
import warhammermod.Items.ranged.RatlingGun;
import warhammermod.Items.ranged.SlingTemplate;
import warhammermod.Items.ranged.WarpgunTemplate;
import warhammermod.utils.Registry.Entityinit;
import warhammermod.utils.Registry.WHRegistry;
import warhammermod.utils.functions;

import java.util.*;

import static warhammermod.utils.reference.*;


public class SkavenEntity extends HostileEntity implements RangedAttackMob {

    //useless but to remove annoying error message

    /**
     * Goals
     */
    private final RangedSkavenAttackGoal<SkavenEntity> bowGoal = new RangedSkavenAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final ProjectileAttackGoal aiRangedPotion = new ProjectileAttackGoal(this,1,55,10F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false) {
        public void stop() {
            super.stop();
            setAttacking(false);
        }

        public void start() {
            super.start();
            setAttacking(true);
        }
    };

    public void reassessWeaponGoal() {
        if (this.world != null && !this.world.isClient) {
            this.goalSelector.remove(this.meleeGoal);
            this.goalSelector.remove(this.bowGoal);
            this.goalSelector.remove(this.aiRangedPotion);
            ItemStack item = this.getMainHandStack();
            int i = firerate.get(getSkavenTypePosition());
            if (item.getItem() instanceof WarpgunTemplate || item.getItem() instanceof RatlingGun || item.getItem() instanceof SlingTemplate) {
                if (this.world.getDifficulty() != Difficulty.HARD) {
                    i *= 1.5;
                }

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.add(4, this.bowGoal);
            } else if(getSkaventype().equals(globadier)){
                this.goalSelector.add(4,aiRangedPotion);
            }

            else{
                this.goalSelector.add(4, this.meleeGoal);
            }

        }
    }

    protected void initGoals() {

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, CatEntity.class, 6.0F, 1.0D, 1.2D));
        this.targetSelector.add(1, new RevengeGoal(this, SkavenEntity.class).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));

    }
    /**
     * handle all of skaventypes
     *
     */
    public static final TrackedData<String> SkavenType = DataTracker.<String>registerData(SkavenEntity.class, TrackedDataHandlerRegistry.STRING);
    private static ArrayList<String> Types = new ArrayList<String>(Arrays.asList(slave,clanrat,stormvermin,gutter_runner,globadier,ratling_gunner));
    private ArrayList<Float> SkavenSize = new ArrayList<Float>(Arrays.asList(1F,(1.7F/1.6F),(1.8F/1.6F),(1.7F/1.6F),(1.7F/1.6F),(1.7F/1.6F)));
    private ArrayList<Integer> Spawnchance = new ArrayList<Integer>(Arrays.asList(38,27,15,7,4,4));//3
    private ArrayList<Float> reinforcementchance = new ArrayList<Float>(Arrays.asList(0.08F,0.1F,0.14F,0F,0.08F,0.11F));
    private ArrayList<Integer> firerate = new ArrayList<Integer>(Arrays.asList(28,38,0,0,55,6));
    public static ItemStack[][][] SkavenEquipment =  {{{functions.getRandomspear(4)},{new ItemStack(ItemsInit.Sling)}},
            {{functions.getRandomsword(4),new ItemStack(ItemsInit.Skaven_shield)},{new ItemStack(ItemsInit.Warplock_jezzail)}},
            {{functions.getRandomHalberd(4)},{functions.getRandomsword(5),new ItemStack(ItemsInit.Skaven_shield)}},
            {{new ItemStack(ItemsInit.iron_dagger),new ItemStack(ItemsInit.iron_dagger)}},
            {{}},
            {{new ItemStack(ItemsInit.RatlingGun)}},
    };
    private static int Skaven_ranged_dropchance = 100;
    public static void setSkaven_ranged_dropchance(int skaven_ranged_dropchance) {
        Skaven_ranged_dropchance = skaven_ranged_dropchance;
    }
    public static int getSkaven_ranged_dropchance() {
        return Skaven_ranged_dropchance;
    }




    public String getSkaventype(){
        return dataTracker.get(SkavenType);
    }
    public int getSkavenTypePosition(){
        int typepos = Math.max(Types.indexOf(dataTracker.get(SkavenType)),0);
        return typepos;
    }
    public float getSkavenSize(){
        int typepos = Math.max(Types.indexOf(getSkaventype()),0);
        return SkavenSize.get(typepos);
    }
    public EntityDimensions getDimensions(EntityPose pose) {
        return super.getDimensions(pose).scaled(getSkavenSize());
    }

    private void updateSkavenSize(){
        this.calculateDimensions();
        handleAttributes();
    }

    private void setSkavenType(String type){
        this.dataTracker.set(SkavenType,type);
    }

    private String getrandomtype(){
        int total=0;
        for(int x:Spawnchance){
            total+=x;
        }
        int perc = random.nextInt(total);
        int chance=0;
        for(int x=0;x<Spawnchance.size();x++){
            chance+=Spawnchance.get(x);
            if(perc<chance){
                return Types.get(x);
            }
        }
        return Types.get(0);
    }

    /**
     * initializing
     */
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SkavenType, Types.get(0));
    }


    private boolean fixgame; //skaven task dont work without was for 1.15 to test if still necessary

    public SkavenEntity(EntityType<? extends SkavenEntity> p_i48555_1_, World p_i48555_2_) {
        super(p_i48555_1_, p_i48555_2_);
        this.reassessWeaponGoal();
        fixgame=true;
    }
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficultyIn, SpawnReason reason, @Nullable EntityData spawnDataIn, @Nullable NbtCompound dataTag) {
        spawnDataIn = super.initialize(world, difficultyIn, reason, spawnDataIn, dataTag);
        setSkavenType(getrandomtype());
        this.initEquipment(difficultyIn);
        this.updateEnchantments(difficultyIn);
        this.reassessWeaponGoal();
        this.handleAttributes();
        return spawnDataIn;
    }


    public void tickMovement() {
        if(fixgame){
            reassessWeaponGoal();
            this.handleAttributes();
            fixgame=false;
        }
        super.tickMovement();
    }

        /**
         * attributes
         */
    public void updateSkavenAttributes(double speed,double health,double armor,double AD){
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(health);
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(armor);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(AD);
    }
    public void handleAttributes(){
        double speed = 0.55;
        double max_health = 18;
        double armor=0;
        double AD=1.5;

        if(getSkaventype().equals(slave)){
            max_health=14;
        }
        else if(getSkaventype().equals(clanrat)){
            AD=2;
            max_health=16;
            armor=2;

        }
        else if(getSkaventype().equals(stormvermin)){
            AD=3;
            max_health=20;
            armor=5;
        }else if(getSkaventype().equals(gutter_runner)){
            AD=5;
            max_health=17;
            speed=0.75;
        }
        updateSkavenAttributes(speed,max_health,armor,AD);
    };

    public static DefaultAttributeContainer.Builder createHostileAttributes()  {
        return HostileEntity.createHostileAttributes();
    }
    /**
     * behaviour
     */
    public boolean isAffectedBySplashPotions() {
        return !getSkaventype().equals(globadier);
    }

    public void attack(LivingEntity target, float p_82196_2_) {
        Item item = this.getMainHandStack().getItem();
        if(item instanceof WarpgunTemplate){
            attackEntitywithrifle(target,7,11);
        }
        else if(item instanceof RatlingGun){
            attackEntitywithrifle(target,5,26);
        }
        else if(item instanceof SlingTemplate){
            attackEntitywithstone(target,15);
        }
        else if(getSkaventype().equals(globadier)){
            attackpotions(target);
        }
    }
    private void attackpotions(LivingEntity target){


        Vec3d vec3d = target.getVelocity();
        double d0 = target.getX() + vec3d.x - this.getX();
        double d1 = target.getEyeY() - (double)1.1F - this.getY();
        double d2 = target.getZ() + vec3d.z - this.getZ();
        double f = Math.sqrt(d0 * d0 + d2 * d2);
        Potion potion = random.nextBoolean() ? Potions.HARMING : Potions.POISON;

        PotionEntity potionentity = new PotionEntity(this.world, this);
        potionentity.setItem(PotionUtil.setPotion(new ItemStack(Items.LINGERING_POTION), potion));
        potionentity.setPitch(potionentity.getPitch() - -20.0F);
        potionentity.setVelocity(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
        this.world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        this.world.spawnEntity(potionentity);

    }
    private void attackEntitywithrifle(LivingEntity target, int damage, float inaccuracy){
        WarpBulletEntity bullet = new WarpBulletEntity(this,world, damage);
        int j = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER,this);
        if (j > 0) {
            bullet.setpowerDamage(j);
        }
        int k = EnchantmentHelper.getEquipmentLevel(Enchantments.PUNCH, this) + 1;
        if (k > 0) {
            bullet.setknockbacklevel(k);
        }
        double d0 = target.getX()  - this.getX();
        double d1 = target.getBodyY(0.3333333333333333D) - bullet.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double) Math.sqrt(d0 * d0 + d2 * d2);
        bullet.setVelocity(d0,d1+d3*0.06,d2,3,(float)(inaccuracy - this.world.getDifficulty().getId() * 4));//inac14
        this.playSound( SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 2F / (random.nextFloat() * 0.4F + 1.2F) + 0.5F);
        this.world.spawnEntity(bullet);

    }
    private void attackEntitywithstone(LivingEntity target, float inaccuracy){
        StoneEntity stone = new StoneEntity(this,world,3);

        int j = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, this);
        if (j > 0) {
            stone.setTotaldamage(stone.projectiledamage + (float)j * 0.5F + 0.5F);
        }
        int k = EnchantmentHelper.getEquipmentLevel(Enchantments.PUNCH, this) + 1;
        if (k > 0) {
            stone.setknockbacklevel(k);
        }


        if (EnchantmentHelper.getEquipmentLevel(Enchantments.FLAME, this) > 0) {
            stone.setOnFireFor(100);
        }
        double d0 = target.getX()  - this.getX();
        double d1 = target.getBodyY(0.3333333333333333D) - stone.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double) Math.sqrt(d0 * d0 + d2 * d2);
        stone.setVelocity(d0,d1+d3*0.2,d2,1.3F,(float)(inaccuracy - this.world.getDifficulty().getId() * 4));
        this.playSound( SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F));
        this.world.spawnEntity(stone);

    }

    public boolean damage(DamageSource source, float amount){
        if (!super.damage(source, amount)) {
            return false;
        } else if (!(this.world instanceof ServerWorld)) {
            return false;
        } else {
            ServerWorld serverworld = (ServerWorld)this.world;
            LivingEntity livingentity = this.getTarget();
            if (livingentity == null && source.getAttacker() instanceof LivingEntity) {
                livingentity = (LivingEntity)source.getAttacker();
            }




            if (livingentity != null && this.world.getDifficulty() == Difficulty.HARD && (double)this.random.nextFloat() < reinforcementchance.get(getSkavenTypePosition())/2 && this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                SkavenEntity skavenEntity = Entityinit.SKAVEN.create(this.world);
                int i = MathHelper.floor(this.getX());
                int j = MathHelper.floor(this.getY());
                int k = MathHelper.floor(this.getZ());

                for(int l = 0; l < 50; ++l) {
                    int i1 = i + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    int j1 = j + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    int k1 = k + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    BlockPos blockpos = new BlockPos(i1, j1, k1);
                    EntityType<?> entitytype = skavenEntity.getType();
                    SpawnRestriction.Location placementType = SpawnRestriction.getLocation(entitytype);
                    if (SpawnHelper.canSpawn(placementType, this.world, blockpos, entitytype) && SpawnRestriction.canSpawn(entitytype, serverworld, SpawnReason.REINFORCEMENT, blockpos, this.world.random)) {
                        skavenEntity.setPosition((double)i1, (double)j1, (double)k1);
                        if (!this.world.isPlayerInRange((double)i1, (double)j1, (double)k1, 7.0D) && this.world.doesNotIntersectEntities(skavenEntity) && this.world.isSpaceEmpty(skavenEntity) && !this.world.containsFluid(skavenEntity.getBoundingBox())) {

                            skavenEntity.setTarget(livingentity);
                            skavenEntity.initialize(serverworld, this.world.getLocalDifficulty(skavenEntity.getBlockPos()), SpawnReason.REINFORCEMENT, (EntityData)null, (NbtCompound)null);
                            serverworld.spawnEntityAndPassengers(skavenEntity);
                            break;
                        }
                    }
                }
            }

            return true;
        }
    }
    /**
     * other
     */
    protected SoundEvent getAmbientSound() {
        return WHRegistry.ratambient;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return WHRegistry.rathurt;
    }

    protected SoundEvent getDeathSound() {
        return WHRegistry.ratdeath;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
    public float getSoundPitch()
    {
        return 0.4F +this.random.nextFloat()*0.5F;
    }

    protected void initEquipment(LocalDifficulty difficulty)
    {
        int typepos = Math.max(Types.indexOf(getSkaventype()),0);
        int weapontype;
        if(SkavenEquipment[typepos].length<2){
            weapontype = 0;
        }
        else {
            weapontype = this.random.nextInt(2);
        }
        if(SkavenEquipment[typepos][weapontype].length>0){
            this.equipStack(EquipmentSlot.MAINHAND,SkavenEquipment[typepos][weapontype][0]);
            if(SkavenEquipment[typepos][weapontype].length>1){
                this.equipStack(EquipmentSlot.OFFHAND,SkavenEquipment[typepos][weapontype][1]);
            }
        }
    }

    protected void updateEnchantments(LocalDifficulty difficulty) {
        float f = difficulty.getClampedLocalDifficulty();
        if(this.random.nextFloat() > 0.5F) this.enchantMainHandItem(f);
    }

    public void writeCustomDataToNbt(NbtCompound compoundNBT) {
        super.writeCustomDataToNbt(compoundNBT);
        compoundNBT.putString("SkavenType", this.getSkaventype());

    }

    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        setSkavenType(compound.getString("SkavenType"));
    }

    public void onTrackedDataSet(TrackedData<?> key) {
        if (SkavenType.equals(key)) {
            this.updateSkavenSize();
        }
        super.onTrackedDataSet(key);
    }

    public static boolean checkSkavenSpawnRules(EntityType<SkavenEntity> entityType, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (world.getDifficulty() != Difficulty.PEACEFUL && isSpawnDark(world, pos, random) && canMobSpawn(entityType, world, spawnReason, pos, random) && !spawnReason.equals(SpawnReason.REINFORCEMENT)) {
            return Objects.equals(world.getBiomeName(pos), Optional.of(BiomeKeys.MOUNTAINS)) || pos.getY() < 60;
        }
        return false;
    }

    protected float getDropChance(EquipmentSlot slot) {
        float f;
        switch (slot.getType()) {
            case HAND -> {
                f = this.handDropChances[slot.getEntitySlotId()];
                if (this.getMainHandStack().getItem() instanceof IReloadItem) f = 2;
            }
            case ARMOR -> f = this.armorDropChances[slot.getIndex()];
            default -> f = 0.0F;
        }

        return f;
    }

    @Override
    public boolean isBlocking() {
        if(this.getOffHandStack().getItem() instanceof ShieldItem && getTarget()!=null && this.random.nextFloat()<0.25){
            playSound(SoundEvents.ITEM_SHIELD_BLOCK,1,1);
            return true;
        }
        else return false;
    }
}
