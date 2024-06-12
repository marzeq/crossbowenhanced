package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class CrossbowDraw {
    @Inject(at = @At("HEAD"), method = "doItemUse")
    private void useItem(CallbackInfo info) {

        CrossbowEnhanced.resetValues();

        var client = CrossbowEnhanced.CLIENT;
        var player = client.player;

        if (player == null) {
            return;
        }

        var item = player.getMainHandStack().getItem();

        var offHandItemStack = player.getOffHandStack();

        if (CrossbowEnhanced.isFireworkWithEffects(offHandItemStack)) {
            return;
        }

        if (!(item instanceof CrossbowItem) || CrossbowItem.isCharged(player.getMainHandStack())) {
            return;
        }

        int slot = -1;
        int minValue = Integer.MAX_VALUE;

        for (int i = 0; i < 36; i++) {
            var itemStack = player.getInventory().getStack(i);

            if (CrossbowEnhanced.isFireworkWithEffects(itemStack)) {
                if (itemStack.getCount() < minValue) {
                    slot = i;
                    minValue = itemStack.getCount();
                }
            }
        }

        if (slot == -1) {
            return;
        }

        try {
            CrossbowEnhanced.swap(slot);
        }  catch (NullPointerException e) {
            CrossbowEnhanced.LOGGER.error("Something went terribly wrong, stack trace:");
            e.printStackTrace();
            return;
        }

        CrossbowEnhanced.swappedWithSlot(slot);
    }
}