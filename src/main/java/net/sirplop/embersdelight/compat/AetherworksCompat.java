package net.sirplop.embersdelight.compat;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.aetherworks.AWRegistry;
import vectorwing.farmersdelight.common.item.DrinkableItem;

public class AetherworksCompat {

    public static final FoodProperties AETHER_COLADA_PROP = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.25f).alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0), 1.0F)
            .effect(() -> new MobEffectInstance(AWRegistry.EFFECT_MOONFIRE.get(), 40, 1), 1.0F).build();

    public static final RegistryObject<Item> AETHER_COLADA = EDRegistry.ITEMS.register("aether_colada", () -> new DrinkableItem(EDRegistry.foodItem(AETHER_COLADA_PROP), true));

    public static void init() {}
}
