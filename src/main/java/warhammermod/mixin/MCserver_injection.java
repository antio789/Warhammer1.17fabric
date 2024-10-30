package warhammermod.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.SpecialSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import warhammermod.world.SkavenPatrolSpawner;

import java.util.ArrayList;
import java.util.List;


@Mixin(ServerWorld.class)
public abstract class MCserver_injection {

    @ModifyVariable(at = @At("CTOR_HEAD"),method = "<init>",target = @Desc("list"))
    private List<SpecialSpawner> test(List<SpecialSpawner> spawner){
        List<SpecialSpawner> templist = new ArrayList<>(spawner);
        templist.add(new SkavenPatrolSpawner());
        return templist;
    }
}
