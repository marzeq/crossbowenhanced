package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import me.marzeq.crossbowenhanced.SlotManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class CrossbowDraw {
    @Inject(method="doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", shift = At.Shift.BEFORE))
    private void doItemUse(CallbackInfo info) {
        var player = CrossbowEnhanced.CLIENT.player;

        if (!CrossbowEnhanced.config.fireworksInOffHand || player == null) {
            return;
        }

        var handItemStack = player.getMainHandStack();
        var offHandItemStack = player.getOffHandStack();

        boolean crossbowInMainHand;

        if (offHandItemStack.getItem() instanceof CrossbowItem) {
            crossbowInMainHand = false;
        } else if (handItemStack.getItem() instanceof CrossbowItem) {
            crossbowInMainHand = true;
        } else {
            return;
        }


        if (crossbowInMainHand && CrossbowEnhanced.isFireworkWithEffects(offHandItemStack)) {
            return;
        } else if (!crossbowInMainHand && CrossbowEnhanced.isFireworkWithEffects(handItemStack)) {
            return;
        }

        var crossbowItemStack = crossbowInMainHand ? handItemStack : offHandItemStack;

        if (CrossbowEnhanced.isCrossbowCharged(crossbowItemStack)) {
            return;
        }

        int slot = findSlotWithMinFireworkCount(player);
        if (slot == -1) {
            return;
        }

        var slotTarget = crossbowInMainHand ? SlotManager.OFFHAND_SLOT : player.getInventory().selectedSlot;

        try {
            SlotManager.swap(slot, slotTarget);
            SlotManager.swappedWithSlot(slot, slotTarget);
        }  catch (NullPointerException e) {
            CrossbowEnhanced.LOGGER.error("Something went terribly wrong, stack trace:");
            e.printStackTrace();
        }
    }

    @Unique
    private int findSlotWithMinFireworkCount(ClientPlayerEntity player) {
        int slot = -1;
        int minValue = Integer.MAX_VALUE;

        for (int i = 0; i < 36; i++) {
            var itemStack = player.getInventory().getStack(i);

            if (CrossbowEnhanced.isFireworkWithEffects(itemStack) && itemStack.getCount() < minValue) {
                slot = i;
                minValue = itemStack.getCount();
            }
        }

        return slot;
    }
}