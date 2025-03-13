package net.sirplop.embersdelight.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.EmbersDelight;
import net.sirplop.embersdelight.compat.AetherworksCompat;

public class EDItemModels extends ItemModelProvider {

    public EDItemModels(PackOutput gen, ExistingFileHelper existingFileHelper) {
        super(gen, EmbersDelight.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        itemWithTexture(EDRegistry.PLUMP_HELMET, "plump_helmet");
        itemWithTexture(EDRegistry.PLUMP_HELMET_SEED, "plump_helmet_seed");
        itemWithTexture(EDRegistry.PIG_TAIL, "pig_tail");
        itemWithTexture(EDRegistry.PIG_TAIL_SEED, "pig_tail_seed");

        toolWithTexture(EDRegistry.SILVER_KNIFE, "silver_knife");
        toolWithTexture(EDRegistry.DAWNSTONE_KNIFE, "dawnstone_knife");
        //toolWithTexture(EDRegistry.CLOCKWORK_KNIFE, "clockwork_knife");

        itemWithTexture(EDRegistry.EMBER_GRITS, "ember_grits");
        itemWithTexture(EDRegistry.PLUMP_HELMET_CUT, "plump_helmet_cuts");
        itemWithTexture(EDRegistry.STUFFED_HELMET, "stuffed_helmet");
        itemWithTexture(EDRegistry.CINDER_DONUT, "cinder_donut");
        itemWithTexture(EDRegistry.FIVE_FUNGUS_FAJITA, "five_fungus_fajita");
        itemWithTexture(EDRegistry.PLUMP_ROAST, "plump_roast");
        itemWithTexture(EDRegistry.GILDED_GREENS, "gilded_greens");
        itemWithTexture(EDRegistry.ROCK_CANDY, "rock_candy");
        itemWithTexture(EDRegistry.SPICY_MEATBALLS, "spicy_meatballs");
        itemWithTexture(EDRegistry.TRANSMOG_MEAT, "transmog_meat");
        itemWithTexture(EDRegistry.COOKED_TRANSMOG_MEAT, "cooked_transmog_meat");

        itemWithTexture(AetherworksCompat.AETHER_COLADA, "aether_colada");
    }

    public void itemWithTexture(RegistryObject<? extends Item> registryObject, String texture) {
        itemWithTexture(registryObject, "item/generated", texture);
    }
    public void itemWithTexture(RegistryObject<? extends Item> registryObject, String model, String... textures) {
        ResourceLocation id = registryObject.getId();
        for (int i = 0; i < textures.length; i++) {
            ResourceLocation textureLocation = ResourceLocation.tryBuild(id.getNamespace(), "item/" + textures[i]);
            singleTexture(id.getPath(), ResourceLocation.parse(model), "layer"+i, textureLocation);
        }
    }
    public void toolWithTexture(RegistryObject<? extends Item> registryObject, String... textures) {
        itemWithTexture(registryObject, "item/handheld", textures);
    }
}
