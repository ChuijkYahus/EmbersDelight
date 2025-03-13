package net.sirplop.embersdelight;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class EDConfig {

    public static ForgeConfigSpec.ConfigValue<Integer> CUTTER_PROCESS_TIME;
    public static ForgeConfigSpec.ConfigValue<Double> CUTTER_EMBER_COST;

    public static void register() {
        //registerClientConfigs();
        registerCommonConfigs();
        //registerServerConfigs();
    }

    @SuppressWarnings("removal") //bruh what's the replacement for modloadingcontext???
    public static void registerCommonConfigs() {
        ForgeConfigSpec.Builder COMMON = new ForgeConfigSpec.Builder();
        COMMON.comment("Settings for machine parameters").push("parameters");

        CUTTER_PROCESS_TIME = COMMON.comment("The time in ticks it takes to process one recipe.").define("cutter.processTime", 15);
        CUTTER_EMBER_COST = COMMON.comment("The ember cost per tick.").define("cutter.cost", 1.0);

        COMMON.pop();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON.build());
    }
}
