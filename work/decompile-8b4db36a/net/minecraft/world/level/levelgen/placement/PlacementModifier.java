package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.util.RandomSource;

public abstract class PlacementModifier {

    public static final Codec<PlacementModifier> CODEC = IRegistry.PLACEMENT_MODIFIERS.byNameCodec().dispatch(PlacementModifier::type, PlacementModifierType::codec);

    public PlacementModifier() {}

    public abstract Stream<BlockPosition> getPositions(PlacementContext placementcontext, RandomSource randomsource, BlockPosition blockposition);

    public abstract PlacementModifierType<?> type();
}
