package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
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
    @Redirect(method = {"handleInputEvents", "doItemPick"}, at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD))
    private void selectedSlot(PlayerInventory inventory, int i) {
        if (inventory.selectedSlot == i) return;

        if (CrossbowEnhanced.isSwapped()) {
            CrossbowEnhanced.swap(CrossbowEnhanced.getPreviousSlot(), CrossbowEnhanced.getCurrentSlot());
            CrossbowEnhanced.resetValues();
        }

        inventory.selectedSlot = i;
    }

    @Inject(method = "doItemPick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickCreativeStack(Lnet/minecraft/item/ItemStack;I)V", shift = At.Shift.AFTER))
    private void doItemPick(CallbackInfo ci) {
        if (CrossbowEnhanced.isSwapped()) {
            CrossbowEnhanced.swap(CrossbowEnhanced.getPreviousSlot(), CrossbowEnhanced.getCurrentSlot());
            CrossbowEnhanced.resetValues();
        }
    }
}

