package net.sirplop.embersdelight.block;

import com.rekindled.embers.block.DoubleTallMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.blockentity.CutterTopBlockEntity;

public class CutterBlock extends DoubleTallMachineBlock {

    protected static final VoxelShape BASE_AABB = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(1, 4, 1, 15, 15, 15),
            Block.box(1, 15, 1, 1, 16, 4),
            Block.box(12, 15, 1, 15, 16, 4),
            Block.box(1, 15, 12, 4, 16, 15),
            Block.box(12, 15, 12, 15, 16, 15)
    );
    protected static final VoxelShape TOP_AABB = Shapes.or(
            Block.box(0, 12, 0, 16, 16, 16),
            Block.box(1, 4, 1, 15, 12, 15),
            Block.box(1, 0, 1, 1, 4, 4),
            Block.box(12, 0, 1, 15, 4, 4),
            Block.box(1, 0, 12, 4, 4, 15),
            Block.box(12, 0, 12, 15, 4, 15),
            Block.box(5, 5, 0, 11, 11, 16),
            Block.box(0, 5, 5, 16, 11, 11)
    );

    public CutterBlock(BlockBehaviour.Properties properties, SoundType topSound) {
        super(properties, topSound);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? BASE_AABB : TOP_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if (pState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER)
            return EDRegistry.CUTTER_BOTTOM_ENTITY.get().create(pPos, pState);
        return EDRegistry.CUTTER_TOP_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) != DoubleBlockHalf.LOWER)
            return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, EDRegistry.CUTTER_TOP_ENTITY.get(), CutterTopBlockEntity::clientTick) : createTickerHelper(pBlockEntityType, EDRegistry.CUTTER_TOP_ENTITY.get(), CutterTopBlockEntity::serverTick);
        return null;
    }
}
