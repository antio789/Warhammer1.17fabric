package warhammermod.Entities.Living;



import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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

import static warhammermod.utils.reference.*;


import java.util.*;


public class SkavenEntity extends Monster implements RangedAttackMob {

    //useless but to remove annoying error message

    /**
     * Goals
     */
    private final RangedSkavenAttackGoal<SkavenEntity> bowGoal = new RangedSkavenAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final RangedAttackGoal aiRangedPotion = new RangedAttackGoal(this,1,55,10F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false) {
        public void stop() {
            super.stop();
            setAggressive(false);
        }

        public void start() {
            super.start();
            setAggressive(true);
        }
    };

    public void reassessWeaponGoal() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            this.goalSelector.removeGoal(this.aiRangedPotion);
            ItemStack item = this.getMainHandItem();
            int i = firerate.get(getSkavenTypePosition());
            if (item.getItem() instanceof WarpgunTemplate || item.getItem() instanceof RatlingGun || item.getItem() instanceof SlingTemplate) {
                if (this.level.getDifficulty() != Difficulty.HARD) {
                    i *= 1.5;
                }

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else if(getSkaventype().equals(globadier)){
                this.goalSelector.addGoal(4,aiRangedPotion);
            }

            else{
                this.goalSelector.addGoal(4, this.meleeGoal);
            }

        }
    }

    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, SkavenEntity.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));

    }
    /**
     * handle all of skaventypes
     *
     */
    public static final EntityDataAccessor<String> SkavenType = SynchedEntityData.<String>defineId(SkavenEntity.class, EntityDataSerializers.STRING);
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
        return entityData.get(SkavenType);
    }
    public int getSkavenTypePosition(){
        int typepos = Math.max(Types.indexOf(entityData.get(SkavenType)),0);
        return typepos;
    }
    public float getSkavenSize(){
        int typepos = Math.max(Types.indexOf(getSkaventype()),0);
        return SkavenSize.get(typepos);
    }
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(getSkavenSize());
    }

    private void updateSkavenSize(){
        this.refreshDimensions();
        handleAttributes();
    }

    private void setSkavenType(String type){
        this.entityData.set(SkavenType,type);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SkavenType, Types.get(0));
    }


    private boolean fixgame; //skaven task dont work without was for 1.15 to test if still necessary

    public SkavenEntity(EntityType<? extends SkavenEntity> p_i48555_1_, Level p_i48555_2_) {
        super(p_i48555_1_, p_i48555_2_);
        this.reassessWeaponGoal();
        fixgame=true;
    }
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(world, difficultyIn, reason, spawnDataIn, dataTag);
        setSkavenType(getrandomtype());
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        this.reassessWeaponGoal();
        this.handleAttributes();
        return spawnDataIn;
    }


    public void aiStep() {
        if(fixgame){
            reassessWeaponGoal();
            this.handleAttributes();
            fixgame=false;
        }
        super.aiStep();
    }

        /**
         * attributes
         */
    public void updateSkavenAttributes(double speed,double health,double armor,double AD){
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        this.getAttribute(Attributes.ARMOR).setBaseValue(armor);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(AD);
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

    public static AttributeSupplier.Builder createMonsterAttributes()  {
        return Monster.createMonsterAttributes();
    }
    /**
     * behaviour
     */
    public boolean isAffectedByPotions() {
        return !getSkaventype().equals(globadier);
    }

    public void performRangedAttack(LivingEntity target, float p_82196_2_) {
        Item item = this.getMainHandItem().getItem();
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


        Vec3 vec3d = target.getDeltaMovement();
        double d0 = target.getX() + vec3d.x - this.getX();
        double d1 = target.getEyeY() - (double)1.1F - this.getY();
        double d2 = target.getZ() + vec3d.z - this.getZ();
        double f = Math.sqrt(d0 * d0 + d2 * d2);
        Potion potion = random.nextBoolean() ? Potions.HARMING : Potions.POISON;

        ThrownPotion potionentity = new ThrownPotion(this.level, this);
        potionentity.setItem(PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), potion));
        potionentity.setXRot(potionentity.getXRot() - -20.0F);
        potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
        this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        this.level.addFreshEntity(potionentity);

    }
    private void attackEntitywithrifle(LivingEntity target, int damage, float inaccuracy){
        WarpBulletEntity bullet = new WarpBulletEntity(this,level, damage);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS,this);
        if (j > 0) {
            bullet.setpowerDamage(j);
        }
        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, this) + 1;
        if (k > 0) {
            bullet.setknockbacklevel(k);
        }
        double d0 = target.getX()  - this.getX();
        double d1 = target.getY(0.3333333333333333D) - bullet.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double) Math.sqrt(d0 * d0 + d2 * d2);
        bullet.shoot(d0,d1+d3*0.06,d2,3,(float)(inaccuracy - this.level.getDifficulty().getId() * 4));//inac14
        this.playSound( SoundEvents.GENERIC_EXPLODE, 1.0F, 2F / (random.nextFloat() * 0.4F + 1.2F) + 0.5F);
        this.level.addFreshEntity(bullet);

    }
    private void attackEntitywithstone(LivingEntity target, float inaccuracy){
        StoneEntity stone = new StoneEntity(this,level,3);

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, this);
        if (j > 0) {
            stone.setTotaldamage(stone.projectiledamage + (float)j * 0.5F + 0.5F);
        }
        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, this) + 1;
        if (k > 0) {
            stone.setknockbacklevel(k);
        }


        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, this) > 0) {
            stone.setSecondsOnFire(100);
        }
        double d0 = target.getX()  - this.getX();
        double d1 = target.getY(0.3333333333333333D) - stone.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double) Math.sqrt(d0 * d0 + d2 * d2);
        stone.shoot(d0,d1+d3*0.2,d2,1.3F,(float)(inaccuracy - this.level.getDifficulty().getId() * 4));
        this.playSound( SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F));
        this.level.addFreshEntity(stone);

    }

    public boolean hurt(DamageSource source, float amount){
        if (!super.hurt(source, amount)) {
            return false;
        } else if (!(this.level instanceof ServerLevel)) {
            return false;
        } else {
            ServerLevel serverworld = (ServerLevel)this.level;
            LivingEntity livingentity = this.getTarget();
            if (livingentity == null && source.getEntity() instanceof LivingEntity) {
                livingentity = (LivingEntity)source.getEntity();
            }




            if (livingentity != null && this.level.getDifficulty() == Difficulty.HARD && (double)this.random.nextFloat() < reinforcementchance.get(getSkavenTypePosition())/2 && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                SkavenEntity skavenEntity = Entityinit.SKAVEN.create(this.level);
                int i = Mth.floor(this.getX());
                int j = Mth.floor(this.getY());
                int k = Mth.floor(this.getZ());

                for(int l = 0; l < 50; ++l) {
                    int i1 = i + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                    int j1 = j + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                    int k1 = k + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                    BlockPos blockpos = new BlockPos(i1, j1, k1);
                    EntityType<?> entitytype = skavenEntity.getType();
                    SpawnPlacements.Type placementType = SpawnPlacements.getPlacementType(entitytype);
                    if (NaturalSpawner.isSpawnPositionOk(placementType, this.level, blockpos, entitytype) && SpawnPlacements.checkSpawnRules(entitytype, serverworld, MobSpawnType.REINFORCEMENT, blockpos, this.level.random)) {
                        skavenEntity.setPos((double)i1, (double)j1, (double)k1);
                        if (!this.level.hasNearbyAlivePlayer((double)i1, (double)j1, (double)k1, 7.0D) && this.level.isUnobstructed(skavenEntity) && this.level.noCollision(skavenEntity) && !this.level.containsAnyLiquid(skavenEntity.getBoundingBox())) {

                            skavenEntity.setTarget(livingentity);
                            skavenEntity.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(skavenEntity.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
                            serverworld.addFreshEntityWithPassengers(skavenEntity);
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
        return SoundEvents.ZOMBIE_STEP;
    }
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
    public float getVoicePitch()
    {
        return 0.4F +this.random.nextFloat()*0.5F;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty)
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
            this.setItemSlot(EquipmentSlot.MAINHAND,SkavenEquipment[typepos][weapontype][0]);
            if(SkavenEquipment[typepos][weapontype].length>1){
                this.setItemSlot(EquipmentSlot.OFFHAND,SkavenEquipment[typepos][weapontype][1]);
            }
        }
    }

    protected void populateDefaultEquipmentEnchantments(DifficultyInstance difficulty) {
        float f = difficulty.getSpecialMultiplier();
        if(this.random.nextFloat() > 0.5F) this.enchantSpawnedWeapon(f);
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("SkavenType", this.getSkaventype());

    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setSkavenType(compound.getString("SkavenType"));
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (SkavenType.equals(key)) {
            this.updateSkavenSize();
        }
        super.onSyncedDataUpdated(key);
    }

    public static boolean checkSkavenSpawnRules(EntityType<SkavenEntity> entityType, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        if (world.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(world, pos, random) && checkMobSpawnRules(entityType, world, spawnReason, pos, random) && !spawnReason.equals(MobSpawnType.REINFORCEMENT)) {
            return Objects.equals(world.getBiomeName(pos), Optional.of(Biomes.MOUNTAINS)) || pos.getY() < 60;
        }
        return false;
    }

    protected float getEquipmentDropChance(EquipmentSlot slot) {
        float f;
        switch (slot.getType()) {
            case HAND -> {
                f = this.handDropChances[slot.getIndex()];
                if (this.getMainHandItem().getItem() instanceof IReloadItem) f = 2;
            }
            case ARMOR -> f = this.armorDropChances[slot.getIndex()];
            default -> f = 0.0F;
        }

        return f;
    }

    @Override
    public boolean isBlocking() {
        if(this.getOffhandItem().getItem() instanceof ShieldItem && getTarget()!=null && this.random.nextFloat()<0.25){
            playSound(SoundEvents.SHIELD_BLOCK,1,1);
            return true;
        }
        else return false;
    }
}
