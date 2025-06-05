package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.SlotManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ChangeActiveSlot {
    @Redirect(method = {"handleInputEvents", "doItemPick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setSelectedSlot(I)V"))
    private void selectedSlot(PlayerInventory inventory, int i) {
        if (inventory.getSelectedSlot() == i) return;

        if (SlotManager.isSwapped()) {
            SlotManager.swap(SlotManager.getPreviousSlot(), SlotManager.getCurrentSlot());
            SlotManager.resetValues();
        }

        inventory.setSelectedSlot(i);
    }

    @Inject(at = @At("TAIL"), method = "doItemPick")
        private void doItemPick(CallbackInfo ci) {
        if (SlotManager.isSwapped()) {
            SlotManager.swap(SlotManager.getPreviousSlot(), SlotManager.getCurrentSlot());
            SlotManager.resetValues();
        }
    }
}

