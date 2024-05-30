package warhammermod.Entities.Projectile;



import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import warhammermod.utils.Registry.Entityinit;


@EnvironmentInterfaces({@EnvironmentInterface(
        value = EnvType.CLIENT,
        itf = FlyingItemEntity.class
)})
public class StoneEntity extends ProjectileBase implements FlyingItemEntity{
    public StoneEntity(World world){
        super(Entityinit.STONEENTITY,world);
    }

    public StoneEntity(EntityType<? extends StoneEntity> p_i50148_1_, World p_i50148_2_) {
        super(p_i50148_1_, p_i50148_2_);
    }

    public StoneEntity(World worldin, LivingEntity shooter, float damageIn, EntityType<? extends ProjectileBase> type) {
        super(worldin, shooter,damageIn, type);
    }
    public StoneEntity(World world, EntityType<? extends ProjectileBase> entity, double x, double y, double z) {
        super( world,entity, x, y, z);
    }

    public StoneEntity(LivingEntity shooter, World world, float damage) {
        this(world, shooter, damage, Entityinit.STONEENTITY);
    }






    public ItemStack getStack() {
        return new ItemStack(Items.COBBLESTONE);
    }

    public void setTotaldamage(float sum) {
        this.totaldamage = sum;
    }

    protected void onEntityHit(EntityHitResult p_213868_1_) {
        float f = (float)this.getVelocity().length();
        double damageSum = projectiledamage+extradamage;
        int i = MathHelper.ceil(MathHelper.clamp((double)f * damageSum, 0.0D, 2.147483647E9D));
        if (this.isCritical()) {
            long j = this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }
        setTotaldamage(i);
        super.onEntityHit(p_213868_1_);

    }

    @Override
    public float getTotalDamage(){
        return totaldamage;
    }









}
