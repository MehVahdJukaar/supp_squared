package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.supplementaries.common.items.KeyItem;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class HeavyKeyItem extends KeyItem {

    public HeavyKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public String getPassword(ItemStack stack) {
        verifyComponentsAfterLoad(stack);
        return uuidToLongString(stack.get(SuppSquared.HEAVY_KEY_UUID.get()));
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack stack) {
        super.verifyComponentsAfterLoad(stack);
        if (!stack.has(SuppSquared.HEAVY_KEY_UUID.get())) {
            stack.set(SuppSquared.HEAVY_KEY_UUID.get(), UUID.randomUUID());
        }
    }

    @Override
    public void onCraftedPostProcess(ItemStack stack, Level level) {
        super.onCraftedPostProcess(stack, level);
        verifyComponentsAfterLoad(stack);
    }

    private static String uuidToLongString(UUID id) {
        return id.toString().replace("-", "-----");
    }

}
