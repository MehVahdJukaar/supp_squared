package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class LightableLanternBlock extends LanternBlock implements ILightable {
    public final VoxelShape shapeDown;
    public final VoxelShape shapeUp;

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public LightableLanternBlock(Properties properties, VoxelShape shape) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(LIT, true)
                .setValue(HANGING, false));
        this.shapeDown = shape;
        this.shapeUp = shapeDown.move(0, 14 / 16f - shape.bounds().maxY, 0);
    }

    public LightableLanternBlock(Properties properties) {
        this(properties, Shapes.or(Block.box(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D),
                Block.box(6.0D, 8.0D, 6.0D, 10.0D, 9.0D, 10.0D)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        return state.getValue(HANGING) ? shapeUp : shapeDown;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!state.getValue(WATERLOGGED)) {
            return this.lightableInteractWithPlayerItem(state, level, pos, player, hand, stack);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }


    @Override
    public boolean isLitUp(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LIT);
    }

    @Override
    public void setLitUp(BlockState state, LevelAccessor world, BlockPos pos, @Nullable Entity entity, boolean lit) {
        world.setBlock(pos, state.setValue(LIT, lit), 3);
    }
}
