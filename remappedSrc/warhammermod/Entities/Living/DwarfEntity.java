package warhammermod.Entities.Living;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.sensor.GolemLastSeenSensor;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.*;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Nullable;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfession;
import warhammermod.Entities.Living.AImanager.Data.DwarfTrades;
import warhammermod.Entities.Living.AImanager.DwarfCombattasks;
import warhammermod.Entities.Living.AImanager.DwarfVillagerTasks;
import warhammermod.Items.ItemsInit;
import warhammermod.utils.Registry.Entityinit;
import warhammermod.utils.Registry.WHRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class DwarfEntity extends MerchantEntity implements InteractionObserver, VillagerDataContainer {
    //useless but to remove annoying error message
    public DwarfEntity(net.minecraft.world.World world){
        super(Entityinit.DWARF,world);
    }
    //easier to work with than default mc way thats also private
    private static final TrackedData<Integer> Profession = DataTracker.registerData(DwarfEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> Level = DataTracker.registerData(DwarfEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private Boolean hasspawnedlord=false;


    public static final Map<Item, Integer> FOOD_POINTS = ImmutableMap.of(ItemsInit.BEER,8, Items.BREAD, 4, Items.POTATO, 1, Items.CARROT, 1, Items.BEETROOT, 1);
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(ItemsInit.BEER,Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);

    private int updateMerchantTimer;
    private boolean increaseProfessionLevelOnUpdate;
    @Nullable
    private PlayerEntity lastTradedPlayer;
    private byte foodLevel;
    private final VillagerGossips gossips = new VillagerGossips();
    private long lastGossipTime;
    private long lastGossipDecayTime;
    private int villagerXp;
    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    private long lastRestockCheckDayTime;
    private boolean assignProfessionWhenSpawned;
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.ATTACK_COOLING_DOWN,MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY,MemoryModuleType.ATTACK_TARGET);
    private static final ImmutableList<SensorType<? extends Sensor<? super DwarfEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, WHRegistry.Hostiles, WHRegistry.VISIBLE_VILLAGER_BABIES, WHRegistry.SECONDARY_POIS, WHRegistry.Lord_LastSeen);
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<DwarfEntity, PointOfInterestType>> POI_MEMORIES = ImmutableMap.of(MemoryModuleType.HOME, (dwarfEntity, pointOfInterestType) -> {
        return pointOfInterestType == PointOfInterestType.HOME;
    }, MemoryModuleType.JOB_SITE, (dwarfEntity, pointOfInterestType) -> {
        return dwarfEntity.getProfession().getPointOfInterest() == pointOfInterestType;
    }, MemoryModuleType.POTENTIAL_JOB_SITE, (dwarfEntity, pointOfInterestType) -> {
        return PointOfInterestType.ALL_JOBS.test(pointOfInterestType);
    }, MemoryModuleType.MEETING_POINT, (dwarfEntity, pointOfInterestType) -> {
        return pointOfInterestType == PointOfInterestType.MEETING;
    });
    /**
     * initialization
     */

    public DwarfEntity(EntityType<? extends DwarfEntity> entityType, World world) {
        super(entityType, world);
        ((MobNavigation)this.getNavigation()).setCanPathThroughDoors(true);
        this.getNavigation().setCanSwim(true);
        this.setCanPickUpLoot(true);
        this.setVillagerDataBase();
        this.setEquipment(this.getProfession());
    }



    /**
     * all custom profession
     */
    @Deprecated
    public void setVillagerData(VillagerData p_213753_1_) {
    }
    @Deprecated
    public VillagerData getVillagerData() {
        return null;
    }

    public int getProfessionID()
    {
        return Math.max(this.dataTracker.get(Profession), 0);//0farmer,1miner,2Engineer,3builder,4slayer,5lord,6 warrior
    }

    public void setVillagerData() {
        this.setVillagerData(getProfessionID(), getProfessionLevel());
    }

    public void setVillagerDataBase() {
        this.setVillagerData(DwarfProfession.Warrior.getID(),1);
    }

    public void setVillagerData(int prof,int level) {

        if (getProfession().getID() != prof) {
            this.offers = null;
        }
        if(dataTracker.get(Profession)!=DwarfProfession.Lord.getID()){
            this.dataTracker.set(Profession,prof);}
        this.dataTracker.set(Level,level);
    }

    public void registerVillagerData() {

        this.dataTracker.startTracking(Profession, DwarfProfession.Warrior.getID());
        this.dataTracker.startTracking(Level,1);
    }

    public void setVillagerlevel(int level) {
        setVillagerData(getProfessionID(),level);
    }

    public void setVillagerProfession(DwarfProfession profession){
        setVillagerData(profession.getID(), getProfessionLevel());
        this.setEquipment(profession);
        this.updateAttributes(profession);
    }


    public DwarfProfession getProfession(){
        for(DwarfProfession prof : DwarfProfession.Profession){
            if (prof.getID()==getProfessionID()){
                return prof;
            }
        }
        return DwarfProfession.Warrior;
    }


    public int getProfessionLevel(){
        return Math.max(this.dataTracker.get(Level),1);
    }

    public void setProfession(int professionId)
    {
        this.dataTracker.set(Profession, professionId);
    }


    private final int[] xpforlevel = new int[]{0, 10, 70, 150, 250};

    @Environment(EnvType.CLIENT)
    public int getlevelfromxpclient(int p_221133_0_) {
        return haslevel(p_221133_0_) ? xpforlevel[p_221133_0_ - 1] : 0;
    }

    public int getlevelfromxp(int p_221127_0_) {
        return haslevel(p_221127_0_) ? xpforlevel[p_221127_0_] : 0;
    }

    public boolean haslevel(int p_221128_0_) {
        return p_221128_0_ >= 1 && p_221128_0_ < 5;
    }

    /**
     * all related to combat capabilities
     */
    protected void setEquipment(DwarfProfession prof)
    {
        if(hasStackEquipped(EquipmentSlot.MAINHAND)){
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        if(hasStackEquipped(EquipmentSlot.OFFHAND)){
            this.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }
        this.equipStack(EquipmentSlot.MAINHAND,new ItemStack(prof.getItemtoslot(0)));
        this.equipStack(EquipmentSlot.OFFHAND,new ItemStack(prof.getItemtoslot(1)));
    }

    protected void updateAttributes(DwarfProfession prof){
        if(prof.equals(DwarfProfession.Slayer)){
            this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(0);
        }
    }

    @Environment(EnvType.CLIENT)
    public IllagerEntity.State getArmPose() {
        if (this.isAttacking()){
            return IllagerEntity.State.ATTACKING;
        } else {
            return IllagerEntity.State.CROSSED;
        }
    }
    @Override
    public boolean isBlocking() {
        if(this.getOffHandStack().getItem() instanceof ShieldItem && this.isAttacking() && this.random.nextFloat()<0.33){
            playSound(SoundEvents.ITEM_SHIELD_BLOCK,1,1);
            return true;
        }
        else return false;
    }

    /**
     * where modifications are needed
     */
    public float getSoundPitch()
    {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.4F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F;
    }

    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setVillagerData(compound.getInt("profession"),compound.getInt("level"));
        this.hasspawnedlord=compound.getBoolean("canspawnlord");

        if (compound.contains("Offers", 10)) {
            this.offers = new TradeOfferList(compound.getCompound("Offers"));
        }

        if (compound.contains("FoodLevel", 1)) {
            this.foodLevel = compound.getByte("FoodLevel");
        }

        NbtList listnbt = compound.getList("Gossips", 10);
        this.gossips.deserialize(new Dynamic<>(NbtOps.INSTANCE, listnbt));
        if (compound.contains("Xp", 3)) {
            this.villagerXp = compound.getInt("Xp");
        }

        this.lastRestockGameTime = compound.getLong("LastRestock");
        this.lastGossipDecayTime = compound.getLong("LastGossipDecay");
        this.setCanPickUpLoot(true);
        if (this.world instanceof ServerWorld) {
            this.refreshBrain((ServerWorld)this.world);
        }

        this.numberOfRestocksToday = compound.getInt("RestocksToday");
        if (compound.contains("AssignProfessionWhenSpawned")) {
            this.assignProfessionWhenSpawned = compound.getBoolean("AssignProfessionWhenSpawned");
        }

    }

    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);

        compound.putInt("level", this.getProfessionLevel());
        compound.putInt("profession",this.getProfessionID());

        compound.putByte("FoodLevel", this.foodLevel);
        compound.put("Gossips", this.gossips.serialize(NbtOps.INSTANCE).getValue());
        compound.putInt("Xp", this.villagerXp);
        compound.putLong("LastRestock", this.lastRestockGameTime);
        compound.putLong("LastGossipDecay", this.lastGossipDecayTime);
        compound.putInt("RestocksToday", this.numberOfRestocksToday);
        if (this.assignProfessionWhenSpawned) {
            compound.putBoolean("AssignProfessionWhenSpawned", true);
        }

    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(Profession,
                DwarfProfession.Warrior.getID());
        this.dataTracker.startTracking(Level,1);
    }

    public DwarfEntity createChild(ServerWorld world, PassiveEntity entity) {
        double d0 = this.random.nextDouble();

        DwarfEntity dwarfEntity = new DwarfEntity(Entityinit.DWARF, world);
        dwarfEntity.initialize(world, world.getLocalDifficulty(dwarfEntity.getBlockPos()), SpawnReason.BREEDING, (EntityData)null, (NbtCompound)null);
        return dwarfEntity;
    }

    private void registerBrainGoals(Brain<DwarfEntity> p_213744_1_) {
        DwarfProfession villagerprofession = this.getProfession();
        if (this.isBaby()) {
            p_213744_1_.setSchedule(Schedule.VILLAGER_BABY);
            p_213744_1_.setTaskList(Activity.PLAY, DwarfVillagerTasks.getPlayPackage(0.5F));
            p_213744_1_.setTaskList(Activity.PANIC, DwarfVillagerTasks.getPanicPackage(villagerprofession, 0.5F));
        } else {
            p_213744_1_.setSchedule(Schedule.VILLAGER_DEFAULT);

            p_213744_1_.setTaskList(Activity.PANIC, DwarfCombattasks.getAttackPackage(villagerprofession, 0.5F));
            p_213744_1_.setTaskList(Activity.WORK, DwarfVillagerTasks.getWorkPackage(villagerprofession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT)));
        }

        p_213744_1_.setTaskList(Activity.CORE, DwarfVillagerTasks.getCorePackage(villagerprofession, 0.5F));
        p_213744_1_.setTaskList(Activity.MEET, DwarfVillagerTasks.getMeetPackage(villagerprofession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleState.VALUE_PRESENT)));
        p_213744_1_.setTaskList(Activity.REST, DwarfVillagerTasks.getRestPackage(villagerprofession, 0.5F));
        p_213744_1_.setTaskList(Activity.IDLE, DwarfVillagerTasks.getIdlePackage(this,villagerprofession, 0.5F));
        p_213744_1_.setTaskList(Activity.PRE_RAID, DwarfVillagerTasks.getPreRaidPackage(villagerprofession, 0.5F));
        p_213744_1_.setTaskList(Activity.RAID, DwarfVillagerTasks.getRaidPackage(villagerprofession, 0.5F));
        p_213744_1_.setTaskList(Activity.HIDE, DwarfVillagerTasks.getHidePackage(villagerprofession, 0.5F));
        p_213744_1_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_213744_1_.setDefaultActivity(Activity.IDLE);
        p_213744_1_.doExclusively(Activity.IDLE);
        p_213744_1_.refreshActivities(this.world.getTimeOfDay(), this.world.getTime());
    }

    public void spawnGolemIfNeeded(ServerWorld p_242367_1_, long p_242367_2_, int p_242367_4_) {
        if (this.wantsToSpawnGolem(p_242367_2_)) {
            Box axisalignedbb = this.getBoundingBox().expand(10.0D, 10.0D, 10.0D);
            List<DwarfEntity> list = p_242367_1_.getNonSpectatingEntities(DwarfEntity.class, axisalignedbb);
            List<DwarfEntity> list1 = list.stream().filter((p_226554_2_) -> {
                return p_226554_2_.wantsToSpawnGolem(p_242367_2_);
            }).limit(5L).collect(Collectors.toList());
            if (list1.size() >= p_242367_4_) {
                DwarfEntity lord = this.trySpawnGolem(p_242367_1_);
                if (lord != null) {
                    lord.setVillagerProfession(DwarfProfession.Lord);
                    list.forEach(GolemLastSeenSensor::rememberIronGolem);
                }
            }
        }
    }

    public boolean wantsToSpawnGolem(long p_223350_1_) {
        if (!this.golemSpawnConditionsMet(this.world.getTime())) {
            return false;
        } else {
            return !this.brain.hasMemoryModule(MemoryModuleType.GOLEM_DETECTED_RECENTLY);
        }
    }

    @Nullable
    private DwarfEntity trySpawnGolem(ServerWorld p_213759_1_) {
        BlockPos blockpos = this.getBlockPos();

        for(int i = 0; i < 10; ++i) {
            double d0 = (double)(p_213759_1_.random.nextInt(16) - 8);
            double d1 = (double)(p_213759_1_.random.nextInt(16) - 8);
            BlockPos blockpos1 = this.findSpawnPositionForGolemInColumn(blockpos, d0, d1);
            if (blockpos1 != null) {
                DwarfEntity lord = Entityinit.DWARF.create(p_213759_1_, null, null, null, blockpos1, SpawnReason.MOB_SUMMONED, false, false);
                if (lord != null) {
                    if (lord.canSpawn(p_213759_1_, SpawnReason.MOB_SUMMONED) && lord.canSpawn(p_213759_1_)) {
                        p_213759_1_.spawnEntityAndPassengers(lord);
                        return lord;
                    }

                    lord.discard();
                }
            }
        }

        return null;
    }

    private boolean golemSpawnConditionsMet(long gametime) {
        Optional<Long> optional = this.brain.getOptionalRegisteredMemory(MemoryModuleType.LAST_SLEPT);
        if (optional.isPresent() && haslevelforlord()) {
            return gametime - optional.get() < 24000L;
        } else {
            return false;
        }
    }

    private boolean haslevelforlord() {
        return this.getProfessionLevel() == 5;
    }

    /**
     * copy from villager includes everything that doesnt needs to be modified
     */

    public Brain<DwarfEntity> getBrain() {
        return (Brain<DwarfEntity>)super.getBrain();
    }

    protected Brain.Profile<DwarfEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected Brain<?> deserializeBrain(Dynamic<?> p_213364_1_) {
        Brain<DwarfEntity> brain = this.createBrainProfile().deserialize(p_213364_1_);
        this.registerBrainGoals(brain);
        return brain;
    }

    public void refreshBrain(ServerWorld p_213770_1_) {
        Brain<DwarfEntity> brain = this.getBrain();
        brain.stopAllTasks(p_213770_1_, this);
        this.brain = brain.copy();
        this.registerBrainGoals(this.getBrain());
    }



    protected void onGrowUp() {
        super.onGrowUp();
        if (this.world instanceof ServerWorld) {
            this.refreshBrain((ServerWorld)this.world);
        }

    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);
    }

    public boolean assignProfessionWhenSpawned() {
        return this.assignProfessionWhenSpawned;
    }

    protected void mobTick() {
        DwarfCombattasks.updateActivity(this);
        this.world.getProfiler().push("villagerBrain");
        this.getBrain().tick((ServerWorld)this.world, this);
        this.world.getProfiler().pop();
        if (this.assignProfessionWhenSpawned) {
            this.assignProfessionWhenSpawned = false;
        }

        if (!this.hasCustomer() && this.updateMerchantTimer > 0) {
            --this.updateMerchantTimer;
            if (this.updateMerchantTimer <= 0) {
                if (this.increaseProfessionLevelOnUpdate) {
                    this.increaseMerchantCareer();
                    this.increaseProfessionLevelOnUpdate = false;
                }

                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0));
            }
        }

        if (this.lastTradedPlayer != null && this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).handleInteraction(EntityInteraction.TRADE, this.lastTradedPlayer, this);
            this.world.sendEntityStatus(this, (byte)14);
            this.lastTradedPlayer = null;
        }

        if (!this.isAiDisabled() && this.random.nextInt(100) == 0) {
            Raid raid = ((ServerWorld)this.world).getRaidAt(this.getBlockPos());
            if (raid != null && raid.isActive() && !raid.isFinished()) {
                this.world.sendEntityStatus(this, (byte)42);
            }
        }

        if (this.getProfession() == DwarfProfession.Warrior && this.hasCustomer()) {
            this.resetCustomer();
        }

        super.mobTick();
    }

    public void tick() {
        super.tick();
        if (this.getHeadRollingTimeLeft() > 0) {
            this.setHeadRollingTimeLeft(this.getHeadRollingTimeLeft() - 1);
        }

        this.maybeDecayGossip();
    }

    public ActionResult interactMob(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getStackInHand(p_230254_2_);
        if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.hasCustomer() && !this.isSleeping() && !p_230254_1_.shouldCancelInteraction()) {
            if (this.isBaby()) {
                this.setUnhappy();
                return ActionResult.success(this.world.isClient);
            } else {
                boolean flag = this.getOffers().isEmpty() && this.isAttacking();
                if (p_230254_2_ == Hand.MAIN_HAND) {
                    if (flag && !this.world.isClient) {
                        this.setUnhappy();
                    }

                    p_230254_1_.incrementStat(Stats.TALKED_TO_VILLAGER);
                }

                if (flag) {
                    return ActionResult.success(this.world.isClient);
                } else {
                    if (!this.world.isClient && !this.offers.isEmpty()) {
                        this.startTrading(p_230254_1_);
                    }

                    return ActionResult.success(this.world.isClient);
                }
            }
        } else {
            return super.interactMob(p_230254_1_, p_230254_2_);
        }
    }

    private void setUnhappy() {
        this.setHeadRollingTimeLeft(40);
        if (!this.world.isClient()) {
            this.playSound(SoundEvents.ENTITY_VILLAGER_NO, this.getSoundVolume(), this.getSoundPitch());
        }

    }

    private void startTrading(PlayerEntity p_213740_1_) {
        this.updateSpecialPrices(p_213740_1_);
        this.setCustomer(p_213740_1_);
        this.sendOffers(p_213740_1_, this.getDisplayName(), this.getProfessionLevel());
    }

    public void setCustomer(@Nullable PlayerEntity p_70932_1_) {
        boolean flag = this.getCustomer() != null && p_70932_1_ == null;
        super.setCustomer(p_70932_1_);
        if (flag) {
            this.resetCustomer();
        }

    }

    protected void resetCustomer() {
        super.resetCustomer();
        this.resetSpecialPrices();
    }

    private void resetSpecialPrices() {
        for(TradeOffer merchantoffer : this.getOffers()) {
            merchantoffer.clearSpecialPrice();
        }

    }

    public boolean canRefreshTrades() {
        return true;
    }

    public void restock() {
        this.updateDemand();

        for(TradeOffer merchantoffer : this.getOffers()) {
            merchantoffer.resetUses();
        }

        this.lastRestockGameTime = this.world.getTime();
        ++this.numberOfRestocksToday;
    }

    private boolean needsToRestock() {
        for(TradeOffer merchantoffer : this.getOffers()) {
            if (merchantoffer.hasBeenUsed()) {
                return true;
            }
        }

        return false;
    }

    private boolean allowedToRestock() {
        return this.numberOfRestocksToday == 0 || this.numberOfRestocksToday < 2 && this.world.getTime() > this.lastRestockGameTime + 2400L;
    }

    public boolean shouldRestock() {
        long i = this.lastRestockGameTime + 12000L;
        long j = this.world.getTime();
        boolean flag = j > i;
        long k = this.world.getTimeOfDay();
        if (this.lastRestockCheckDayTime > 0L) {
            long l = this.lastRestockCheckDayTime / 24000L;
            long i1 = k / 24000L;
            flag |= i1 > l;
        }

        this.lastRestockCheckDayTime = k;
        if (flag) {
            this.lastRestockGameTime = j;
            this.resetNumberOfRestocks();
        }

        return this.allowedToRestock() && this.needsToRestock();
    }

    private void catchUpDemand() {
        int i = 2 - this.numberOfRestocksToday;
        if (i > 0) {
            for(TradeOffer merchantoffer : this.getOffers()) {
                merchantoffer.resetUses();
            }
        }

        for(int j = 0; j < i; ++j) {
            this.updateDemand();
        }

    }

    private void updateDemand() {
        for(TradeOffer merchantoffer : this.getOffers()) {
            merchantoffer.updateDemandBonus();
        }

    }

    private void updateSpecialPrices(PlayerEntity p_213762_1_) {
        int i = this.getPlayerReputation(p_213762_1_);
        if (i != 0) {
            for(TradeOffer merchantoffer : this.getOffers()) {
                merchantoffer.increaseSpecialPrice(-MathHelper.floor((float)i * merchantoffer.getPriceMultiplier()));
            }
        }

        if (p_213762_1_.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE)) {
            StatusEffectInstance effectinstance = p_213762_1_.getStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);
            int k = effectinstance.getAmplifier();

            for(TradeOffer merchantoffer1 : this.getOffers()) {
                double d0 = 0.3D + 0.0625D * (double)k;
                int j = (int)Math.floor(d0 * (double)merchantoffer1.getOriginalFirstBuyItem().getCount());
                merchantoffer1.increaseSpecialPrice(-Math.max(j, 1));
            }
        }

    }

    public boolean canImmediatelyDespawn(double p_213397_1_) {
        return false;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return null;
        } else {
            return this.hasCustomer() ? SoundEvents.ENTITY_VILLAGER_TRADE : SoundEvents.ENTITY_VILLAGER_AMBIENT;
        }
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    public void playWorkSound() {
        SoundEvent soundevent = this.getProfession().getWorksound();
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }

    }

    protected void afterUsing(TradeOffer p_213713_1_) {
        int i = 3 + this.random.nextInt(4);
        this.villagerXp += p_213713_1_.getMerchantExperience();
        this.lastTradedPlayer = this.getCustomer();
        if (this.shouldIncreaseLevel()) {
            this.updateMerchantTimer = 40;
            this.increaseProfessionLevelOnUpdate = true;
            i += 5;
        }

        if (p_213713_1_.shouldRewardPlayerExperience()) {
            this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.getX(), this.getY() + 0.5D, this.getZ(), i));
        }

    }

    public void setAttacker(@Nullable LivingEntity p_70604_1_) {
        if (p_70604_1_ != null && this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).handleInteraction(EntityInteraction.VILLAGER_HURT, p_70604_1_, this);
            if (this.isAlive() && p_70604_1_ instanceof PlayerEntity) {
                this.world.sendEntityStatus(this, (byte)13);
            }
        }

        super.setAttacker(p_70604_1_);
    }

    public void onDeath(DamageSource p_70645_1_) {
        LOGGER.info("Villager {} died, message: '{}'", this, p_70645_1_.getDeathMessage(this).getString());
        Entity entity = p_70645_1_.getAttacker();
        if (entity != null) {
            this.tellWitnessesThatIWasMurdered(entity);
        }

        this.releaseAllPois();
        super.onDeath(p_70645_1_);
    }

    private void releaseAllPois() {
        this.releasePoi(MemoryModuleType.HOME);
        this.releasePoi(MemoryModuleType.JOB_SITE);
        this.releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
        this.releasePoi(MemoryModuleType.MEETING_POINT);
    }

    private void tellWitnessesThatIWasMurdered(Entity p_223361_1_) {
        if (this.world instanceof ServerWorld) {
            Optional<List<LivingEntity>> optional = this.brain.getOptionalRegisteredMemory(MemoryModuleType.VISIBLE_MOBS);
            if (optional.isPresent()) {
                ServerWorld serverworld = (ServerWorld)this.world;
                optional.get().stream().filter((p_223349_0_) -> {
                    return p_223349_0_ instanceof InteractionObserver;
                }).forEach((p_223342_2_) -> {
                    serverworld.handleInteraction(EntityInteraction.VILLAGER_KILLED, p_223361_1_, (InteractionObserver)p_223342_2_);
                });
            }
        }
    }

    public void releasePoi(MemoryModuleType<GlobalPos> p_213742_1_) {
        if (this.world instanceof ServerWorld) {
            MinecraftServer minecraftserver = ((ServerWorld)this.world).getServer();
            this.brain.getOptionalRegisteredMemory(p_213742_1_).ifPresent((p_213752_3_) -> {
                ServerWorld serverworld = minecraftserver.getWorld(p_213752_3_.getDimension());
                if (serverworld != null) {
                    PointOfInterestStorage pointofinterestmanager = serverworld.getPointOfInterestStorage();
                    Optional<PointOfInterestType> optional = pointofinterestmanager.getType(p_213752_3_.getPos());
                    BiPredicate<DwarfEntity, PointOfInterestType> bipredicate = POI_MEMORIES.get(p_213742_1_);
                    if (optional.isPresent() && bipredicate.test(this, optional.get())) {
                        pointofinterestmanager.releaseTicket(p_213752_3_.getPos());
                        DebugInfoSender.sendPointOfInterest(serverworld, p_213752_3_.getPos());
                    }

                }
            });
        }
    }

    public boolean isReadyToBreed() {
        return this.foodLevel + this.countFoodPointsInInventory() >= 12 && this.getBreedingAge() == 0;
    }


    private boolean hungry() {
        return this.foodLevel < 12;
    }

    private void eatUntilFull() {
        if (this.hungry() && this.countFoodPointsInInventory() != 0) {
            for(int i = 0; i < this.getInventory().size(); ++i) {
                ItemStack itemstack = this.getInventory().getStack(i);
                if (!itemstack.isEmpty()) {
                    Integer integer = FOOD_POINTS.get(itemstack.getItem());
                    if (integer != null) {
                        int j = itemstack.getCount();

                        for(int k = j; k > 0; --k) {
                            this.foodLevel = (byte)(this.foodLevel + integer);
                            this.getInventory().removeStack(i, 1);
                            if (!this.hungry()) {
                                return;
                            }
                        }
                    }
                }
            }

        }
    }

    public int getPlayerReputation(PlayerEntity p_223107_1_) {
        return this.gossips.getReputationFor(p_223107_1_.getUuid(), (p_223103_0_) -> {
            return true;
        });
    }

    private void digestFood(int p_213758_1_) {
        this.foodLevel = (byte)(this.foodLevel - p_213758_1_);
    }

    public void eatAndDigestFood() {
        this.eatUntilFull();
        this.digestFood(12);
    }

    public void setOffers(TradeOfferList p_213768_1_) {
        this.offers = p_213768_1_;
    }

    private boolean shouldIncreaseLevel() {
        int i = this.getProfessionLevel();
        return VillagerData.canLevelUp(i) && this.villagerXp >= VillagerData.getUpperLevelExperience(i);
    }

    private void increaseMerchantCareer() {
        this.setVillagerlevel(getProfessionLevel()+1);
        this.fillRecipes();
    }

    protected Text getDefaultName() {
        Identifier profName = this.getProfession().getRegistryName();
        return new TranslatableTextContent(this.getType().getTranslationKey() + '.' + (!"minecraft".equals(profName.getNamespace()) ? profName.getNamespace() + '.' : "") + profName.getPath());
    }


    public void handleStatus(byte p_70103_1_) {
        if (p_70103_1_ == 12) {
            this.produceParticles(ParticleTypes.HEART);
        } else if (p_70103_1_ == 13) {
            this.produceParticles(ParticleTypes.ANGRY_VILLAGER);
        } else if (p_70103_1_ == 14) {
            this.produceParticles(ParticleTypes.HAPPY_VILLAGER);
        } else if (p_70103_1_ == 42) {
            this.produceParticles(ParticleTypes.SPLASH);
        } else {
            super.handleStatus(p_70103_1_);
        }

    }

    @Nullable
    public EntityData initialize(ServerWorldAccess p_213386_1_, LocalDifficulty p_213386_2_, SpawnReason p_213386_3_, @Nullable EntityData p_213386_4_, @Nullable NbtCompound p_213386_5_) {
        if (p_213386_3_ == SpawnReason.BREEDING) {
            this.setVillagerProfession(DwarfProfession.Warrior);
        }

        if (p_213386_3_ == SpawnReason.COMMAND || p_213386_3_ == SpawnReason.SPAWN_EGG || p_213386_3_ == SpawnReason.SPAWNER || p_213386_3_ == SpawnReason.DISPENSER) {
            this.setVillagerDataBase();
        }

        if (p_213386_3_ == SpawnReason.STRUCTURE) {
            this.assignProfessionWhenSpawned = true;
        }

        return super.initialize(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }




    protected void loot(ItemEntity p_175445_1_) {
        ItemStack itemstack = p_175445_1_.getStack();
        if (this.canGather(itemstack)) {
            SimpleInventory inventory = this.getInventory();
            boolean flag = inventory.canInsert(itemstack);
            if (!flag) {
                return;
            }

            this.triggerItemPickedUpByEntityCriteria(p_175445_1_);
            this.sendPickup(p_175445_1_, itemstack.getCount());
            ItemStack itemstack1 = inventory.addStack(itemstack);
            if (itemstack1.isEmpty()) {
                p_175445_1_.discard();
            } else {
                itemstack.setCount(itemstack1.getCount());
            }
        }

    }

    public boolean canGather(ItemStack p_230293_1_) {
        Item item = p_230293_1_.getItem();
        return (WANTED_ITEMS.contains(item) || this.getProfession().getSpecificItems().contains(item)) && this.getInventory().canInsert(p_230293_1_);
    }

    public boolean hasExcessFood() {
        return this.countFoodPointsInInventory() >= 24;
    }

    public boolean wantsMoreFood() {
        return this.countFoodPointsInInventory() < 12;
    }

    private int countFoodPointsInInventory() {
        SimpleInventory inventory = this.getInventory();
        return FOOD_POINTS.entrySet().stream().mapToInt((p_226553_1_) -> {
            return inventory.count(p_226553_1_.getKey()) * p_226553_1_.getValue();
        }).sum();
    }

    public boolean hasFarmSeeds() {
        return this.getInventory().containsAny(ImmutableSet.of(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS));
    }

    protected void fillRecipes() {
        DwarfProfession prof = getProfession();
        Int2ObjectMap<TradeOffers.Factory[]> int2objectmap = DwarfTrades.TRADES.get(prof);
        if (int2objectmap != null && !int2objectmap.isEmpty()) {
            TradeOffers.Factory[] avillagertrades$itrade = int2objectmap.get(this.getProfessionLevel());
            if (avillagertrades$itrade != null) {
                TradeOfferList merchantoffers = this.getOffers();
                this.fillRecipesFromPool(merchantoffers, avillagertrades$itrade, 2);
            }
        }
    }

    protected void fillRecipesFromPool(TradeOfferList p_213717_1_, TradeOffers.Factory[] p_213717_2_, int p_213717_3_) {
        Set<Integer> set = Sets.newHashSet();
        if (p_213717_2_.length > p_213717_3_) {
            while(set.size() < p_213717_3_) {
                set.add(this.random.nextInt(p_213717_2_.length));
            }
        } else {
            for(int i = 0; i < p_213717_2_.length; ++i) {
                set.add(i);
            }
        }

        for(Integer integer : set) {
            TradeOffers.Factory villagertrades$itrade = p_213717_2_[integer];
            TradeOffer merchantoffer = villagertrades$itrade.create(this, this.random);
            if (merchantoffer != null) {
                p_213717_1_.add(merchantoffer);
            }
        }

    }

    public void gossip(ServerWorld p_242368_1_, DwarfEntity p_242368_2_, long p_242368_3_) {
        if ((p_242368_3_ < this.lastGossipTime || p_242368_3_ >= this.lastGossipTime + 1200L) && (p_242368_3_ < p_242368_2_.lastGossipTime || p_242368_3_ >= p_242368_2_.lastGossipTime + 1200L)) {
            this.gossips.shareGossipFrom(p_242368_2_.gossips, this.random, 10);
            this.lastGossipTime = p_242368_3_;
            p_242368_2_.lastGossipTime = p_242368_3_;
            this.spawnGolemIfNeeded(p_242368_1_, p_242368_3_, 5);
        }
    }

    private void maybeDecayGossip() {
        long i = this.world.getTime();
        if (this.lastGossipDecayTime == 0L) {
            this.lastGossipDecayTime = i;
        } else if (i >= this.lastGossipDecayTime + 24000L) {
            this.gossips.decay();
            this.lastGossipDecayTime = i;
        }
    }



    @Nullable
    private BlockPos findSpawnPositionForGolemInColumn(BlockPos p_241433_1_, double p_241433_2_, double p_241433_4_) {
        BlockPos blockpos = p_241433_1_.offset(p_241433_2_, 6.0D, p_241433_4_);
        BlockState blockstate = this.world.getBlockState(blockpos);

        for(int j = 6; j >= -6; --j) {
            BlockPos blockpos1 = blockpos;
            BlockState blockstate1 = blockstate;
            blockpos = blockpos.down();
            blockstate = this.world.getBlockState(blockpos);
            if ((blockstate1.isAir() || blockstate1.getMaterial().isLiquid()) && blockstate.getMaterial().isSolidBlocking()) {
                return blockpos1;
            }
        }

        return null;
    }

    public void onInteractionWith(EntityInteraction p_213739_1_, Entity p_213739_2_) {
        if (p_213739_1_ == EntityInteraction.ZOMBIE_VILLAGER_CURED) {
            this.gossips.startGossip(p_213739_2_.getUuid(), VillageGossipType.MAJOR_POSITIVE, 20);
            this.gossips.startGossip(p_213739_2_.getUuid(), VillageGossipType.MINOR_POSITIVE, 25);
        } else if (p_213739_1_ == EntityInteraction.TRADE) {
            this.gossips.startGossip(p_213739_2_.getUuid(), VillageGossipType.TRADING, 2);
        } else if (p_213739_1_ == EntityInteraction.VILLAGER_HURT) {
            this.gossips.startGossip(p_213739_2_.getUuid(), VillageGossipType.MINOR_NEGATIVE, 25);
        } else if (p_213739_1_ == EntityInteraction.VILLAGER_KILLED) {
            this.gossips.startGossip(p_213739_2_.getUuid(), VillageGossipType.MAJOR_NEGATIVE, 25);
        }

    }

    public int getExperience() {
        return this.villagerXp;
    }

    public void setVillagerXp(int p_213761_1_) {
        this.villagerXp = p_213761_1_;
    }

    private void resetNumberOfRestocks() {
        this.catchUpDemand();
        this.numberOfRestocksToday = 0;
    }

    public VillagerGossips getGossips() {
        return this.gossips;
    }

    public void setGossips(NbtElement p_223716_1_) {
        this.gossips.deserialize(new Dynamic<>(NbtOps.INSTANCE, p_223716_1_));
    }

    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }

    public void sleep(BlockPos p_213342_1_) {
        super.sleep(p_213342_1_);
        this.brain.remember(MemoryModuleType.LAST_SLEPT, this.world.getTime());
        this.brain.forget(MemoryModuleType.WALK_TARGET);
        this.brain.forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    public void wakeUp() {
        super.wakeUp();
        this.brain.remember(MemoryModuleType.LAST_WOKEN, this.world.getTime());
    }


    public static boolean canDwarfBreed(Object o) {
        if(o instanceof DwarfEntity){
            DwarfEntity entity = (DwarfEntity) o;
            return entity.foodLevel + entity.countFoodPointsInInventory() >= 12 && entity.getBreedingAge() == 0;
        }
        return false;
    }
}
