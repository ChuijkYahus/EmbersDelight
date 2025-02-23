package net.sirplop.embersdelight;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.rekindled.embers.item.EmberStorageItem;

public class EDRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EmbersDelight.MODID);
    public static final DeferredRegister<Block> BLOCKS =  DeferredRegister.create(ForgeRegistries.BLOCKS, EmbersDelight.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EmbersDelight.MODID);


    public static final RegistryObject<Item> PLUMP_HELMET = ITEMS.register("plump_helmet", () -> new Item(new Item.Properties()));

    //Creative Tabs
    public static final RegistryObject<CreativeModeTab> AW_TAB = CREATIVE_MODE_TAB.register("aetherworks_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(EDRegistry.PLUMP_HELMET.get()))
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


}
