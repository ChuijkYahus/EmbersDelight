package net.sirplop.embersdelight.datagen;

import com.rekindled.embers.Embers;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.datagen.EmbersRecipes;
import com.rekindled.embers.recipe.AlchemyRecipeBuilder;
import com.rekindled.embers.recipe.StampingRecipeBuilder;
import com.rekindled.embers.util.ConsumerWrapperBuilder;
import com.rekindled.embers.util.FluidAmounts;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.EmbersDelight;
import net.sirplop.embersdelight.compat.AetherworksCompat;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;
import vectorwing.farmersdelight.data.recipe.CookingRecipes;

import java.util.function.Consumer;

import static com.rekindled.embers.datagen.EmbersRecipes.stampingFolder;
import static vectorwing.farmersdelight.data.recipe.CookingRecipes.*;

public class EDRecipes  extends RecipeProvider implements IConditionBuilder {

    public EDRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

        //alchemy
        AlchemyRecipeBuilder.create(EDRegistry.PLUMP_HELMET.get()).tablet(ModItems.BROWN_MUSHROOM_COLONY.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Items.RED_MUSHROOM, RegistryManager.ASH.get(), RegistryManager.EMBER_GRIT.get())
                .aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(EDRegistry.PLUMP_HELMET.get()).id(getResource("plump_helmet_alt")).tablet(ModItems.RED_MUSHROOM_COLONY.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Items.BROWN_MUSHROOM, RegistryManager.ASH.get(), RegistryManager.EMBER_GRIT.get())
                .aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(EDRegistry.PIG_TAIL.get()).tablet(ModItems.RICE_PANICLE.get()).folder(EmbersRecipes.alchemyFolder)
                .inputs(Items.STRING, RegistryManager.ASH.get(), RegistryManager.EMBER_GRIT.get())
                .aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(EDRegistry.TRANSMOG_MEAT.get()).tablet(Ingredient.of(EDItemTags.RAW_MEAT)).folder(EmbersRecipes.alchemyFolder)
                .inputs(Items.BONE, RegistryManager.ASH.get(), RegistryManager.EMBER_GRIT.get())
                .aspects(EmbersItemTags.DAWNSTONE_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS).save(consumer);

        //stamper
        StampingRecipeBuilder.create(new ItemStack(EDRegistry.GILDED_GREENS.get(), 1)).domain(EmbersDelight.MODID).folder(stampingFolder).stamp(RegistryManager.NUGGET_STAMP.get()).input(ModItems.MIXED_SALAD.get()).fluid(RegistryManager.MOLTEN_GOLD.FLUID.get(), 8 * FluidAmounts.NUGGET_AMOUNT).save(ConsumerWrapperBuilder.wrap().build(consumer));

        //Smelting
        foodSmeltingRecipes("transmog_meat", EDRegistry.TRANSMOG_MEAT.get(), EDRegistry.COOKED_TRANSMOG_MEAT.get(), 0.35F, consumer);

        //crafting
        storageBlock(consumer, "has_plump_helmet", EDRegistry.PLUMP_HELMET, EDRegistry.PLUMP_HELMET_CRATE);
        storageBlock(consumer, "has_pig_tail", EDRegistry.PIG_TAIL, EDRegistry.PIG_TAIL_BALE);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, EDRegistry.PLUMP_HELMET_SEED.get(), 1)
                .requires(EDRegistry.PLUMP_HELMET.get())
                .unlockedBy("has_plump_helmet", has(EDRegistry.PLUMP_HELMET.get()))
                .save(consumer, getResource("plump_helmet_to_seed"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.MUSHROOM_STEW, 1)
                .requires(Ingredient.of(EDRegistry.PLUMP_HELMET.get(), EDRegistry.PLUMP_HELMET_CUT.get()))
                .requires(Ingredient.of(EDRegistry.PLUMP_HELMET.get(), EDRegistry.PLUMP_HELMET_CUT.get()))
                .requires(Items.BOWL)
                .unlockedBy("has_plump_helmet", has(EDRegistry.PLUMP_HELMET.get()))
                .save(consumer, getResource("plump_mushroom_stew"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.MUSHROOM_STEW, 1)
                .requires(Ingredient.of(EDRegistry.PLUMP_HELMET.get(), EDRegistry.PLUMP_HELMET_CUT.get()))
                .requires(Ingredient.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
                .requires(Items.BOWL)
                .unlockedBy("has_plump_helmet", has(EDRegistry.PLUMP_HELMET.get()))
                .save(consumer, getResource("plump_mushroom_stew_2"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.RABBIT_STEW, 1)
                .requires(Items.BAKED_POTATO)
                .requires(Ingredient.of(Items.COOKED_RABBIT, EDRegistry.COOKED_TRANSMOG_MEAT.get()))
                .requires(Items.BOWL)
                .requires(Items.CARROT)
                .requires(Ingredient.of(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, EDRegistry.PLUMP_HELMET.get(), EDRegistry.PLUMP_HELMET_CUT.get()))
                .unlockedBy("has_plump_helmet", has(EDRegistry.PLUMP_HELMET.get()))
                .save(consumer, getResource("rabbit_stew"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.STEAK_AND_POTATOES.get())
                .requires(Items.BAKED_POTATO)
                .requires(Ingredient.of(Items.COOKED_BEEF, EDRegistry.COOKED_TRANSMOG_MEAT.get()))
                .requires(Items.BOWL)
                .requires(ForgeTags.CROPS_ONION)
                .requires(ModItems.COOKED_RICE.get())
                .unlockedBy("has_baked_potato", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAKED_POTATO))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.ROASTED_MUTTON_CHOPS.get())
                .requires(Ingredient.of(ModItems.COOKED_MUTTON_CHOPS.get(), EDRegistry.COOKED_TRANSMOG_MEAT.get()))
                .requires(Items.BEETROOT)
                .requires(Items.BOWL)
                .requires(ModItems.COOKED_RICE.get())
                .requires(ForgeTags.CROPS_TOMATO)
                .unlockedBy("has_mutton", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COOKED_MUTTON))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BACON_AND_EGGS.get())
                .requires(Ingredient.of(ModItems.COOKED_BACON.get(), EDRegistry.COOKED_TRANSMOG_MEAT.get()))
                .requires(Ingredient.of(ModItems.COOKED_BACON.get(), EDRegistry.COOKED_TRANSMOG_MEAT.get()))
                .requires(Items.BOWL)
                .requires(ForgeTags.COOKED_EGGS)
                .requires(ForgeTags.COOKED_EGGS)
                .unlockedBy("has_bacon", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.COOKED_BACON.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.HAMBURGER.get())
                .requires(ForgeTags.BREAD)
                .requires(Ingredient.of(ModItems.BEEF_PATTY.get(), EDRegistry.COOKED_TRANSMOG_MEAT.get()))
                .requires(ForgeTags.SALAD_INGREDIENTS)
                .requires(ForgeTags.CROPS_TOMATO)
                .requires(ForgeTags.CROPS_ONION)
                .unlockedBy("has_beef_patty", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BEEF_PATTY.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, EDRegistry.FIVE_FUNGUS_FAJITA.get(), 1)
                .requires(Items.BREAD)
                .requires(Items.BROWN_MUSHROOM)
                .requires(Items.RED_MUSHROOM)
                .requires(Items.CRIMSON_FUNGUS)
                .requires(Items.WARPED_FUNGUS)
                .requires(EDRegistry.PLUMP_HELMET.get())
                .unlockedBy("has_plump_helmet", has(EDRegistry.PLUMP_HELMET.get()))
                .save(consumer, getResource("five_fungus_fajita"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, EDRegistry.CINDER_DONUT.get())
                .pattern(" G ")
                .pattern("WMW")
                .define('G', RegistryManager.EMBER_GRIT.get())
                .define('M', ForgeTags.MILK)
                .define('W', Items.WHEAT)
                .unlockedBy("has_grit", has(RegistryManager.EMBER_GRIT.get()))
                .save(consumer, getResource("cinder_donut"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EDRegistry.CUTTER.get())
                .pattern("CPC")
                .pattern("OUO")
                .pattern("CBC")
                .define('C', RegistryManager.CAMINITE_BRICKS.get())
                .define('P', EmbersItemTags.IRON_PLATE)
                .define('O', EmbersItemTags.COPPER_PLATE)
                .define('U', Blocks.STONECUTTER)
                .define('B', Blocks.IRON_BARS)
                .unlockedBy("has_caminite_bricks", has(RegistryManager.CAMINITE_BRICKS.get()))
                .save(consumer, getResource("cutter"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EDRegistry.SILVER_KNIFE.get())
                .pattern("I")
                .pattern("S")
                .define('I', EmbersItemTags.SILVER_INGOT)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_silver", has(EmbersItemTags.SILVER_INGOT))
                .save(consumer, getResource("silver_knife"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EDRegistry.DAWNSTONE_KNIFE.get())
                .pattern("I")
                .pattern("S")
                .define('I', EmbersItemTags.DAWNSTONE_INGOT)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_dawnstone", has(EmbersItemTags.DAWNSTONE_INGOT))
                .save(consumer, getResource("dawnstone_knife"));
        /*ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EDRegistry.CLOCKWORK_KNIFE.get())
                .pattern("EPC")
                .pattern(" S ")
                .define('E', RegistryManager.EMBER_SHARD.get())
                .define('P', EmbersItemTags.DAWNSTONE_PLATE)
                .define('C', Items.COPPER_INGOT)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_charger", has(RegistryManager.COPPER_CHARGER.get()))
                .save(consumer, getResource("clockwork_knife"));
         */

        //cooking
        CookingPotRecipeBuilder.cookingPotRecipe(EDRegistry.EMBER_GRITS.get(), 1, FAST_COOKING, SMALL_EXP)
                .addIngredient(ForgeTags.MILK)
                .addIngredient(RegistryManager.EMBER_GRIT.get())
                .unlockedByAnyIngredient(RegistryManager.EMBER_GRIT.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .build(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(EDRegistry.STUFFED_HELMET.get(), 1, NORMAL_COOKING, MEDIUM_EXP, EDRegistry.PLUMP_HELMET.get())
                .addIngredient(ForgeTags.MILK)
                .addIngredient(ForgeTags.COOKED_BEEF)
                .addIngredient(ForgeTags.VEGETABLES_TOMATO)
                .unlockedByAnyIngredient(EDRegistry.PLUMP_HELMET.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(EDRegistry.SPICY_MEATBALLS.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
                .addIngredient(Ingredient.of(ModItems.MINCED_BEEF.get(), (EDRegistry.TRANSMOG_MEAT.get())))
                .addIngredient(ForgeTags.PASTA)
                .addIngredient(ModItems.TOMATO_SAUCE.get())
                .addIngredient(RegistryManager.EMBER_GRIT.get())
                .addIngredient(RegistryManager.EMBER_GRIT.get())
                .addIngredient(RegistryManager.EMBER_GRIT.get())
                .unlockedByAnyIngredient(ModItems.RAW_PASTA.get(), Items.BEEF, ModItems.TOMATO_SAUCE.get(), RegistryManager.EMBER_GRIT.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(EDRegistry.ROCK_CANDY.get(), 8, SLOW_COOKING, MEDIUM_EXP)
                .addIngredient(Tags.Items.COBBLESTONE)
                .addIngredient(Items.SUGAR)
                .addIngredient(RegistryManager.EMBER_GRIT.get())
                .unlockedByAnyIngredient(RegistryManager.EMBER_GRIT.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(EDRegistry.PLUMP_ROAST.get(), 1, SLOW_COOKING, MEDIUM_EXP)
                .addIngredient(EDRegistry.PLUMP_HELMET.get())
                .addIngredient(Items.POTATO)
                .addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
                .unlockedByAnyIngredient(EDRegistry.PLUMP_HELMET.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(Items.RABBIT_STEW, 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(Items.BAKED_POTATO)
                .addIngredient(Ingredient.of(Items.RABBIT, EDRegistry.TRANSMOG_MEAT.get()))
                .addIngredient(Items.CARROT)
                .addIngredient(Ingredient.of(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, EDRegistry.PLUMP_HELMET.get(), EDRegistry.PLUMP_HELMET_CUT.get()))
                .unlockedByAnyIngredient(Items.RABBIT, Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, EDRegistry.PLUMP_HELMET.get(), Items.CARROT, Items.BAKED_POTATO)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(AetherworksCompat.AETHER_COLADA.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.GLASS_BOTTLE)
                .addIngredient(Items.SUGAR)
                .addIngredient(ForgeTags.MILK)
                .addIngredient(Items.APPLE)
                .addIngredient(Items.COCOA_BEANS)
                .addIngredient(AWRegistry.AETHER_SHARD.get())
                .unlockedByAnyIngredient(AWRegistry.AETHER_SHARD.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(consumer);

        //cutting
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(EDRegistry.PIG_TAIL.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.STRING, 1)
                .addResult(ModItems.STRAW.get())
                .addResult(EDRegistry.PIG_TAIL_SEED.get())
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(EDRegistry.PLUMP_HELMET.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), EDRegistry.PLUMP_HELMET_CUT.get(), 2)
                .build(consumer);
    }

    public static ResourceLocation getResource(String name) {
        return ResourceLocation.tryBuild(EmbersDelight.MODID, name);
    }
    private static void foodSmeltingRecipes(String name, ItemLike ingredient, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        String namePrefix = ResourceLocation.tryBuild(EmbersDelight.MODID, name).toString();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 200)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
                .save(consumer);
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 600)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
                .save(consumer, namePrefix + "_from_campfire_cooking");
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 100)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
                .save(consumer, namePrefix + "_from_smoking");
    }
    private static void storageBlock(Consumer<FinishedRecipe> consumer, String triggerName, RegistryObject<? extends ItemLike> item, RegistryObject<? extends ItemLike> block) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, block.get(), 1)
                .requires(item.get(), 9)
                .unlockedBy(triggerName, has(item.get()))
                .save(consumer, getResource(item.getId().getPath() + "_to_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, item.get(), 9)
                .requires(block.get(), 1)
                .unlockedBy(triggerName, has(item.get()))
                .save(consumer, getResource(block.getId().getPath() + "_to_item"));
    }
}
