package net.minecraft.world.level.block.grower;

import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;

public class MangroveTreeGrower extends WorldGenTreeProvider {

    private final float tallProbability;

    public MangroveTreeGrower(float f) {
        this.tallProbability = f;
    }

    @Nullable
    @Override
    protected Holder<? extends WorldGenFeatureConfigured<?, ?>> getConfiguredFeature(RandomSource randomsource, boolean flag) {
        return randomsource.nextFloat() < this.tallProbability ? TreeFeatures.TALL_MANGROVE : TreeFeatures.MANGROVE;
    }
}
