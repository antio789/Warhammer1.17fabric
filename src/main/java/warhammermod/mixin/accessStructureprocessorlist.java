package warhammermod.mixin;

import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.structure.processor.StructureProcessorList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;


@Mixin(StructureProcessorList.class)
public interface accessStructureprocessorlist {
    @Invoker("<init>")
    static <U extends Sensor<?>> SensorType<U> init(Supplier<U> factory) {
        throw new AssertionError("Untransformed Accessor!");
    }
}
