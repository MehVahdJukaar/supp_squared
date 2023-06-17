package net.mehvahdjukaar.suppsquared.common;


import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.supplementaries.common.block.ITextHolderProvider;
import net.mehvahdjukaar.supplementaries.common.block.TextHolder;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.mehvahdjukaar.suppsquared.client.PlaqueEditScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;


public class PlaqueBlockTile extends BlockEntity implements ITextHolderProvider, IOwnerProtected {

    public static final int MAX_LINES = 3;
    public static final int LINE_SEPARATION = 12;
    private static final int MAX_WIDTH = 80;

    private final TextHolder textHolder;
    private UUID owner = null;

    public PlaqueBlockTile(BlockPos pos, BlockState state) {
        super(SuppSquared.PLAQUE_TILE.get(), pos, state);
        this.textHolder = new TextHolder(MAX_LINES, MAX_WIDTH);
    }

    @Override
    public TextHolder getTextHolder(int ind) {
        return this.textHolder;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.textHolder.load(compound);
        this.loadOwner(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        this.textHolder.save(compound);
        this.saveOwner(compound);
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public void openScreen(Level level, BlockPos pos, Player player) {
        PlaqueEditScreen.open(this);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
}

