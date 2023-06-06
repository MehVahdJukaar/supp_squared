package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.RegUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PlaqueBlock extends WaterBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE_NORTH = Block.box(0, 3, 15, 16, 13, 16);
    public static final VoxelShape SHAPE_SOUTH = Utils.rotateVoxelShape(SHAPE_NORTH, Direction.SOUTH);
    public static final VoxelShape SHAPE_EAST = Utils.rotateVoxelShape(SHAPE_NORTH, Direction.EAST);
    public static final VoxelShape SHAPE_WEST = Utils.rotateVoxelShape(SHAPE_NORTH, Direction.WEST);

    public PlaqueBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = level.getBlockState(blockPos);
        VoxelShape faceShape = blockState.getBlockSupportShape(level, pos).getFaceShape(direction);
        var axis = direction.getAxis();
        if(direction.getAxisDirection() == Direction.AxisDirection.POSITIVE){
            return faceShape.max(axis) == 1;
        }else   return faceShape.min(axis) == 0;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;

        BlockState blockState = this.defaultBlockState();
        LevelReader levelReader = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Direction[] directions = context.getNearestLookingDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.setValue(FACING, direction2);
                if (blockState.canSurvive(levelReader, blockPos)) {
                    return blockState.setValue(WATERLOGGED, flag);
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlaqueBlockTile(pos, state);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof PlaqueBlockTile tile && tile.isAccessibleBy(player)) {
                InteractionResult result = tile.getTextHolder().playerInteract(level, pos, player, handIn, tile);
                if (result != InteractionResult.PASS) return result;

                //place item
                if (handIn == InteractionHand.MAIN_HAND) {
                    // open gui (edit sign with empty hand)
                    tile.sendOpenGuiPacket(level, pos, player);
                    return InteractionResult.CONSUME;
                }
            }
            return InteractionResult.PASS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing == stateIn.getValue(FACING) && !this.canSurvive(stateIn, worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
}
