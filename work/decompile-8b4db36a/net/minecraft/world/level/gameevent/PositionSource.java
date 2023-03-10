package net.minecraft.world.level.gameevent;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.IRegistry;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;

public interface PositionSource {

    Codec<PositionSource> CODEC = IRegistry.POSITION_SOURCE_TYPE.byNameCodec().dispatch(PositionSource::getType, PositionSourceType::codec);

    Optional<Vec3D> getPosition(World world);

    PositionSourceType<?> getType();
}
