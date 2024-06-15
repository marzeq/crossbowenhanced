package me.marzeq.crossbowenhanced;

import net.minecraft.screen.slot.SlotActionType;

public class SlotManager {
    public static final int OFFHAND_SLOT = 45;

    private static int previousSlot = -1;
    private static int currentSlot = OFFHAND_SLOT;

    public static void resetValues() {
        SlotManager.previousSlot = -1;
        SlotManager.currentSlot = OFFHAND_SLOT;
    }

    public static void swappedWithSlot(int previousSlot, int currentSlot) {
        SlotManager.previousSlot = previousSlot;
        SlotManager.currentSlot = currentSlot;
    }

    public static boolean isSwapped() {
        return SlotManager.previousSlot != -1;
    }

    public static int getPreviousSlot() {
        return SlotManager.previousSlot;
    }

    public static int getCurrentSlot() {
        return SlotManager.currentSlot;
    }

    public static void swap(int fireworksSlot, int destinationSlot) throws NullPointerException {
        if (fireworksSlot < 9) fireworksSlot += 36;
        if (destinationSlot < 9) destinationSlot += 36;

        CrossbowEnhanced.CLIENT.interactionManager.clickSlot(0, fireworksSlot, 0, SlotActionType.PICKUP, CrossbowEnhanced.CLIENT.player);
        CrossbowEnhanced.CLIENT.interactionManager.clickSlot(0, destinationSlot, 0, SlotActionType.PICKUP, CrossbowEnhanced.CLIENT.player);
        CrossbowEnhanced.CLIENT.interactionManager.clickSlot(0, fireworksSlot, 0, SlotActionType.PICKUP, CrossbowEnhanced.CLIENT.player);
    }
}
