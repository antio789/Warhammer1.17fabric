package warhammermod.Items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;

import java.util.List;
import java.util.Optional;

public record Ammocomponent(int ammocount) {//test adding component firing rotation that update on client
    public static final Ammocomponent DEFAULT = new Ammocomponent(0);
    public static final Codec<Ammocomponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codecs.NONNEGATIVE_INT.fieldOf("ammo").forGetter(Ammocomponent::ammocount)
                    )
                    .apply(instance, Ammocomponent::new)
    );

    public static final PacketCodec<ByteBuf, Ammocomponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            Ammocomponent::ammocount,
            Ammocomponent::new
    );

}
