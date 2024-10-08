package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.moonlight.api.block.IColored;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ColoredSackBlock extends SackBlock implements IColored {
    private final DyeColor color;

    public ColoredSackBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter reader, BlockPos pos) {
        return color.getMapColor().col;
    }

    @Override
    public @Nullable DyeColor getColor() {
        return color;
    }

    @Override
    public boolean supportsBlankColor() {
        return true;
    }
}
