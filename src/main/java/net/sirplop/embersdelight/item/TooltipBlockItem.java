package net.sirplop.embersdelight.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TooltipBlockItem extends ItemNameBlockItem {
    protected final String tooltipLocation;
    protected final ChatFormatting format;
    protected final boolean foil;
    public TooltipBlockItem(Block block, Properties pProperties, String tooltipLocation, ChatFormatting format, boolean foil) {
        super(block, pProperties);
        this.tooltipLocation = tooltipLocation;
        this.format = format;
        this.foil = foil;
    }
    public TooltipBlockItem(Block block, Properties pProperties, String tooltipLocation, boolean foil) {
        super(block, pProperties);
        this.tooltipLocation = tooltipLocation;
        this.format = ChatFormatting.GRAY;
        this.foil = foil;
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable(tooltipLocation).withStyle(format));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
    @Override
    public boolean isFoil(ItemStack pStack) {
        return foil;
    }
}
