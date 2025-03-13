package net.sirplop.embersdelight.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.EmbersDelight;
import net.sirplop.embersdelight.block.PigTailCropBlock;
import net.sirplop.embersdelight.block.PlumpHelmetCropBlock;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class EDBlockStates extends BlockStateProvider {
    public EDBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EmbersDelight.MODID, exFileHelper);
    }

    private static final int DEFAULT_ANGLE_OFFSET = 180;

    @Override
    protected void registerStatesAndModels() {

        this.customStageBlock(EDRegistry.PLUMP_HELMET_CROP.get(), resourceBlock("crop_wide_center"), "crop", PlumpHelmetCropBlock.AGE, Arrays.asList(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageBlock(EDRegistry.PIG_TAIL_CROP.get(), mcLoc("crop"), "crop", PigTailCropBlock.AGE, new ArrayList<>());

        crateBlock(EDRegistry.PLUMP_HELMET_CRATE, "plump_helmet");
        ModelFile pigTail = models().cubeBottomTop(blockName(EDRegistry.PIG_TAIL_BALE.get()),
                resourceBlock("pig_tail_bale_side"), resourceBlock("pig_tail_bale_bottom"),
                resourceBlock("pig_tail_bale_top"));
        simpleBlockItem(EDRegistry.PIG_TAIL_BALE.get(), pigTail);
        customDirectionalBlock(EDRegistry.PIG_TAIL_BALE.get(), (state) -> pigTail);

        ModelFile.ExistingModelFile cutterBottomModel = models().getExistingFile(ResourceLocation.tryBuild(EmbersDelight.MODID, "cutter_bottom"));
        simpleBlockItem(EDRegistry.CUTTER.get(), cutterBottomModel);
        getVariantBuilder(EDRegistry.CUTTER.get()).forAllStates(state -> {
            return ConfiguredModel.builder()
                    .modelFile(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? cutterBottomModel : models().getExistingFile(resourceBlock("cutter_top")))
                    .uvLock(false)
                    .build();
        });
    }

    private String blockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }
    public ResourceLocation resourceBlock(String path) {
        return ResourceLocation.tryBuild(EmbersDelight.MODID, "block/" + path);
    }
    public ResourceLocation resourceBlock(String path, String modid) {
        return ResourceLocation.fromNamespaceAndPath(modid, "block/" + path);
    }

    // "I am not proud of this method... But hey, it's runData. Only I shall have to deal with it."
    // HAHA, YOU FOOL, VECTORWING! NOW I AM DEALING WITH IT DIRECTLY! HAHAHAHAHAHAHAHAHA!
    public void customStageBlock(Block block, @Nullable ResourceLocation parent, String textureKey, IntegerProperty ageProperty, List<Integer> suffixes, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    int ageSuffix = state.getValue(ageProperty);
                    String stageName = "crops/"+blockName(block) + "_stage_";
                    stageName += suffixes.isEmpty() ? ageSuffix : suffixes.get(Math.min(suffixes.size(), ageSuffix));
                    if (parent == null) {
                        return ConfiguredModel.builder()
                                .modelFile(models().cross(stageName, resourceBlock(stageName)).renderType("cutout")).build();
                    }
                    return ConfiguredModel.builder()
                            .modelFile(models().singleTexture(stageName, parent, textureKey, resourceBlock(stageName)).renderType("cutout")).build();
                }, ignored);
    }

    public void blockWithItem(RegistryObject<? extends Block> registryObject) {
        //block model
        simpleBlock(registryObject.get());
        //itemblock model
        simpleBlockItem(registryObject.get(), cubeAll(registryObject.get()));
    }
    public void blockWithItem(RegistryObject<? extends Block> registryObject, String model) {
        ModelFile.ExistingModelFile modelFile = models().getExistingFile(resourceBlock(model));
        //block model
        simpleBlock(registryObject.get(), modelFile);
        //itemblock model
        simpleBlockItem(registryObject.get(), modelFile);
    }
    public void blockWithItem(RegistryObject<? extends Block> registryObject, ModelFile model) {
        //block model
        simpleBlock(registryObject.get(), model);
        //itemblock model
        simpleBlockItem(registryObject.get(), model);
    }

    public void crateBlock(RegistryObject<? extends Block> registryObject, String cropName) {
        blockWithItem(registryObject, models().cubeBottomTop(blockName(registryObject.get()),
                resourceBlock(cropName + "_crate_side"), resourceBlock("crate_bottom", FarmersDelight.MODID),
                resourceBlock(cropName + "_crate_top")));
    }

    public void customDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                            .rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
                            .build();
                }, ignored);
    }
}
