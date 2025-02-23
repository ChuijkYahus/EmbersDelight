package net.sirplop.embersdelight.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.EmbersDelight;

public class EDItemModels extends ItemModelProvider {

    public EDItemModels(PackOutput gen, ExistingFileHelper existingFileHelper) {
        super(gen, EmbersDelight.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        itemWithTexture(EDRegistry.PLUMP_HELMET, "plump_helmet");
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
