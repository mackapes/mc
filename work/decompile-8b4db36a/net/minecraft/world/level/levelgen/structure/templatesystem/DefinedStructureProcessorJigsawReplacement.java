package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.blocks.ArgumentBlock;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;

public class DefinedStructureProcessorJigsawReplacement extends DefinedStructureProcessor {

    public static final Codec<DefinedStructureProcessorJigsawReplacement> CODEC = Codec.unit(() -> {
        return DefinedStructureProcessorJigsawReplacement.INSTANCE;
    });
    public static final DefinedStructureProcessorJigsawReplacement INSTANCE = new DefinedStructureProcessorJigsawReplacement();

    private DefinedStructureProcessorJigsawReplacement() {}

    @Nullable
    @Override
    public DefinedStructure.BlockInfo processBlock(IWorldReader iworldreader, BlockPosition blockposition, BlockPosition blockposition1, DefinedStructure.BlockInfo definedstructure_blockinfo, DefinedStructure.BlockInfo definedstructure_blockinfo1, DefinedStructureInfo definedstructureinfo) {
        IBlockData iblockdata = definedstructure_blockinfo1.state;

        if (iblockdata.is(Blocks.JIGSAW)) {
            String s = definedstructure_blockinfo1.nbt.getString("final_state");

            IBlockData iblockdata1;

            try {
                ArgumentBlock.a argumentblock_a = ArgumentBlock.parseForBlock((IRegistry) IRegistry.BLOCK, s, true);

                iblockdata1 = argumentblock_a.blockState();
            } catch (CommandSyntaxException commandsyntaxexception) {
                throw new RuntimeException(commandsyntaxexception);
            }

            return iblockdata1.is(Blocks.STRUCTURE_VOID) ? null : new DefinedStructure.BlockInfo(definedstructure_blockinfo1.pos, iblockdata1, (NBTTagCompound) null);
        } else {
            return definedstructure_blockinfo1;
        }
    }

    @Override
    protected DefinedStructureStructureProcessorType<?> getType() {
        return DefinedStructureStructureProcessorType.JIGSAW_REPLACEMENT;
    }
}
