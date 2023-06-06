package net.mehvahdjukaar.suppsquared.common;

import dev.architectury.injectables.annotations.PlatformOnly;
import net.mehvahdjukaar.supplementaries.common.items.KeyItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class HeavyKeyItem extends KeyItem {

    private static final String DEFAULT_KEY = uuidToLongString(UUID.fromString("8d5fae3f-1d44-47b4-9d81-1d0ee7669dd6"));
    private static final String KEY_TAG = "id";

    public HeavyKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public String getPassword(ItemStack stack) {
        CompoundTag t = stack.getTag();
        if (t != null && t.contains(KEY_TAG)) {
            UUID id = t.getUUID(KEY_TAG);
            return uuidToLongString(id);
        }
        return DEFAULT_KEY;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        stack.getOrCreateTag().putUUID(KEY_TAG, UUID.randomUUID());
        player.sendSystemMessage(Component.literal("crafted"+ stack.getTag()));
    }

    private static String uuidToLongString(UUID id) {
        return id.toString().replace("-", "-----");
    }

    //TODO: figure out why this shit isnt obfuscared
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!stack.hasTag() || !stack.getTag().contains(KEY_TAG)) {
            stack.getOrCreateTag().putUUID(KEY_TAG, UUID.randomUUID());
        }
        return super.use(level, player, usedHand);
    }
    //crafted : m_7836_
    //used : m_7203_
    @PlatformOnly(PlatformOnly.FORGE)
    public void m_7836_(ItemStack stack, Level level, Player player) {
        stack.getOrCreateTag().putUUID(KEY_TAG, UUID.randomUUID());
    }

}
