package warhammermod.Entities.Living;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.VillagerTaskListProvider;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.*;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.jetbrains.annotations.Nullable;
import warhammermod.Entities.Living.AImanager.Data.DwarfProfessionRecord;
import warhammermod.Entities.Living.AImanager.Data.DwarfTrades;
import warhammermod.Entities.Living.AImanager.DwarfCombattasks;
import warhammermod.Entities.Living.AImanager.DwarfVillagerTasks;
import warhammermod.Entities.Living.AImanager.Data.DwarfTasks.Sensor.LordLastSeenSensor;
import warhammermod.utils.Registry.ItemsInit;
import warhammermod.utils.Registry.Entityinit;
import warhammermod.utils.Registry.WHRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
//fix villagerdata.
public class DwarfEntity extends VillagerEntity  {

    @Nullable
    private PlayerEntity lastCustomer;
    private int levelUpTimer;
    private boolean levelingUp;

    public DwarfEntity(net.minecraft.world.World world){
        super(Entityinit.DWARF,world);
    }
    //not using villagerdata as it is not easily modifiable, replaced by a decomposed method.
    private static final TrackedData<Integer> Profession = DataTracker.registerData(DwarfEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> Level = DataTracker.registerData(DwarfEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private int restocksToday;
    private long lastRestockCheckTime;
    public static final Map<Item, Integer> ITEM_FOOD_VALUES = ImmutableMap.of(ItemsInit.Beer,8, Items.BREAD, 4, Items.POTATO, 1, Items.CARROT, 1, Items.BEETROOT, 1);
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(ItemsInit.Beer,Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<DwarfEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST =
            ImmutableMap.of(MemoryModuleType.HOME, (villager, registryEntry) -> registryEntry.matchesKey(PointOfInterestTypes.HOME),
                    MemoryModuleType.JOB_SITE, (villager, registryEntry) -> villager.getProfession().heldWorkstation().test(registryEntry),
                    MemoryModuleType.POTENTIAL_JOB_SITE, (villager, registryEntry) -> DwarfProfessionRecord.IS_ACQUIRABLE_JOB_SITE.test(registryEntry),
                    MemoryModuleType.MEETING_POINT, (villager, registryEntry) -> registryEntry.matchesKey(PointOfInterestTypes.MEETING));
    private long lastRestockTime;
    private int foodLevel;
    private int experience;

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(
            MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT,
            MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET,
            MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED,
            MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE,
            MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN,
            MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY,
            MemoryModuleType.ATTACK_COOLING_DOWN,MemoryModuleType.ATTACK_TARGET
            );
    private static final ImmutableList<SensorType<? extends Sensor<? super VillagerEntity>>> SENSORS = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY,
            SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.SECONDARY_POIS, SensorType.GOLEM_DETECTED);

    private static final ImmutableList<SensorType<? extends Sensor<? super DwarfEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY,
            SensorType.VILLAGER_HOSTILES, WHRegistry.Hostiles, WHRegistry.VISIBLE_VILLAGER_BABIES, WHRegistry.SECONDARY_POIS, WHRegistry.Lord_LastSeen);
    /**
     * initialization
     */

    public DwarfEntity(EntityType<? extends DwarfEntity> entityType, World world) {
        super(entityType, world);
        this.setVillagerDataBase();
        this.setEquipment(this.getProfession());
        this.initDataTracker();
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        if (spawnReason == SpawnReason.BREEDING) {
            this.setProfession(DwarfProfessionRecord.Warrior.ID());
        }
        return super.initialize(world, difficulty, spawnReason, entityData);
    }



    /**
     * all custom profession
     */

    @Override
    public VillagerData getVillagerData() {
        return null;
    }
    @Override
    public void setVillagerData(VillagerData villagerData) {
    }

    public int getProfessionID()
    {
        return Math.max(this.dataTracker.get(Profession), 0);//0farmer,1miner,2Engineer,3builder,4slayer,5lord,6 warrior
    }

    public void setVillagerData() {
        this.setVillagerData(getProfessionID(), getProfessionLevel());
    }

    public void setVillagerDataBase() {
        this.setVillagerData(DwarfProfessionRecord.Warrior.ID(),1);
    }



    public void setVillagerData(int prof,int level) {

        if (getProfession().ID() != prof) {
            this.offers = null;
        }
        if(dataTracker.get(Profession)!=DwarfProfessionRecord.Lord.ID()){
            this.dataTracker.set(Profession,prof);}
        this.dataTracker.set(Level,level);
    }



    public void setVillagerlevel(int level) {
        setVillagerData(getProfessionID(),level);
    }

    public void setVillagerProfession(DwarfProfessionRecord profession){
        setVillagerData(profession.ID(), getProfessionLevel());
        this.setEquipment(profession);
        this.updateAttributes(profession);
    }

/*
    public DwarfProfessionRecord getProfession(){
        for(DwarfProfessionRecord prof : DwarfProfession.Profession){
            if (prof.getID()==getProfessionID()){
                return prof;
            }
        }
        return DwarfProfession.Warrior;
    }

 */
    public DwarfProfessionRecord getProfession(){
        return this.getRegistryManager().get(WHRegistry.DwarfProfessionKey).stream().filter(p -> p.ID()==getProfessionID()).findFirst().orElse(DwarfProfessionRecord.Warrior);
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
    protected void setEquipment(DwarfProfessionRecord prof)
    {
        if(hasStackEquipped(EquipmentSlot.MAINHAND)){
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        if(hasStackEquipped(EquipmentSlot.OFFHAND)){
            this.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }
        this.equipStack(EquipmentSlot.MAINHAND,new ItemStack(prof.mainhainditem()));
        this.equipStack(EquipmentSlot.OFFHAND,new ItemStack(prof.offhanditem()));
    }

    protected void updateAttributes(DwarfProfessionRecord prof){
        if(prof.equals(DwarfProfessionRecord.Slayer)){
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

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVillagerData(nbt.getInt("profession"),nbt.getInt("level"));
        Boolean hasspawnedlord = nbt.getBoolean("canspawnlord");
        this.setCanPickUpLoot(true);
        this.lastRestockTime = nbt.getLong("LastRestockDwarf");
        this.restocksToday = nbt.getInt("RestocksTodayDwarf");
        nbt.putByte("FoodLevelDwarf", (byte)this.foodLevel);

    }


    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (nbt.contains("FoodLevel", NbtElement.BYTE_TYPE)) {
            this.foodLevel = nbt.getByte("FoodLevel");
        }
        nbt.putInt("level", this.getProfessionLevel());
        nbt.putInt("profession",this.getProfessionID());
        nbt.putLong("LastRestockdwarf", this.lastRestockTime);
        nbt.putInt("RestocksTodayDwarf", this.restocksToday);


    }

    private void initDataTracker() {
        this.dataTracker.set(Profession, DwarfProfessionRecord.Warrior.ID());
        this.dataTracker.set(Level,1);
    }
    @Nullable
    public DwarfEntity createChild(ServerWorld world, PassiveEntity entity) {
        double d0 = this.random.nextDouble();

        DwarfEntity dwarfEntity = new DwarfEntity(Entityinit.DWARF, world);
        dwarfEntity.initialize(world, world.getLocalDifficulty(dwarfEntity.getBlockPos()), SpawnReason.BREEDING, (EntityData)null);
        return dwarfEntity;
    }



    public Brain.Profile<DwarfEntity> createDwarfBrainProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }

    @Override
    public Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        Brain<DwarfEntity> brain = this.createDwarfBrainProfile().deserialize(dynamic);
        this.initBrain(brain);
        return brain;
    }

    @Override
    public void reinitializeBrain(ServerWorld world) {
        Brain<DwarfEntity> brain = (Brain<DwarfEntity>) this.brain;
        brain.stopAllTasks(world, this);
        this.brain = brain.copy();
        this.initBrain((Brain<DwarfEntity>) this.brain);
    }

    private void initBrain(Brain<DwarfEntity> entityBrain) {
        DwarfProfessionRecord dwarfProfession = this.getProfession();
        if (this.isBaby()) {
            entityBrain.setSchedule(Schedule.VILLAGER_BABY);
            entityBrain.setTaskList(Activity.PLAY, VillagerTaskListProvider.createPlayTasks(0.5F));
            entityBrain.setTaskList(Activity.PANIC, VillagerTaskListProvider.createPanicTasks(VillagerProfession.NONE, 0.5F));
        } else {
            entityBrain.setSchedule(Schedule.VILLAGER_DEFAULT);

            entityBrain.setTaskList(Activity.PANIC, DwarfCombattasks.getAttackPackage(dwarfProfession, 0.5F));
            entityBrain.setTaskList(Activity.WORK, DwarfVillagerTasks.createWorkTasks(dwarfProfession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT)));
        }

        entityBrain.setTaskList(Activity.CORE, DwarfVillagerTasks.createCoreTasks(dwarfProfession, 0.5F));
        VillagerProfession villagerProfession = VillagerProfession.NONE;
        entityBrain.setTaskList(Activity.MEET, VillagerTaskListProvider.createMeetTasks(villagerProfession, 0.5f), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleState.VALUE_PRESENT)));
        entityBrain.setTaskList(Activity.REST, VillagerTaskListProvider.createRestTasks(villagerProfession, 0.5f));
        entityBrain.setTaskList(Activity.IDLE, VillagerTaskListProvider.createIdleTasks(villagerProfession, 0.5f));
        entityBrain.setTaskList(Activity.PRE_RAID, VillagerTaskListProvider.createPreRaidTasks(villagerProfession, 0.5f));
        entityBrain.setTaskList(Activity.RAID, VillagerTaskListProvider.createRaidTasks(villagerProfession, 0.5f));
        entityBrain.setTaskList(Activity.HIDE, VillagerTaskListProvider.createHideTasks(villagerProfession, 0.5f));
        entityBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        entityBrain.setDefaultActivity(Activity.IDLE);
        entityBrain.doExclusively(Activity.IDLE);
        entityBrain.refreshActivities(this.getWorld().getTimeOfDay(), this.getWorld().getTime());
    }
    @Override
    public void summonGolem(ServerWorld world, long time, int requiredCount){
        this.SummonLord(world, time, requiredCount);
    }
    public void SummonLord(ServerWorld serverWorld, long time, int requiredCount) {
        if (this.canSummonGolem(time)) {
            Box axisalignedbb = this.getBoundingBox().expand(10.0D, 10.0D, 10.0D);
            List<DwarfEntity> list = serverWorld.getNonSpectatingEntities(DwarfEntity.class, axisalignedbb);
            List<DwarfEntity> list1 = list.stream().filter((dwarfEntity) -> {
                return dwarfEntity.canSummonGolem(time);
            }).limit(5L).collect(Collectors.toList());
            if (list1.size() >= requiredCount) {
                if (this.tryspawnlord(serverWorld)) {
                    list.forEach(LordLastSeenSensor::rememberIronGolem);
                }
            }
        }
    }

    @Override
    public boolean canSummonGolem(long time) {
        if (!this.golemSpawnConditionsMet(this.getWorld().getTime())) {
            return false;
        } else {
            return !this.brain.hasMemoryModule(MemoryModuleType.GOLEM_DETECTED_RECENTLY);
        }
    }

    private boolean tryspawnlord(ServerWorld world){
        int i = MathHelper.floor(this.getX());
        int j = MathHelper.floor(this.getY());
        int k = MathHelper.floor(this.getZ());
        DwarfEntity dwarfEntity = Entityinit.DWARF.create(world);
        for(int l = 0; l < 15; ++l) {
            int i1 = i + MathHelper.nextInt(this.random, 2, 6) * MathHelper.nextInt(this.random, -1, 1);
            int j1 = j + MathHelper.nextInt(this.random, 2, 6) * MathHelper.nextInt(this.random, -1, 1);
            int k1 = k + MathHelper.nextInt(this.random, 2, 6) * MathHelper.nextInt(this.random, -1, 1);
            BlockPos blockpos = new BlockPos(i1, j1, k1);
            EntityType<?> entityType = dwarfEntity.getType();
            if (!SpawnRestriction.isSpawnPosAllowed(entityType, this.getWorld(), blockpos) || !SpawnRestriction.canSpawn(entityType, world, SpawnReason.REINFORCEMENT, blockpos, this.getWorld().random)) continue;
            dwarfEntity.setPosition(i1, j1, k1);
            if ( !this.getWorld().doesNotIntersectEntities(dwarfEntity) || !this.getWorld().isSpaceEmpty(dwarfEntity) || this.getWorld().containsFluid(dwarfEntity.getBoundingBox())) continue;
            dwarfEntity.initialize(world,world.getLocalDifficulty(dwarfEntity.getBlockPos()),SpawnReason.MOB_SUMMONED,null);
            dwarfEntity.setProfession(DwarfProfessionRecord.Lord.ID());
            world.spawnEntityAndPassengers(dwarfEntity);
            return true;
        }
        return false;
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

    @Override
    public boolean canGather(ItemStack stack) {
        Item item = stack.getItem();
        return (WANTED_ITEMS.contains(item) || this.getProfession().gatherableItems().contains(item)) && this.getInventory().canInsert(stack);
    }
    /**
     * other
     */

    public void releaseTicketFor(MemoryModuleType<GlobalPos> pos) {
        if (!(this.getWorld() instanceof ServerWorld)) {
            return;
        }
        MinecraftServer minecraftServer = ((ServerWorld)this.getWorld()).getServer();
        this.brain.getOptionalRegisteredMemory(pos).ifPresent(posx -> {
            ServerWorld serverWorld = minecraftServer.getWorld(posx.dimension());
            if (serverWorld == null) {
                return;
            }
            PointOfInterestStorage pointOfInterestStorage = serverWorld.getPointOfInterestStorage();
            Optional<RegistryEntry<PointOfInterestType>> optional = pointOfInterestStorage.getType(posx.pos());
            BiPredicate<DwarfEntity, RegistryEntry<PointOfInterestType>> biPredicate = POINTS_OF_INTEREST.get(pos);
            if (optional.isPresent() && biPredicate.test(this, optional.get())) {
                pointOfInterestStorage.releaseTicket(posx.pos());
                DebugInfoSender.sendPointOfInterest(serverWorld, posx.pos());
            }
        });
    }

    @Override
    protected void fillRecipes() {
        Int2ObjectMap<TradeOffers.Factory[]> int2ObjectMap2 = DwarfTrades.TRADES.get(this.getProfession());
        if (int2ObjectMap2 == null || int2ObjectMap2.isEmpty()) {
            return;
        }
        TradeOffers.Factory[] factorys = int2ObjectMap2.get(this.getProfessionLevel());
        if (factorys == null) {
            return;
        }
        TradeOfferList tradeOfferList = this.getOffers();
        this.fillRecipesFromPool(tradeOfferList, factorys, 2);
    }

    @Override
    @Nullable
    public <T extends MobEntity> T convertTo(EntityType<T> entityType, boolean keepEquipment) {
        if (this.isRemoved()|| entityType.equals(EntityType.ZOMBIE_VILLAGER)) {
            return null;
        }
        MobEntity mobEntity = (MobEntity)entityType.create(this.getWorld());
        if (mobEntity == null) {
            return null;
        }
        mobEntity.copyPositionAndRotation(this);
        mobEntity.setBaby(this.isBaby());
        mobEntity.setAiDisabled(this.isAiDisabled());
        if (this.hasCustomName()) {
            mobEntity.setCustomName(this.getCustomName());
            mobEntity.setCustomNameVisible(this.isCustomNameVisible());
        }
        if (this.isPersistent()) {
            mobEntity.setPersistent();
        }
        mobEntity.setInvulnerable(this.isInvulnerable());
        if (keepEquipment) {
            mobEntity.setCanPickUpLoot(this.canPickUpLoot());
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                ItemStack itemStack = this.getEquippedStack(equipmentSlot);
                if (itemStack.isEmpty()) continue;
                mobEntity.equipStack(equipmentSlot, itemStack.copyAndEmpty());
                mobEntity.setEquipmentDropChance(equipmentSlot, this.getDropChance(equipmentSlot));
            }
        }
        this.getWorld().spawnEntity(mobEntity);
        if (this.hasVehicle()) {
            Entity entity = this.getVehicle();
            this.stopRiding();
            mobEntity.startRiding(entity, true);
        }
        this.discard();
        return (T)mobEntity;
    }

    @Override
    protected void resetCustomer() {
        if (this.getProfession() == DwarfProfessionRecord.Warrior && this.hasCustomer()) {
            super.resetCustomer();
        }
    }

    private void sayNo() {
        this.setHeadRollingTimeLeft(40);
        if (!this.getWorld().isClient()) {
            this.playSound(SoundEvents.ENTITY_VILLAGER_NO);
        }
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isOf(Items.VILLAGER_SPAWN_EGG) && this.isAlive() && !this.hasCustomer() && !this.isSleeping()) {
            if (this.isBaby()) {
                this.sayNo();
                return ActionResult.success(this.getWorld().isClient);
            }
            if (!this.getWorld().isClient) {
                boolean bl = this.getOffers().isEmpty();
                if (hand == Hand.MAIN_HAND) {
                    if (bl) {
                        this.sayNo();
                    }
                    player.incrementStat(Stats.TALKED_TO_VILLAGER);
                }
                if (bl) {
                    return ActionResult.CONSUME;
                }
                this.beginTradeWith(player);
            }
            return ActionResult.success(this.getWorld().isClient);
        }
        return super.interactMob(player, hand);
    }

    private void beginTradeWith(PlayerEntity customer) {
        this.prepareOffersFor(customer);
        this.setCustomer(customer);
        this.sendOffers(customer, this.getDisplayName(), this.getProfessionLevel());
    }



    private void prepareOffersFor(PlayerEntity player) {
        int i = this.getReputation(player);
        if (i != 0) {
            for (TradeOffer tradeOffer : this.getOffers()) {
                tradeOffer.increaseSpecialPrice(-MathHelper.floor((float)i * tradeOffer.getPriceMultiplier()));
            }
        }
        if (player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE)) {
            StatusEffectInstance statusEffectInstance = player.getStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);
            int j = statusEffectInstance.getAmplifier();
            for (TradeOffer tradeOffer2 : this.getOffers()) {
                double d = 0.3 + 0.0625 * (double)j;
                int k = (int)Math.floor(d * (double)tradeOffer2.getOriginalFirstBuyItem().getCount());
                tradeOffer2.increaseSpecialPrice(-Math.max(k, 1));
            }
        }
    }

    private void updateDemandBonus() {
        for (TradeOffer tradeOffer : this.getOffers()) {
            tradeOffer.updateDemandBonus();
        }
    }

    public void restock() {
        this.updateDemandBonus();
        for (TradeOffer tradeOffer : this.getOffers()) {
            tradeOffer.resetUses();
        }
        this.sendOffersToCustomer();
        this.lastRestockTime = this.getWorld().getTime();
        ++this.restocksToday;
    }

    public boolean shouldRestock() {
        long l = this.lastRestockTime + 12000L;
        long m = this.getWorld().getTime();
        boolean bl = m > l;
        long n = this.getWorld().getTimeOfDay();
        if (this.lastRestockCheckTime > 0L) {
            long p = n / 24000L;
            long o = this.lastRestockCheckTime / 24000L;
            bl |= p > o;
        }
        this.lastRestockCheckTime = n;
        if (bl) {
            this.lastRestockTime = m;
            this.clearDailyRestockCount();
        }
        return this.canRestock() && this.needsRestock();
    }

    private boolean needsRestock() {
        for (TradeOffer tradeOffer : this.getOffers()) {
            if (!tradeOffer.hasBeenUsed()) continue;
            return true;
        }
        return false;
    }

    private boolean canRestock() {
        return this.restocksToday == 0 || this.restocksToday < 2 && this.getWorld().getTime() > this.lastRestockTime + 2400L;
    }

    private void sendOffersToCustomer() {
        TradeOfferList tradeOfferList = this.getOffers();
        PlayerEntity playerEntity = this.getCustomer();
        if (playerEntity != null && !tradeOfferList.isEmpty()) {
            playerEntity.sendTradeOffers(playerEntity.currentScreenHandler.syncId, tradeOfferList, this.getProfessionLevel(), this.getExperience(), this.isLeveledMerchant(), this.canRefreshTrades());
        }
    }

    public void playWorkSound() {
        this.playSound(this.getProfession().workSound());
    }

    private void clearDailyRestockCount() {
        this.restockAndUpdateDemandBonus();
        this.restocksToday = 0;
    }

    private void restockAndUpdateDemandBonus() {
        int i = 2 - this.restocksToday;
        if (i > 0) {
            for (TradeOffer tradeOffer : this.getOffers()) {
                tradeOffer.resetUses();
            }
        }
        for (int j = 0; j < i; ++j) {
            this.updateDemandBonus();
        }
        this.sendOffersToCustomer();
    }

    private boolean canLevelUp() {
        int i = this.getProfessionLevel();
        return VillagerData.canLevelUp(i) && this.experience >= VillagerData.getUpperLevelExperience(i);
    }

    @Override
    protected void afterUsing(TradeOffer offer) {
        int i = 3 + this.random.nextInt(4);
        this.experience += offer.getMerchantExperience();
        this.lastCustomer = this.getCustomer();
        if (this.canLevelUp()) {
            this.levelUpTimer = 40;
            this.levelingUp = true;
            i += 5;
        }
        if (offer.shouldRewardPlayerExperience()) {
            this.getWorld().spawnEntity(new ExperienceOrbEntity(this.getWorld(), this.getX(), this.getY() + 0.5, this.getZ(), i));
        }
    }

    private void levelUp() {
        this.setVillagerlevel(this.getProfessionLevel()+1);
        this.fillRecipes();
    }

    @Override
    protected void mobTick() {
        if (!this.hasCustomer() && this.levelUpTimer > 0) {
            --this.levelUpTimer;
            if (this.levelUpTimer <= 0) {
                if (this.levelingUp) {
                    this.levelUp();
                    this.levelingUp = false;
                }
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0));
            }
        }
        if (this.lastCustomer != null && this.getWorld() instanceof ServerWorld) {
            ((ServerWorld)this.getWorld()).handleInteraction(EntityInteraction.TRADE, this.lastCustomer, this);
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
            this.lastCustomer = null;
        }
        super.mobTick();
    }

    @Override
    protected Text getDefaultName() {
        return Text.translatable(this.getType().getTranslationKey() + "." + WHRegistry.DWARF_PROFESSIONS.getId(this.getProfession()).getPath());
    }

    @Override
    public boolean isReadyToBreed() {
        return this.foodLevel + this.getAvailableFood() >= 12 && !this.isSleeping() && this.getBreedingAge() == 0;
    }

    private boolean lacksFood() {
        return this.foodLevel < 12;
    }

    private void consumeAvailableFood() {
        if (!this.lacksFood() || this.getAvailableFood() == 0) {
            return;
        }
        for (int i = 0; i < this.getInventory().size(); ++i) {
            int j;
            Integer integer;
            ItemStack itemStack = this.getInventory().getStack(i);
            if (itemStack.isEmpty() || (integer = ITEM_FOOD_VALUES.get(itemStack.getItem())) == null) continue;
            for (int k = j = itemStack.getCount(); k > 0; --k) {
                this.foodLevel += integer.intValue();
                this.getInventory().removeStack(i, 1);
                if (this.lacksFood()) continue;
                return;
            }
        }
    }

    public void eatForBreeding() {
        this.consumeAvailableFood();
        this.depleteFood(12);
    }

    private void depleteFood(int amount) {
        this.foodLevel -= amount;
    }

    private int getAvailableFood() {
        SimpleInventory simpleInventory = this.getInventory();
        return ITEM_FOOD_VALUES.entrySet().stream().mapToInt(item -> simpleInventory.count(item.getKey()) * item.getValue()).sum();
    }



}
