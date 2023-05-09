package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.supplementaries.common.items.KeyItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class HeavyKeyItem extends KeyItem {

    private static final String DEFAULT_KEY = uuidToLongString(UUID.fromString("8d5fae3f-1d44-47b4-9d81-1d0ee7669dd6"));

    public HeavyKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public String getPassword(ItemStack stack) {
        CompoundTag t = stack.getTag();
        if (t != null && t.contains("id")) {
            UUID id = t.getUUID("id");
            return uuidToLongString(id);
        }
        return DEFAULT_KEY;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        stack.getOrCreateTag().putUUID("id", UUID.randomUUID());
    }

    private static String uuidToLongString(UUID id) {
        return id.toString().replace("-", "-----");
    }
}
