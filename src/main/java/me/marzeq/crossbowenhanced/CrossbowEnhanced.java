package me.marzeq.crossbowenhanced;

import me.marzeq.crossbowenhanced.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrossbowEnhanced implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("crossbowenhanced");
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static final String MOD_ID = "crossbowenhanced";

    public static final Config config = Config.load();

    public static final int OFFHAND_SLOT = 45;

    private static int previousSlot = -1;
    private static int currentSlot = OFFHAND_SLOT;

    public static void resetValues() {
        CrossbowEnhanced.previousSlot = -1;
        CrossbowEnhanced.currentSlot = OFFHAND_SLOT;
    }

    public static void swappedWithSlot(int previousSlot, int currentSlot) {
        CrossbowEnhanced.previousSlot = previousSlot;
        CrossbowEnhanced.currentSlot = currentSlot;
    }

    public static boolean isSwapped() {
        return CrossbowEnhanced.previousSlot != -1;
    }

    public static int getPreviousSlot() {
        return CrossbowEnhanced.previousSlot;
    }

    public static int getCurrentSlot() {
        return CrossbowEnhanced.currentSlot;
    }

    @Override
    public void onInitializeClient() {
        Config.load();
        LOGGER.info("Crossbow Enhanced initialized");
    }

    public static boolean isFireworkWithEffects(ItemStack itemStack) {
        if (itemStack == null) return false;

        if (!(itemStack.getItem() instanceof FireworkRocketItem)) return false;

        var component = (FireworksComponent) itemStack.get(DataComponentTypes.FIREWORKS);

        return component != null && !component.explosions().isEmpty();
    }

    public static boolean isCrossbowCharged(ItemStack itemStack) {
        return !itemStack.get(DataComponentTypes.CHARGED_PROJECTILES).isEmpty();
    }

    public static void swap(int fireworksSlot, int destinationSlot) throws NullPointerException {
        if (fireworksSlot < 9) fireworksSlot += 36;
        if (destinationSlot < 9) destinationSlot += 36;

        CLIENT.interactionManager.clickSlot(0, fireworksSlot, 0, SlotActionType.PICKUP, CLIENT.player);
        CLIENT.interactionManager.clickSlot(0, destinationSlot, 0, SlotActionType.PICKUP, CLIENT.player);
        CLIENT.interactionManager.clickSlot(0, fireworksSlot, 0, SlotActionType.PICKUP, CLIENT.player);
    }

    public static void clickHand(Hand hand) {
        CLIENT.interactionManager.interactItem(CLIENT.player, hand);
    }
}
