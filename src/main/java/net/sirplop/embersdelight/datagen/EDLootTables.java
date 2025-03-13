package net.sirplop.embersdelight.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class EDLootTables extends LootTableProvider {
    public EDLootTables(PackOutput output) {
        super(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(EDBlockLootTables::new, LootContextParamSets.BLOCK)
                //new LootTableProvider.SubProviderEntry(EmbersEntityLootTables::new, LootContextParamSets.ENTITY)
        ));
    }
}