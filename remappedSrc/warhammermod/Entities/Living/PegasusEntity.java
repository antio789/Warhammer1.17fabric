package warhammermod.Entities.Living;


import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import warhammermod.utils.Clientside;
import warhammermod.utils.Registry.Entityinit;


public class PegasusEntity extends HorseEntity {

    public PegasusEntity(EntityType<? extends HorseEntity> p_i50238_1_, World p_i50238_2_) {
        super(p_i50238_1_, p_i50238_2_);
    }

    /**
     * all of this crap just to have special colored pegasuses
     */
    private static final TrackedData<Boolean> MixBlood = DataTracker.registerData(PegasusEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Integer> Texture_parameter = DataTracker.registerData(PegasusEntity.class, TrackedDataHandlerRegistry.INTEGER);


    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MixBlood, false);
        this.dataTracker.startTracking(Texture_parameter, 0);
    }

    public void writeCustomDataToNbt(NbtCompound p_213281_1_) {
        super.writeCustomDataToNbt(p_213281_1_);
        p_213281_1_.putInt("ModVariant", this.getHorseVariant());
        p_213281_1_.putBoolean("mixblood", this.ismixblood());
        if (!this.items.getStack(1).isEmpty()) {
            p_213281_1_.put("ArmorItem", this.items.getStack(1).writeNbt(new NbtCompound()));
        }

    }

    public void readCustomDataFromNbt(NbtCompound p_70037_1_) {
        super.readCustomDataFromNbt(p_70037_1_);
        this.setHorseVariant(p_70037_1_.getInt("ModVariant"));
        setMixedblood(p_70037_1_.getBoolean("mixblood"));
        this.updateSaddle();
    }


    private void setHorseVariant(int p_234242_1_) {
        this.dataTracker.set(Texture_parameter, p_234242_1_);
    }

    private int getHorseVariant() {
        return this.dataTracker.get(Texture_parameter);
    }

    private void setHorseVariant(HorseColor p_234238_1_, HorseMarking p_234238_2_) {
        this.setHorseVariant(p_234238_1_.getId() & 255 | p_234238_2_.getId() << 8 & '\uff00');
    }

    public HorseColor getVariant() {
        return HorseColor.byId(this.getHorseVariant() & 255);
    }

    public HorseMarking getMarking() {
        return HorseMarking.byIndex((this.getHorseVariant() & '\uff00') >> 8);
    }

    public void setMixedblood(Boolean mixblood) {
        this.dataTracker.set(MixBlood, mixblood);
    }
    public Boolean ismixblood() {
        return this.dataTracker.get(MixBlood);
    }

    public PassiveEntity createChild(ServerWorld world, PassiveEntity partner) {
        AbstractHorseEntity pegasus=null;

        if (partner instanceof DonkeyEntity) {
            pegasus = EntityType.MULE.create(world);
        }

        else if (partner instanceof PegasusEntity) {
            pegasus = Entityinit.Pegasus.create(world);
            ((PegasusEntity) pegasus).setHorseVariant(HorseColor.WHITE, HorseMarking.NONE);
        }

        else if (random.nextFloat() < 0.15) {
            HorseEntity mating_partner = (HorseEntity) partner;
            pegasus = Entityinit.Pegasus.create(world);
            ((PegasusEntity) pegasus).setMixedblood(true);

            int j = this.random.nextInt(9);
            HorseColor coatcolors;
            if (j < 4) {
                ((PegasusEntity) pegasus).setMixedblood(false);
                coatcolors=HorseColor.WHITE;
            }
            else if (j < 8) {
                coatcolors = mating_partner.getVariant();
            } else {
                coatcolors = Util.getRandom(HorseColor.values(), this.random);
            }

            int k = this.random.nextInt(5);
            HorseMarking markings;
            if(k<2){
                markings=HorseMarking.NONE;
            }
            else if (k < 4) {
                markings = mating_partner.getMarking();
            } else {
                markings = Util.getRandom(HorseMarking.values(), this.random);
            }

            ((PegasusEntity) pegasus).setHorseVariant(coatcolors, markings);
        } else
        {
            partner.createChild(world, partner);
        }
        if (pegasus != null) {
            this.setChildAttributes(partner, pegasus);
        }

        return pegasus;
    }

    /**
     * fun part of moving the horse
     */

    private int jumpcounter;
    private boolean lastpressjump;
    private int Timer;

    private boolean iselytrafly = false;
    private boolean isstationaryflying = false;
    public boolean flydownsafely = false;

    public boolean isIselytrafly() {
        return iselytrafly;
    }

    public boolean isIsstationaryflying() {
        return isstationaryflying;
    }

    private void changeflymode(){
        if(iselytrafly==isstationaryflying) {
            isstationaryflying=true;
            iselytrafly=false;
        }else{
            iselytrafly = !iselytrafly;
            isstationaryflying = !isstationaryflying;
        }
    }

    private void setFlyFalse(){
        iselytrafly=false;
        isstationaryflying=false;
    }

    public Boolean isgrounded(){
        BlockPos blockpos = this.getVelocityAffectingPos();
        return!(this.world.getBlockState(blockpos).getBlock()== Blocks.AIR);
    }

    public boolean isFlying(){
        return iselytrafly ||isstationaryflying;
    }



    public void travel(Vec3d p_213352_1_) {
        if (this.isAlive()) {
            if (this.hasPassengers() && this.canBeControlledByRider() && this.isSaddled()) {

                LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
                if (this.world.isClient()) {
                    ClientPlayerEntity player = (ClientPlayerEntity) livingentity;
                    boolean isjumping = player.input.jumping;
                    if (isjumping) {
                        if (jumpcounter == 0 && !lastpressjump) {
                            jumpcounter = 10;
                        } else if (!lastpressjump) {
                            changeflymode();
                            jumpcounter = 0;
                        }
                        lastpressjump = true;
                    } else lastpressjump = false;
                    if (jumpcounter > 0) {
                        jumpcounter--;
                    }
                }

                if(!isFlying()) {
                    {
                        super.travel(p_213352_1_);
                        return;
                    }
                }

                this.setYaw( livingentity.getYaw());
                this.prevYaw = this.getYaw();
                this.setPitch(livingentity.getPitch() * 0.5F);
                this.setRotation(this.getYaw(), this.getPitch());
                this.bodyYaw = this.getYaw();
                this.headYaw = this.bodyYaw;
                float f = livingentity.sidewaysSpeed * 0.5F;
                float f1 = livingentity.forwardSpeed;

                if (isstationaryflying || (iselytrafly && livingentity.getPitch() < 0)) {
                    if (Timer > 25) {
                        this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_FLAP, 0.65F, 3F);
                        Timer = 0;
                    } else Timer++;
                }
                if (isgrounded() || this.onGround) {
                    setFlyFalse();
                }
                if (iselytrafly && world.isClient()) {
                    elytramovement();
                }
                else if (isstationaryflying && world.isClient()) {
                    stationnarymovement(livingentity,f,p_213352_1_.y,f1);
                }
            }
            else {
                setFlyFalse();
                if(fallDistance>5){
                    flydownsafely();
                }else
                super.travel(p_213352_1_);
            }

        }
    }






    public void elytramovement() {
        double d = 0.08D;
        boolean bl = this.getVelocity().y <= 0.0D;
        if (bl && this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
            d = 0.01D;
            this.fallDistance = 0.0F;
        }
        Vec3d vector3d = this.getVelocity();
        this.fallDistance = 1.0F;
        Vec3d vec3d = this.getControllingPassenger().getRotationVector();
        float f6 = this.getPitch()/0.5F * ((float) Math.PI / 180F); //this is the fix to have the elytra behave normally
        double dir = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
        double speed = vector3d.horizontalLength();//x^2+z^2 pos
        double d12 = vec3d.length();
        float f3 = MathHelper.cos(f6);
        f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D)); //f3^2 * min 1 ,
        double d0 = d;
        vector3d = this.getVelocity().add(0.0D, d0 * (-1.0D + (double) f3 * 0.75D), 0.0D);

        if (vector3d.y < 0.0D && dir > 0.0D) {// descend x y z +
            double d3 = vector3d.y * -0.1D * (double) f3; // +
            vector3d = vector3d.add(vec3d.x * d3 / dir, d3, vec3d.z * d3 / dir);
        }


        //  0.45*getCustomJump()()*-MathHelper.sin(f);
        if (f6 < 0.0F && dir > 0.0D) { // monte x z - y
            double d13 = speed * (double) (-MathHelper.sin(f6)) * 0.04D; // +
            if (speed < 0.6) {
                vector3d = vector3d.add(vec3d.x * 0.025, 0, vec3d.z * 0.025);
            }
            else
                vector3d = vector3d.add(-vec3d.x * d13 / dir, d13 * 3.2D, -vec3d.z * d13 / dir);  //normal behaviour
            if (vector3d.y < -0.05) {
                vector3d = vector3d.add(0, getJumpStrength()*0.6, 0);
            }

        }

        if (dir > 0.0D) {
            vector3d = vector3d.add((vec3d.x / dir * speed - vector3d.x) * 0.1D, 0.0D, (vec3d.z / dir * speed - vector3d.z) * 0.1D);
        }

        this.setVelocity(vector3d.multiply(0.99F, 0.98F, 0.99F));
        this.move(MovementType.SELF, this.getVelocity());
        if (this.horizontalCollision && !this.world.isClient()) {
            double d14 = this.getVelocity().horizontalLength();
            double d4 = speed - d14;
            float f4 = (float) (d4 * 10.0D - 3.0D);
            if (f4 > 0.0F) {
                this.playSound(this.getFallSound((int) f4), 1.0F, 1.0F);
                this.damage(DamageSource.FLY_INTO_WALL, f4);
            }
        }
    }

    public void stationnarymovement( LivingEntity livingentity,  float f,double y, float f1){
        ClientPlayerEntity player = (ClientPlayerEntity) livingentity;
        double flap = Math.max(Math.abs(Math.cos(Math.PI * this.age / 25)), 0.5) * 0.4;
        if (player.input.jumping) {
            setVelocity(getVelocity().x, flap * getJumpStrength()*1.5, getVelocity().z);
        } else if (Clientside.pegasus_down.isPressed()) {
            setVelocity(getVelocity().x, -flap * getJumpStrength(), getVelocity().z);
        } else setVelocity(getVelocity().x, (flap - 0.32) * 0.08, getVelocity().z);

        this.setMovementSpeed((float) this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue());
        this.fallDistance = 0;
        movehorse(new Vec3d((double) f * 0.5, y, (double) f1 * 0.5));
    }

    private void movehorse(Vec3d vec){
        this.updateVelocity(0.015F, vec);
        this.setVelocity(this.getVelocity());
        this.move(MovementType.SELF, this.getVelocity());
        //Vec3 vector3d5 = this.handleRelativeFrictionAndCalculateMovement(vec, f3);
        //this.setDeltaMovement(vector3d5.x * 0.9, vector3d5.y, vector3d5.z * 0.9);
    }
//not working doesnt have lookangle
    private void flydownsafely(){


            Vec3d vec3d = new Vec3d(this.getRotationVector().x,-0.2,this.getRotationVector().z);
            Vec3d motionvec = this.getVelocity();
            float f = 20 * 0.017453292F;
            double d1 = vec3d.length();
            float f4 = MathHelper.cos(f);
            f4 = (float)((double)f4 * (double)f4 * Math.min(1.0D, d1 / 0.4D));
            if(motionvec.y<-0.745) {
                ;
                motionvec = this.getVelocity().add(0.0D, -0.08D + (double) f4 * 0.06D, 0.0D);
            }

            this.setVelocity(motionvec.multiply((double) 1F, (double) 0.98F, (double) 1F));
            this.move(MovementType.SELF, this.getVelocity());

    }

    /**
     * because MC falldamage is stupid
     * 5blocks - -0.745
     * 10 -0.964
     * 15 -1.143
     * 20 -1.26
     * 100 -1.85
     *
     * polynomial approach doesnt work because server doesnt have speed
     */

    public boolean handleFallDamage(float Falldistance, float modifier,DamageSource damageSource) {
        return super.handleFallDamage(Falldistance,modifier,damageSource);
    }

    protected int computeFallDamage(float Falldistance, float modifier) {
        return 0;
    }



}
