package warhammermod.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.SpecialSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import warhammermod.world.SkavenPatrolSpawner;

import java.util.ArrayList;
import java.util.List;


@Mixin(ServerWorld.class)
public abstract class Serverworld_injection {

    @ModifyVariable(at = @At("CTOR_HEAD"),method = "<init>")
    public List<SpecialSpawner> replacelist(List<SpecialSpawner> spawners){
        List<SpecialSpawner> templist = new ArrayList<>(spawners);
        templist.add(new SkavenPatrolSpawner());
        return templist;
    }

    /*
    * @Shadow @Final
    @Mutable
    private List<SpecialSpawner> spawners;
    @ModifyVariable(at = @At("HEAD"),method = "<init>",ordinal =1)
    private void init(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List<SpecialSpawner> Spawners, boolean shouldTickTime, @Nullable RandomSequencesState randomSequencesState, CallbackInfo callbackInfo){
        List<SpecialSpawner> templist = new ArrayList<>(spawners);
        templist.add(new SkavenPatrolSpawner());
        spawners= templist;
    }
    * */
}
