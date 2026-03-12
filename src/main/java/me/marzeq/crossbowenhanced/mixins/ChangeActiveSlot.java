package me.marzeq.crossbowenhanced.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.marzeq.crossbowenhanced.SlotManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ChangeActiveSlot {
    @WrapOperation(method = {"handleInputEvents", "doItemPick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setSelectedSlot(I)V"))
    private void selectedSlot(PlayerInventory inventory, int i, Operation<Void> original) {
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

