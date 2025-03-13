package net.sirplop.embersdelight.client;

import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.sirplop.embersdelight.EmbersDelight;
import net.sirplop.embersdelight.blockentity.render.CutterTopBlockEntityRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class EDClientEvents {

    public static void afterModelBake(ModelEvent.BakingCompleted event) {
        ModelBakery bakery = event.getModelManager().getModelBakery();
        CutterTopBlockEntityRenderer.blades = getModel(bakery, "cutter_blades");
    }

    public static BakedModel getModel(ModelBakery bakery, String name) {
        ResourceLocation location = ResourceLocation.tryBuild(EmbersDelight.MODID, "block/" + name);
        ModelBakerImplButNotStinky bakerImpl = new ModelBakerImplButNotStinky(bakery, (modelLoc, material) -> material.sprite(), location);
        UnbakedModel model = bakery.getModel(location);
        return model.bake(bakerImpl, Material::sprite, BlockModelRotation.X0_Y0, location);
    }


    //because forge is big stinker, ModelbakeryImpl isn't public.
    //so we've taken things into our own hands.
    @OnlyIn(Dist.CLIENT)
    static class ModelBakerImplButNotStinky implements ModelBaker {
        private final Function<Material, TextureAtlasSprite> modelTextureGetter;
        final Map<BakedCacheKey, BakedModel> bakedCache = Maps.newHashMap();
        final ModelBakery bakery;

        ModelBakerImplButNotStinky(ModelBakery bakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> p_249651_, ResourceLocation p_251408_) {
            this.bakery = bakery;
            this.modelTextureGetter = (p_250859_) -> {
                return (TextureAtlasSprite)p_249651_.apply(p_251408_, p_250859_);
            };
        }

        public @NotNull UnbakedModel getModel(ResourceLocation p_248568_) {
            return bakery.getModel(p_248568_);
        }

        public @NotNull Function<Material, TextureAtlasSprite> getModelTextureGetter() {
            return this.modelTextureGetter;
        }

        public BakedModel bake(ResourceLocation p_252176_, ModelState p_249765_) {
            return this.bake(p_252176_, p_249765_, this.modelTextureGetter);
        }

        public BakedModel bake(ResourceLocation p_252176_, ModelState p_249765_, Function<Material, TextureAtlasSprite> sprites) {
            BakedCacheKey modelbakery$bakedcachekey = new BakedCacheKey(p_252176_, p_249765_.getRotation(), p_249765_.isUvLocked());
            BakedModel bakedmodel = bakedCache.get(modelbakery$bakedcachekey);
            if (bakedmodel != null) {
                return bakedmodel;
            } else {
                UnbakedModel unbakedmodel = getModel(p_252176_);
                BakedModel bakedmodel1 = unbakedmodel.bake(this, sprites, p_249765_, p_252176_);
                bakedCache.put(modelbakery$bakedcachekey, bakedmodel1);
                return bakedmodel1;
            }
        }

        @OnlyIn(Dist.CLIENT)
        record BakedCacheKey(ResourceLocation id, Transformation transformation, boolean isUvLocked) {

            public ResourceLocation id() {
                return this.id;
            }

            public Transformation transformation() {
                return this.transformation;
            }

            public boolean isUvLocked() {
                return this.isUvLocked;
            }
        }
    }
}
