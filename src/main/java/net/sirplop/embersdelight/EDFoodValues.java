package net.sirplop.embersdelight;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class EDFoodValues
{
    public static final int BRIEF_DURATION = FoodValues.BRIEF_DURATION;    // 30 seconds
    public static final int SHORT_DURATION = FoodValues.SHORT_DURATION;    // 1 minute
    public static final int MEDIUM_DURATION = FoodValues.MEDIUM_DURATION;    // 3 minutes
    public static final int LONG_DURATION = FoodValues.LONG_DURATION;    // 5 minutes

    // Raw Crops
    public static final FoodProperties PLUMP_HELMET = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.4f).build();

    //Ingredients
    public static final FoodProperties PLUMP_HELMET_CUT = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.3f).build();
    public static final FoodProperties TRANSMOG_MEAT = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.4f)
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 1), 0.8F).build();

    // Basic Foods
    public static final FoodProperties EMBER_GRITS = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.4f).build();
    public static final FoodProperties PLUMP_ROAST = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(0.5f).build();
    public static final FoodProperties COOKED_TRANSMOG_MEAT = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(0.6f).build();

    public static final FoodProperties ROCK_CANDY = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.4f).fast().build();

    // Drinks (mostly for effects)

    //Handheld Foods
    public static final FoodProperties CINDER_DONUT = (new FoodProperties.Builder())
            .nutrition(6).saturationMod(0.5f).build();
    public static final FoodProperties GILDED_GREENS = (new FoodProperties.Builder())
            .nutrition(8).saturationMod(1.25f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 0), 1.0F).build();

    //Plated Foods
    public static final FoodProperties STUFFED_HELMET = (new FoodProperties.Builder())
            .nutrition(12).saturationMod(0.75f)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), SHORT_DURATION, 0), 1.0F).build();
    public static final FoodProperties SPICY_MEATBALLS = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.75F)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 3600, 0), 1.0F).build();
    public static final FoodProperties FIVE_FUNGUS_FAJITA = (new FoodProperties.Builder())
            .nutrition(14).saturationMod(0.75f)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();

    //Feast Portions
}
