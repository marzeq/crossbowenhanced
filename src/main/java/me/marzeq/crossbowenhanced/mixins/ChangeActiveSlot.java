package me.marzeq.crossbowenhanced.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.marzeq.crossbowenhanced.SlotManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class ChangeActiveSlot {
    @WrapOperation(method = {"handleKeybinds", "pickBlockOrEntity"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private void selectedSlot(Inventory inventory, int i, Operation<Void> original) {
        if (inventory.getSelectedSlot() == i) return;

        if (SlotManager.isSwapped()) {
            SlotManager.swap(SlotManager.getPreviousSlot(), SlotManager.getCurrentSlot());
            SlotManager.resetValues();
        }

        inventory.setSelectedSlot(i);
    }

    @Inject(at = @At("TAIL"), method = "pickBlockOrEntity")
        private void doItemPick(CallbackInfo ci) {
        if (SlotManager.isSwapped()) {
            SlotManager.swap(SlotManager.getPreviousSlot(), SlotManager.getCurrentSlot());
            SlotManager.resetValues();
        }
    }
}
