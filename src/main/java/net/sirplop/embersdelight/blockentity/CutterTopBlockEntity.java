package net.sirplop.embersdelight.blockentity;

import com.rekindled.embers.Embers;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.event.MachineRecipeEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.tile.IUpgradeable;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.util.sound.ISoundController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.sirplop.embersdelight.EDConfig;
import net.sirplop.embersdelight.EDRegistry;
import net.sirplop.embersdelight.datagen.EDSounds;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class CutterTopBlockEntity extends BlockEntity implements IExtraDialInformation, IExtraCapabilityInformation, ISoundController, IUpgradeable {
    public CutterTopBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(EDRegistry.CUTTER_TOP_ENTITY.get(), pPos, pBlockState);
        capability.setEmberCapacity(8000);
    }
    public static final int SOUND_ON = 1;
    public static final int[] SOUND_IDS = new int[]{SOUND_ON};
    HashSet<Integer> soundsPlaying = new HashSet<>();

    int progress = -1;
    public float angle = 0;
    public float lastAngle;
    private static final float MAX_SPIN = 24.0f;
    private float spinRamp = 0;
    private double speedMod;
    public boolean isWorking;
    public List<UpgradeContext> upgrades = new ArrayList<>();
    private ResourceLocation cachedRecipe;

    public IEmberCapability capability = new DefaultEmberCapability() {
        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            CutterTopBlockEntity.this.setChanged();
        }
    };
    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            CutterTopBlockEntity.this.setChanged();
        }
    };
    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> inventory);


    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        capability.deserializeNBT(nbt);
        if (nbt.contains("progress"))
            progress = nbt.getInt("progress");
        isWorking = nbt.getBoolean("working");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", inventory.serializeNBT());
        capability.writeToNBT(nbt);
        nbt.putInt("progress", progress);
        nbt.putBoolean("working", isWorking);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putBoolean("working", isWorking);
        return nbt;
    }
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CutterTopBlockEntity blockEntity) {
        commonTick(level, pos, state, blockEntity);
        blockEntity.handleSound();
    }

    public static boolean commonTick(Level level, BlockPos pos, BlockState state, CutterTopBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST});
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (UpgradeUtil.doTick(blockEntity, blockEntity.upgrades))
            return false;

        blockEntity.spinRamp = Math.max(0, Math.min(MAX_SPIN, blockEntity.spinRamp + (blockEntity.isWorking ? 1 : -1)));
        blockEntity.lastAngle = blockEntity.angle;
        blockEntity.speedMod = UpgradeUtil.getTotalSpeedModifier(blockEntity, blockEntity.upgrades);
        blockEntity.angle += (float) (blockEntity.spinRamp * blockEntity.speedMod);
        return true;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CutterTopBlockEntity blockEntity) {
        if (!commonTick(level, pos, state, blockEntity))
            return;
        CutterBottomBlockEntity bot = (CutterBottomBlockEntity) level.getBlockEntity(pos.below());
        if (bot == null || blockEntity.inventory.getStackInSlot(0).isEmpty()) {
            blockEntity.reset();
            return;
        }

        RecipeWrapper wrapper = new RecipeWrapper(blockEntity.inventory);
        Optional<CuttingBoardRecipe> matchingRecipe = blockEntity.getMatchingRecipe(wrapper);
        List<ItemStack> output;
        if (!matchingRecipe.isPresent()) {
            //this item(s) isn't valid, pass it through.
            blockEntity.reset();
            if (tryPushItems(blockEntity.inventory.getStackInSlot(0), bot.inventory))
                blockEntity.inventory.setStackInSlot(0, ItemStack.EMPTY);
            return;
        } else {
            //verify that we can actually spit the result out.
            output = matchingRecipe.get().rollResults(level.random, 0);
            for (ItemStack stack : output) {
                if (!simPushItems(stack, bot.inventory)) {
                    //we can't, so stop everything!
                    blockEntity.reset();
                    return;
                }
            }
        }

        double emberCost = UpgradeUtil.getTotalEmberConsumption(blockEntity, EDConfig.CUTTER_EMBER_COST.get(), blockEntity.upgrades);
        if (blockEntity.capability.getEmber() < emberCost) {
            blockEntity.reset();
            return;
        }
        if (UpgradeUtil.doWork(blockEntity, blockEntity.upgrades)) {
            blockEntity.reset();
            return;
        }

        UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.CONSUME, emberCost), blockEntity.upgrades);
        blockEntity.capability.removeAmount(emberCost, true);

        if (level instanceof ServerLevel serverLevel) {
            ItemStack grinding = blockEntity.inventory.getStackInSlot(0);
            ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, grinding);
            serverLevel.sendParticles(particle, pos.getX() + 0.5f, pos.getY() + 0.2f, pos.getZ() + 0.5f, 1, 0.125, 0.0, 0.125, 0.1f);
        }

        //TODO: Config cutter process time
        blockEntity.isWorking = true;
        blockEntity.progress++;
        if (blockEntity.progress >= UpgradeUtil.getWorkTime(blockEntity, EDConfig.CUTTER_PROCESS_TIME.get(), blockEntity.upgrades)) {
            UpgradeUtil.transformOutput(blockEntity, output, blockEntity.upgrades);
            if (!output.isEmpty()) {
                for (ItemStack stack : output) {
                    pushItems(stack, bot.inventory);
                }
                blockEntity.inventory.getStackInSlot(0).split(1);
                blockEntity.setChanged();
                blockEntity.reset();
                if (!blockEntity.inventory.getStackInSlot(0).isEmpty())
                    blockEntity.isWorking = true;
                UpgradeUtil.throwEvent(blockEntity, new MachineRecipeEvent.Success<>(blockEntity, matchingRecipe.get()), blockEntity.upgrades);
            }
        }
    }
    void reset() {
        isWorking = false;
        if (progress > 0) {
            progress = 0;
        }
    }

    private static boolean tryPushItems(ItemStack stack, ItemStackHandler inventory) {
        int slots = inventory.getSlots();
        ItemStack processStack = stack;
        List<Integer> validSlots = new ArrayList<>();
        for (int i = 0; i < slots; i++) {
            ItemStack outStack = inventory.insertItem(i, processStack, true);
            if (outStack != processStack) {
                processStack = outStack;
                validSlots.add(i);
                if (processStack.isEmpty())
                { //insert them all for realsies
                    processStack = stack;
                    for (int x : validSlots) {
                        processStack = inventory.insertItem(x, processStack, false);
                    }
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean pushItems(ItemStack stack, ItemStackHandler inventory) {
        int slots = inventory.getSlots();
        ItemStack processStack = stack;
        for (int i = 0; i < slots; i++) {
            processStack = inventory.insertItem(i, processStack, false);
            if (processStack.isEmpty())
                return true;
        }
        return false;
    }
    private static boolean simPushItems(ItemStack stack, ItemStackHandler inventory) {
        int slots = inventory.getSlots();
        ItemStack processStack = stack;
        for (int i = 0; i < slots; i++) {
            processStack = inventory.insertItem(i, processStack, true);
            if (processStack.isEmpty())
                return true;
        }
        return false;
    }

    private Optional<CuttingBoardRecipe> getMatchingRecipe(RecipeWrapper recipeWrapper) {
        if (level == null) return Optional.empty();

        if (cachedRecipe != null) {
            Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
                    .getRecipeMap(ModRecipeTypes.CUTTING.get())
                    .get(cachedRecipe);
            if (recipe instanceof CuttingBoardRecipe && recipe.matches(recipeWrapper, level)) {
                return Optional.of((CuttingBoardRecipe) recipe);
            }
        }

        List<CuttingBoardRecipe> recipeList = level.getRecipeManager().getRecipesFor(ModRecipeTypes.CUTTING.get(), recipeWrapper, level);
        if (recipeList.isEmpty()) {
            return Optional.empty();
        }
        CuttingBoardRecipe recipe = recipeList.get(0);
        cachedRecipe = recipe.getId();
        return Optional.of(recipe);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove) {
            if (cap == ForgeCapabilities.ITEM_HANDLER && side == Direction.UP) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, holder);
            }
            else if (cap == EmbersCapabilities.EMBER_CAPABILITY && side != Direction.UP)
                return capability.getCapability(cap, side);
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        holder.invalidate();
        capability.invalidate();
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER)
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT, Embers.MODID + ".tooltip.goggles.item", null));
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level instanceof ServerLevel)
            ((ServerLevel) level).getChunkSource().blockChanged(worldPosition);
    }

    @Override
    public void playSound(int id) {
        float soundX = (float) worldPosition.getX() + 0.5f;
        float soundY = (float) worldPosition.getY() - 0.5f;
        float soundZ = (float) worldPosition.getZ() + 0.5f;
        switch (id) {
            case SOUND_ON:
                EmbersSounds.playMachineSound(this, SOUND_ON, EDSounds.CUTTER_LOOP.get(), SoundSource.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
                break;
        }
        soundsPlaying.add(id);
    }

    @Override
    public void stopSound(int id) {
        level.playLocalSound((float) worldPosition.getX() + 0.5f, (float) worldPosition.getY() - 0.5f, (float) worldPosition.getZ() + 0.5f, EDSounds.CUTTER_STOP.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
        soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        switch (id) {
            case SOUND_ON:
                return isWorking;
            default:
                return false;
        }
    }

    @Override
    public float getCurrentVolume(int id, float volume) {
        switch (id) {
            case SOUND_ON:
                return isWorking ? 1.0f : 0.0f;
            default:
                return 0f;
        }
    }

    @Override
    public float getCurrentPitch(int id, float pitch) {
        return (float) speedMod;
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), upgrades);
    }

    @Override
    public boolean isSideUpgradeSlot(Direction direction) {
        return direction != Direction.DOWN;
    }
}
