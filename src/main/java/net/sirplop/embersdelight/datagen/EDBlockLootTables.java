package net.sirplop.embersdelight.datagen;

import com.rekindled.embers.RegistryManager;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.EmbersDelight;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EDBlockLootTables extends BlockLootSubProvider {

    public EDBlockLootTables() {
        super(Set.of(), FeatureFlags.VANILLA_SET);
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter((block) -> EmbersDelight.MODID.equals(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getNamespace()))
                .filter((block) -> block != EDRegistry.PLUMP_HELMET_CROP.get() && block != EDRegistry.PIG_TAIL_CROP.get())
                .collect(Collectors.toList());
    }

    @Override
    protected void generate() {
        dropSelf(EDRegistry.CUTTER.get());
        dropSelf(EDRegistry.PLUMP_HELMET_CRATE.get());
        dropSelf(EDRegistry.PIG_TAIL_BALE.get());
    }
}
