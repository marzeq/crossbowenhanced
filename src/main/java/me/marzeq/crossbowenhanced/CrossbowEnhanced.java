package me.marzeq.crossbowenhanced;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrossbowEnhanced implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("crossbowenhanced");
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static int swappedSlot = -1;

    public static void resetValues() {
        CrossbowEnhanced. swappedSlot = -1;
    }

    public static void swappedWithSlot(int swappedSlot) {
        CrossbowEnhanced.swappedSlot = swappedSlot;
    }

    public static boolean isSwapped() {
        return CrossbowEnhanced.swappedSlot != -1;
    }

    public static int getSwappedSlot() {
        return CrossbowEnhanced.swappedSlot;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Crossbow Enhanced initialized");
    }

    public static void sendMessage(String message) {
        if (CLIENT.player != null) CLIENT.player.sendMessage(Text.of(message));
    }

    public static void sendMessage(Object object) {
        sendMessage(object.toString());
    }

    public static boolean isFireworkWithEffects(ItemStack itemStack) {
        if (itemStack == null) return false;

        if (!(itemStack.getItem() instanceof FireworkRocketItem)) return false;

        var component = (FireworksComponent) itemStack.get(DataComponentTypes.FIREWORKS);

        return component != null && !component.explosions().isEmpty();
    }

    public static void swap(int slot) throws NullPointerException {
        if (slot < 9) slot += 36;

        CLIENT.interactionManager.clickSlot(0, slot, 0, SlotActionType.PICKUP, CLIENT.player);
        CLIENT.interactionManager.clickSlot(0, 45, 0, SlotActionType.PICKUP, CLIENT.player);
        CLIENT.interactionManager.clickSlot(0, slot, 0, SlotActionType.PICKUP, CLIENT.player);
    }
}
