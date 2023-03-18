package net.mehvahdjukaar.suppsquared.common;


import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.mehvahdjukaar.supplementaries.common.block.ITextHolderProvider;
import net.mehvahdjukaar.supplementaries.common.block.TextHolder;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SignPostBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public class PlaqueBlockTile extends BlockEntity implements ITextHolderProvider, IOwnerProtected {

    private final TextHolder textHolder;
    private UUID owner = null;

    public PlaqueBlockTile(BlockPos pos, BlockState state) {
        super(SuppSquared.PLAQUE_TILE.get(), pos, state);
        this.textHolder = new TextHolder(2, 90);
    }

    @Override
    public TextHolder getTextHolder() {
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

    @Nullable
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
        // SignPostScreen.open(this);
    }

    public InteractionResult handleInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, ItemStack itemstack, Item item, SignPostBlockTile tile) {
        InteractionResult result = tile.getTextHolder().playerInteract(level, pos, player, handIn, tile);
        if (result != InteractionResult.PASS) return result;

        // open gui (edit sign with empty hand)
        tile.sendOpenGuiPacket(level, pos, player);

        return InteractionResult.CONSUME;
    }

}

