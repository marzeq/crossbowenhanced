package me.marzeq.crossbowenhanced;

import me.marzeq.crossbowenhanced.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.InteractionHand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrossbowEnhanced implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("crossbowenhanced");
    public static final Minecraft CLIENT = Minecraft.getInstance();

    public static final String MOD_ID = "crossbowenhanced";

    public static final Config config = Config.load();

    @Override
    public void onInitializeClient() {
        Config.load();
        LOGGER.info("Crossbow Enhanced initialized");
    }

    public static boolean isFireworkWithEffects(ItemStack itemStack) {
        if (itemStack == null) return false;

        if (!(itemStack.getItem() instanceof FireworkRocketItem)) return false;

        var component = itemStack.get(DataComponents.FIREWORKS);

        return component != null && !component.explosions().isEmpty();
    }

    public static boolean isTippedArrow(ItemStack itemStack) {
        if (itemStack == null) return false;

        return itemStack.getItem() instanceof TippedArrowItem;
    }

    public static boolean isRegularArrow(ItemStack itemStack) {
        if (itemStack == null) return false;

        return itemStack.getItem() instanceof ArrowItem;
    }

    public static boolean isPreferredProjectile(ItemStack itemStack) {
        return switch (config.preferredProjectile) {
            case FIREWORKS -> isFireworkWithEffects(itemStack);
            case TIPPED_ARROWS -> isTippedArrow(itemStack);
            case REGULAR_ARROWS -> isRegularArrow(itemStack);
            default -> false;
        };
    }

    public static boolean isCrossbowCharged(ItemStack itemStack) {
        return !itemStack.get(DataComponents.CHARGED_PROJECTILES).isEmpty();
    }

    public static void clickHand(InteractionHand hand) {
        CLIENT.gameMode.useItem(CLIENT.player, hand);
    }
}
