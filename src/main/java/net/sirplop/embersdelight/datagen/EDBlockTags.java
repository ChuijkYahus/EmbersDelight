package net.sirplop.embersdelight.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.EmbersDelight;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.rekindled.embers.datagen.EmbersBlockTags.MECH_CORE_PROXYABLE_TOP;
import static com.rekindled.embers.datagen.EmbersBlockTags.RELOCATION_NOT_SUPPORTED;

public class EDBlockTags extends BlockTagsProvider {
    public EDBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, EmbersDelight.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.CROPS).add(
                EDRegistry.PLUMP_HELMET_CROP.get(),
                EDRegistry.PIG_TAIL_CROP.get()
        );
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                EDRegistry.CUTTER.get()
        );
        tag(RELOCATION_NOT_SUPPORTED).add(
                EDRegistry.CUTTER.get()
        );
        tag(MECH_CORE_PROXYABLE_TOP).add(
                EDRegistry.CUTTER.get());
    }
}
