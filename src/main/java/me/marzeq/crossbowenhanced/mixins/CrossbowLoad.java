package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public abstract class CrossbowLoad {
    @Inject(at = @At("TAIL"), method = "onStoppedUsing")
    private void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (!(user instanceof PlayerEntity) || !user.equals(CrossbowEnhanced.CLIENT.player)) return;

        /* on singleplayer worlds, this method is actually called both by the internal server and the client.
        we don't want to swap two times, so we have to only run this on the client (aka render thread) to avoid swapping twice */
        if (!CrossbowEnhanced.CLIENT.isOnThread()) return;

        var crossbowHand = CrossbowEnhanced.getCurrentSlot() == CrossbowEnhanced.OFFHAND_SLOT ? Hand.MAIN_HAND : Hand.OFF_HAND;
        System.out.println(crossbowHand);

        if (CrossbowEnhanced.config.fireworksInOffHand) {
            if (!CrossbowEnhanced.isSwapped()) return;

            try {
                CrossbowEnhanced.swap(CrossbowEnhanced.getPreviousSlot(), CrossbowEnhanced.getCurrentSlot());
            } catch (NullPointerException e) {
                CrossbowEnhanced.LOGGER.error("Something went terribly wrong, stack trace:");
                e.printStackTrace();
            }

            CrossbowEnhanced.resetValues();
        }

        if (CrossbowEnhanced.config.autoShoot && CrossbowEnhanced.isCrossbowCharged(stack)) {
            CrossbowEnhanced.clickHand(crossbowHand);
        }
    }
}
