package net.sirplop.embersdelight.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.EmbersDelight;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.concurrent.CompletableFuture;

public class EDItemTags  extends ItemTagsProvider {
    public EDItemTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, EmbersDelight.MODID, existingFileHelper);
    }
    public static final TagKey<Item> RAW_MEAT = ItemTags.create(ResourceLocation.tryBuild("forge", "foods/raw_meat"));
    public static final TagKey<Item> COOKED_MEAT = ItemTags.create(ResourceLocation.tryBuild("forge", "foods/cooked_meat"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(RAW_MEAT).addTags(
                ForgeTags.RAW_BACON,
                ForgeTags.RAW_BEEF,
                ForgeTags.RAW_CHICKEN,
                ForgeTags.RAW_FISHES,
                ForgeTags.RAW_MUTTON,
                ForgeTags.RAW_PORK
        ).add(EDRegistry.TRANSMOG_MEAT.get());
        tag(COOKED_MEAT).addTags(
          ForgeTags.COOKED_BACON,
          ForgeTags.COOKED_BEEF,
          ForgeTags.COOKED_CHICKEN,
          ForgeTags.COOKED_FISHES,
          ForgeTags.COOKED_MUTTON,
          ForgeTags.COOKED_PORK
        ).add(EDRegistry.COOKED_TRANSMOG_MEAT.get());

        tag(ForgeTags.RAW_BACON).add(EDRegistry.TRANSMOG_MEAT.get());
        tag(ForgeTags.RAW_BEEF).add(EDRegistry.TRANSMOG_MEAT.get());
        tag(ForgeTags.RAW_CHICKEN).add(EDRegistry.TRANSMOG_MEAT.get());
        tag(ForgeTags.RAW_FISHES).add(EDRegistry.TRANSMOG_MEAT.get());
        tag(ForgeTags.RAW_FISHES_COD).add(EDRegistry.TRANSMOG_MEAT.get());
        tag(ForgeTags.RAW_FISHES_SALMON).add(EDRegistry.TRANSMOG_MEAT.get());
        tag(ForgeTags.RAW_FISHES_TROPICAL).add(EDRegistry.TRANSMOG_MEAT.get());
        tag(ForgeTags.RAW_MUTTON).add(EDRegistry.TRANSMOG_MEAT.get());
        tag(ForgeTags.RAW_PORK).add(EDRegistry.TRANSMOG_MEAT.get());

        tag(ForgeTags.COOKED_BACON).add(EDRegistry.COOKED_TRANSMOG_MEAT.get());
        tag(ForgeTags.COOKED_BEEF).add(EDRegistry.COOKED_TRANSMOG_MEAT.get());
        tag(ForgeTags.COOKED_CHICKEN).add(EDRegistry.COOKED_TRANSMOG_MEAT.get());
        tag(ForgeTags.COOKED_FISHES).add(EDRegistry.COOKED_TRANSMOG_MEAT.get());
        tag(ForgeTags.COOKED_FISHES_COD).add(EDRegistry.COOKED_TRANSMOG_MEAT.get());
        tag(ForgeTags.COOKED_FISHES_SALMON).add(EDRegistry.COOKED_TRANSMOG_MEAT.get());
        tag(ForgeTags.COOKED_MUTTON).add(EDRegistry.COOKED_TRANSMOG_MEAT.get());
        tag(ForgeTags.COOKED_PORK).add(EDRegistry.COOKED_TRANSMOG_MEAT.get());

        tag(Tags.Items.MUSHROOMS).add(EDRegistry.PLUMP_HELMET.get(), EDRegistry.PLUMP_HELMET_CUT.get());

        tag(ForgeTags.TOOLS_KNIVES).add(EDRegistry.SILVER_KNIFE.get(), EDRegistry.DAWNSTONE_KNIFE.get()/*, EDRegistry.CLOCKWORK_KNIFE.get()*/);

    }
}
