package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowLoad {
    @Inject(at = @At("RETURN"), method = "loadProjectiles")
    private static void loadProjectiles(LivingEntity shooter, ItemStack crossbow, CallbackInfoReturnable<Boolean> cir) {
        /* on singleplayer worlds, this method is actually called both by the internal server and the client.
        we don't want to swap two times, so we have to only run this on the client (aka render thread) to avoid swapping twice */
        if (!CrossbowEnhanced.CLIENT.isOnThread()) return;

        if (!CrossbowEnhanced.isSwapped()) return;

        try {
            CrossbowEnhanced.swap(CrossbowEnhanced.getSwappedSlot());
        }  catch (NullPointerException e) {
            CrossbowEnhanced.LOGGER.error("Something went terribly wrong, stack trace:");
            e.printStackTrace();
        }

        CrossbowEnhanced.resetValues();
    }
}
