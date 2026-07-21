package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import me.marzeq.crossbowenhanced.SlotManager;
import me.marzeq.crossbowenhanced.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(Minecraft.class)
public class CrossbowDraw {
    @Inject(method="startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", shift = At.Shift.BEFORE))
    private void doItemUse(CallbackInfo info) {
        if (!CrossbowEnhanced.config.enableProjectileManagementFeature) {
            return;
        }

        var player = CrossbowEnhanced.CLIENT.player;
        if (player == null) {
            return;
        }

        fireProjectile(player);
    }

    @Unique
    private void fireProjectile(LocalPlayer player) {
        var handItemStack = player.getMainHandItem();
        var offHandItemStack = player.getOffhandItem();

        boolean crossbowInMainHand;

        if (offHandItemStack.getItem() instanceof CrossbowItem) {
            crossbowInMainHand = false;
        } else if (handItemStack.getItem() instanceof CrossbowItem) {
            crossbowInMainHand = true;
        } else {
            return;
        }


        if (crossbowInMainHand && CrossbowEnhanced.isPreferredProjectile(offHandItemStack) ||
                !crossbowInMainHand && CrossbowEnhanced.isPreferredProjectile(handItemStack)) {
            return;
        }

        var crossbowItemStack = crossbowInMainHand ? handItemStack : offHandItemStack;

        if (CrossbowEnhanced.isCrossbowCharged(crossbowItemStack)) {
            return;
        }

        int slot = switch (CrossbowEnhanced.config.preferredProjectile) {
            case FIREWORKS -> findPreferredStackOfType(player, CrossbowEnhanced::isFireworkWithEffects);
            case TIPPED_ARROWS -> findPreferredStackOfType(player, CrossbowEnhanced::isTippedArrow);
            case REGULAR_ARROWS -> findPreferredStackOfType(player, CrossbowEnhanced::isRegularArrow);
            default -> -1;
        };

        // slot == -1 means there is no preferred projectile in inventory, therefore we must try all other projectile types as a backup
        if (slot == -1) {
            slot = findPreferredStackOfType(player, CrossbowEnhanced::isFireworkWithEffects);
        } if (slot == -1) { // we check if still no projectile is found
            slot = findPreferredStackOfType(player, CrossbowEnhanced::isTippedArrow);
        } if (slot == -1) { // and here too
            slot = findPreferredStackOfType(player, CrossbowEnhanced::isRegularArrow);
        } if (slot == -1) { // there are no projectiles at all, return
            return;
        }


        var slotTarget = crossbowInMainHand ? SlotManager.OFFHAND_SLOT : player.getInventory().getSelectedSlot();

        SlotManager.swap(slot, slotTarget);
        SlotManager.swappedWithSlot(slot, slotTarget);
    }

    @Unique
    private int findPreferredStackOfType(LocalPlayer player, Predicate<ItemStack> pickPredicate) {
        int slot = -1;
        int minValue = Integer.MAX_VALUE;

        int start = CrossbowEnhanced.config.drawOrder == Config.DRAW_ORDER.FROM_TOP_LEFT ? 0 : 35;
        int end = CrossbowEnhanced.config.drawOrder == Config.DRAW_ORDER.FROM_TOP_LEFT ? 36 : -1;
        int step = CrossbowEnhanced.config.drawOrder == Config.DRAW_ORDER.FROM_TOP_LEFT ? 1 : -1;

        for (int i = start; i != end; i += step) {
            var itemStack = player.getInventory().getItem(i);

            if (pickPredicate.test(itemStack)) {
                if (!CrossbowEnhanced.config.prioritiseStacksWithLowerCount) {
                    return i;
                }

                if (itemStack.getCount() < minValue) {
                    slot = i;
                    minValue = itemStack.getCount();
                }
            }
        }

        return slot;
    }
}
