package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import me.marzeq.crossbowenhanced.SlotManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowLoad {
    @Inject(at = @At("TAIL"), method = "releaseUsing")
    private void onStoppedUsing(ItemStack stack, Level level, LivingEntity user, int remainingTime, CallbackInfoReturnable<Boolean> cir) {
        if (!(user instanceof Player) || !((Player) user).isLocalPlayer()) return;

        /* on singleplayer worlds, this method is actually called both by the internal server and the client.
        we don't want to swap two times, so we have to only run this on the client (aka render thread) to avoid swapping twice */
        if (!CrossbowEnhanced.CLIENT.isSameThread()) return;

        var crossbowHand = SlotManager.getCurrentSlot() == SlotManager.OFFHAND_SLOT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        if (CrossbowEnhanced.config.enableAutoShootFeature && CrossbowEnhanced.isCrossbowCharged(stack)) {
            CrossbowEnhanced.clickHand(crossbowHand);
        }

        if (CrossbowEnhanced.config.enableProjectileManagementFeature) {
            if (!SlotManager.isSwapped()) return;

            try {
                SlotManager.swap(SlotManager.getPreviousSlot(), SlotManager.getCurrentSlot());
                SlotManager.resetValues();
            } catch (NullPointerException e) {
                CrossbowEnhanced.LOGGER.error("Something went terribly wrong, stack trace:");
                e.printStackTrace();
            }
        }
    }
}
