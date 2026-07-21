package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.SlotManager;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class Disconnect {
    @Inject(at = @At("HEAD"), method = "removePlayerFromWorld")
    private void cleanUp(CallbackInfo ci) {
        SlotManager.resetValues();
    }
}
