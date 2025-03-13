package net.sirplop.embersdelight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DarknessCropBlock extends CropBlock {
    public DarknessCropBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        return (pLevel.getRawBrightness(pPos, 0) <= 8 || !pLevel.canSeeSky(pPos.above())) &&
                pState.getBlock() == this ? pLevel.getBlockState(blockpos).canSustainPlant(pLevel, blockpos, Direction.UP, this) : this.mayPlaceOn(pLevel.getBlockState(blockpos), pLevel, blockpos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.getBlock() instanceof FarmBlock;
    }
}
