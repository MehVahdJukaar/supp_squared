package net.mehvahdjukaar.suppsquared.common;


import net.mehvahdjukaar.moonlight.api.client.IScreenProvider;
import net.mehvahdjukaar.supplementaries.common.block.ITextHolderProvider;
import net.mehvahdjukaar.supplementaries.common.block.TextHolder;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.mehvahdjukaar.suppsquared.client.PlaqueEditScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public class PlaqueBlockTile extends BlockEntity implements ITextHolderProvider, IScreenProvider {

    public static final int MAX_LINES = 3;
    public static final int LINE_SEPARATION = 12;
    private static final int MAX_WIDTH = 65;

    private final TextHolder textHolder;
    private boolean waxed = false;

    @Nullable
    private UUID playerWhoMayEdit;

    public PlaqueBlockTile(BlockPos pos, BlockState state) {
        super(SuppSquared.PLAQUE_TILE.get(), pos, state);
        this.textHolder = new TextHolder(MAX_LINES, MAX_WIDTH);
    }

    @Override
    public TextHolder getTextHolder(int ind) {
        return this.textHolder;
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.textHolder.load(compound, this.level, this.worldPosition);
        if (compound.contains("Waxed")) {
            this.waxed = compound.getBoolean("Waxed");
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        this.textHolder.save(compound, registries);
        if (this.waxed) {
            compound.putBoolean("Waxed", waxed);
        }
    }

    @Override
    public void openScreen(Level level, BlockPos pos, Player player, Direction dir) {
        PlaqueEditScreen.open(this);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void setPlayerWhoMayEdit(@Nullable UUID uuid) {
        this.playerWhoMayEdit = uuid;
    }

    @Override
    public UUID getPlayerWhoMayEdit() {
        return playerWhoMayEdit;
    }

    @Override
    public boolean isWaxed() {
        return waxed;
    }

    @Override
    public void setWaxed(boolean b) {
        this.waxed = b;
    }
}

