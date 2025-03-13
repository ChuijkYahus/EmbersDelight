package net.sirplop.embersdelight;

import com.mojang.logging.LogUtils;
import com.rekindled.embers.datagen.EmbersSounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sirplop.embersdelight.blockentity.render.CutterTopBlockEntityRenderer;
import net.sirplop.embersdelight.client.EDClientEvents;
import net.sirplop.embersdelight.compat.AetherworksCompat;
import net.sirplop.embersdelight.datagen.*;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EmbersDelight.MODID)
public class EmbersDelight
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "embersdelight";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public EmbersDelight(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);

        EDRegistry.BLOCKS.register(modEventBus);
        EDRegistry.ITEMS.register(modEventBus);
        EDRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        EDRegistry.SOUND_EVENTS.register(modEventBus);
        EDRegistry.CREATIVE_MODE_TAB.register(modEventBus);
        EDSounds.init();

        EDConfig.register();

        if (ModList.get().isLoaded("aetherworks")) {
            AetherworksCompat.init();
        }

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Stuffing face with Ember Crystals... Yummy!");
        event.enqueueWork(EDResearch::initResearch);
    }


    public void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if (event.includeClient()) {
            gen.addProvider(true, new EDItemModels(output, existingFileHelper));
            gen.addProvider(true, new EDBlockStates(output, existingFileHelper));
            gen.addProvider(true, new EDSounds(output, existingFileHelper));
        }
        if (event.includeServer()) {
            gen.addProvider(true, new EDLootTables(output));
            gen.addProvider(true, new EDRecipes(output));
            BlockTagsProvider blockTags = new EDBlockTags(output, lookupProvider, existingFileHelper);
            gen.addProvider(true, blockTags);
            gen.addProvider(true, new EDItemTags(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        @SuppressWarnings("removal")
        public static void clientSetup(FMLClientSetupEvent event) {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            modEventBus.addListener(EDClientEvents::afterModelBake);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(EDRegistry.CUTTER_TOP_ENTITY.get(), CutterTopBlockEntityRenderer::new);
        }
    }
}
