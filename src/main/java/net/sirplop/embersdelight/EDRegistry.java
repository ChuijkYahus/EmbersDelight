package net.sirplop.embersdelight;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.util.EmbersTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.rekindled.embers.item.EmberStorageItem;
import net.sirplop.embersdelight.block.CutterBlock;
import net.sirplop.embersdelight.block.PigTailCropBlock;
import net.sirplop.embersdelight.block.PlumpHelmetCropBlock;
import net.sirplop.embersdelight.blockentity.CutterBottomBlockEntity;
import net.sirplop.embersdelight.blockentity.CutterTopBlockEntity;
import net.sirplop.embersdelight.item.ClockworkKnifeItem;
import net.sirplop.embersdelight.item.TooltipBlockItem;
import vectorwing.farmersdelight.common.block.RiceBaleBlock;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public class EDRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EmbersDelight.MODID);
    public static final DeferredRegister<Block> BLOCKS =  DeferredRegister.create(ForgeRegistries.BLOCKS, EmbersDelight.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EmbersDelight.MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EmbersDelight.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EmbersDelight.MODID);

    //farmer's delight helper methods
    public static Item.Properties basicItem() {
        return ModItems.basicItem();
    }
    public static Item.Properties foodItem(FoodProperties food) {
        return ModItems.foodItem(food);
    }
    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return ModItems.bowlFoodItem(food);
    }
    public static Item.Properties drinkItem() {
        return ModItems.drinkItem();
    }

    //tools

    public static final RegistryObject<Item> SILVER_KNIFE = ITEMS.register("silver_knife",
            () -> new KnifeItem(EmbersTiers.SILVER, 0.5F, -2.0F, basicItem().stacksTo(1)));
    public static final RegistryObject<Item> DAWNSTONE_KNIFE = ITEMS.register("dawnstone_knife",
            () -> new KnifeItem(EmbersTiers.DAWNSTONE, 0.5F, -2.0F, basicItem().stacksTo(1)));
    //public static final RegistryObject<Item> CLOCKWORK_KNIFE = ITEMS.register("clockwork_knife",
    //        () -> new ClockworkKnifeItem(EmbersTiers.CLOCKWORK_PICK, 0.5F, -2.0F, basicItem().stacksTo(1)));

    //blocks
    public static final RegistryObject<Block> CUTTER = registerBlock("cutter", () -> new CutterBlock(BlockBehaviour.Properties.copy(RegistryManager.MELTER.get()).requiresCorrectToolForDrops().sound(EmbersSounds.MACHINE), EmbersSounds.MULTIBLOCK_EXTRA));
    public static final RegistryObject<Block> PLUMP_HELMET_CRATE = registerBlock("plump_helmet_crate", () -> new Block(BlockBehaviour.Properties.copy(ModBlocks.POTATO_CRATE.get())));
    public static final RegistryObject<Block> PIG_TAIL_BALE = registerBlock("pig_tail_bale", () -> new RiceBaleBlock(BlockBehaviour.Properties.copy(ModBlocks.RICE_BALE.get())));


    //crops
    public static final RegistryObject<Block> PLUMP_HELMET_CROP = BLOCKS.register("plump_helmets",
            () -> new PlumpHelmetCropBlock(Block.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> PIG_TAIL_CROP = BLOCKS.register("pig_tail",
            () -> new PigTailCropBlock(Block.Properties.copy(Blocks.WHEAT)));

    public static final RegistryObject<Item> PLUMP_HELMET_SEED = ITEMS.register("plump_helmet_seed", () -> new TooltipBlockItem(PLUMP_HELMET_CROP.get(), basicItem(), "embersdelight.tooltip.dark_crop", ChatFormatting.DARK_GRAY, false));
    public static final RegistryObject<Item> PIG_TAIL_SEED = ITEMS.register("pig_tail_seed", () -> new TooltipBlockItem(PIG_TAIL_CROP.get(), basicItem(), "embersdelight.tooltip.dark_crop", ChatFormatting.DARK_GRAY, false));
    public static final RegistryObject<Item> PLUMP_HELMET = ITEMS.register("plump_helmet", () -> new Item(foodItem(EDFoodValues.PLUMP_HELMET)));
    public static final RegistryObject<Item> PIG_TAIL = ITEMS.register("pig_tail", () -> new Item(basicItem()));

    //Ingredients / intermediary foods
    public static final RegistryObject<Item> PLUMP_HELMET_CUT = ITEMS.register("plump_helmet_cut", () -> new Item(foodItem(EDFoodValues.PLUMP_HELMET_CUT)));
    public static final RegistryObject<Item> TRANSMOG_MEAT = ITEMS.register("transmog_meat", () -> new Item(foodItem(EDFoodValues.TRANSMOG_MEAT)));
    public static final RegistryObject<Item> COOKED_TRANSMOG_MEAT = ITEMS.register("cooked_transmog_meat", () -> new Item(foodItem(EDFoodValues.COOKED_TRANSMOG_MEAT)));

    //Foods
    public static final RegistryObject<Item> EMBER_GRITS = ITEMS.register("ember_grits", () -> new Item(bowlFoodItem(EDFoodValues.EMBER_GRITS)));
    public static final RegistryObject<Item> CINDER_DONUT = ITEMS.register("cinder_donut", () -> new Item(foodItem(EDFoodValues.CINDER_DONUT)));
    public static final RegistryObject<Item> ROCK_CANDY = ITEMS.register("rock_candy", () -> new Item(foodItem(EDFoodValues.ROCK_CANDY)));
    public static final RegistryObject<Item> GILDED_GREENS = ITEMS.register("gilded_greens", () -> new ConsumableItem(bowlFoodItem(EDFoodValues.GILDED_GREENS), true));
    public static final RegistryObject<Item> STUFFED_HELMET = ITEMS.register("stuffed_helmet", () -> new ConsumableItem(foodItem(EDFoodValues.STUFFED_HELMET), true));
    public static final RegistryObject<Item> PLUMP_ROAST = ITEMS.register("plump_roast", () -> new Item(bowlFoodItem(EDFoodValues.PLUMP_ROAST)));
    public static final RegistryObject<Item> SPICY_MEATBALLS = ITEMS.register("spicy_meatballs", () -> new ConsumableItem(bowlFoodItem(EDFoodValues.SPICY_MEATBALLS), true));
    public static final RegistryObject<Item> FIVE_FUNGUS_FAJITA = ITEMS.register("five_fungus_fajita", () -> new ConsumableItem(foodItem(EDFoodValues.FIVE_FUNGUS_FAJITA), true));

    //block entities
    public static final RegistryObject<BlockEntityType<CutterBottomBlockEntity>> CUTTER_BOTTOM_ENTITY = BLOCK_ENTITY_TYPES.register("cutter_bottom_block_entity", () -> BlockEntityType.Builder.of(CutterBottomBlockEntity::new, CUTTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<CutterTopBlockEntity>> CUTTER_TOP_ENTITY = BLOCK_ENTITY_TYPES.register("cutter_top_block_entity", () -> BlockEntityType.Builder.of(CutterTopBlockEntity::new, CUTTER.get()).build(null));


    //Creative Tabs
    public static final RegistryObject<CreativeModeTab> ED_TAB = CREATIVE_MODE_TAB.register("aetherworks_tab",
            () -> CreativeModeTab.builder()
                .icon(() -> new ItemStack(EDRegistry.EMBER_GRITS.get()))
                .title(Component.translatable("itemgroup." + EmbersDelight.MODID))
                .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                .displayItems((params, output) -> {
                    for (RegistryObject<Item> item : ITEMS.getEntries()) {
                        output.accept(item.get());

                        if (item.get() instanceof EmberStorageItem)
                            output.accept(EmberStorageItem.withFill(item.get(), ((EmberStorageItem) item.get()).getCapacity()));
                    }
                })
                .build());

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block)
    {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {
        RegistryObject<T> ret = BLOCKS.register(name, block);
        registerBlockItem(name, ret);
        return ret;
    }
}
