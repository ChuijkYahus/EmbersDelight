package net.sirplop.embersdelight;

import com.rekindled.embers.research.ResearchBase;
import com.rekindled.embers.research.ResearchManager;
import net.minecraft.world.item.ItemStack;

public class EDResearch {

    public static ResearchBase foodstuff, meatstuff, cutter;

    public static void initResearch() {

        foodstuff = new ResearchBase("ed.foodstuff", new ItemStack(EDRegistry.PLUMP_HELMET.get()), 8, 4);
        meatstuff = new ResearchBase("ed.meatstuff", new ItemStack(EDRegistry.TRANSMOG_MEAT.get()), 10, 6).addAncestor(foodstuff);
        cutter = new ResearchBase("ed.cutter", new ItemStack(EDRegistry.CUTTER.get()), 0, 7).addAncestor(ResearchManager.stamper);

        ResearchManager.subCategorySimpleAlchemy.addResearch(foodstuff).addResearch(meatstuff);
        ResearchManager.categoryMechanisms.addResearch(cutter);
    }
}
