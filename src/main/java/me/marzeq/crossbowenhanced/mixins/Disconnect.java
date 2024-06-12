package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class Disconnect {
    @Inject(at = @At("HEAD"), method = "cleanUp")
    private void cleanUp(CallbackInfo ci) {
        CrossbowEnhanced.resetValues();
    }
}
