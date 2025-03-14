package net.sirplop.embersdelight.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.EmbersDelight;

public class EDSounds extends SoundDefinitionsProvider {
    public EDSounds(PackOutput output, ExistingFileHelper helper) {
        super(output, EmbersDelight.MODID, helper);
    }

    //this is just here so the class loads, nothing else needs to happen here
    public static void init() {}

    //Sounds
    public static final RegistryObject<SoundEvent> CUTTER_STOP = registerSoundEvent("block.cutter.stop");
    public static final RegistryObject<SoundEvent> CUTTER_LOOP = registerSoundEvent("block.cutter.loop");

    public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return EDRegistry.SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(resource(name)));
    }
    @Override
    public void registerSounds() {
        withSubtitle(CUTTER_STOP, definition().with(sound(resource("cutter_stop"))));
        withSubtitle(CUTTER_LOOP, definition().with(sound(resource("cutter_loop"))));
    }


    public void withSubtitle(RegistryObject<SoundEvent> soundEvent, SoundDefinition definition) {
        add(soundEvent, definition.subtitle("subtitles." + EmbersDelight.MODID + "." + soundEvent.getId().getPath()));
    }
    public static ResourceLocation resource(String path) { return ResourceLocation.tryBuild(EmbersDelight.MODID, path); }
}