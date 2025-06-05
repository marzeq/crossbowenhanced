package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import me.marzeq.crossbowenhanced.SlotManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowLoad {
    @Inject(at = @At("TAIL"), method = "onStoppedUsing")
    private void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable cir) {
        if (!(user instanceof PlayerEntity) || !((PlayerEntity) user).isMainPlayer()) return;

        /* on singleplayer worlds, this method is actually called both by the internal server and the client.
        we don't want to swap two times, so we have to only run this on the client (aka render thread) to avoid swapping twice */
        if (!CrossbowEnhanced.CLIENT.isOnThread()) return;

        var crossbowHand = SlotManager.getCurrentSlot() == SlotManager.OFFHAND_SLOT ? Hand.MAIN_HAND : Hand.OFF_HAND;

        if (CrossbowEnhanced.config.autoShoot && CrossbowEnhanced.isCrossbowCharged(stack)) {
            CrossbowEnhanced.clickHand(crossbowHand);
        }

        if (CrossbowEnhanced.config.fireworksInOffHand) {
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
