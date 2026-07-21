package me.marzeq.crossbowenhanced;

import net.minecraft.world.inventory.ContainerInput;

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

    public static void swap(int fireworksSlot, int destinationSlot) {
        if (fireworksSlot < 9) fireworksSlot += 36;
        if (destinationSlot < 9) destinationSlot += 36;

        CrossbowEnhanced.CLIENT.gameMode.handleContainerInput(0, fireworksSlot, 0, ContainerInput.PICKUP, CrossbowEnhanced.CLIENT.player);
        CrossbowEnhanced.CLIENT.gameMode.handleContainerInput(0, destinationSlot, 0, ContainerInput.PICKUP, CrossbowEnhanced.CLIENT.player);
        CrossbowEnhanced.CLIENT.gameMode.handleContainerInput(0, fireworksSlot, 0, ContainerInput.PICKUP, CrossbowEnhanced.CLIENT.player);
    }
}
