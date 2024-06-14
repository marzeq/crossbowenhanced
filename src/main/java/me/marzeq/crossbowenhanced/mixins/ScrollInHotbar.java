package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.CrossbowEnhanced;
import net.minecraft.entity.player.PlayerInventory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerInventory.class)
public class ScrollInHotbar {
    @Redirect(method = "scrollInHotbar", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD))
    private void selectedSlot(PlayerInventory inventory, int i) {
        if (CrossbowEnhanced.isSwapped()) {
            CrossbowEnhanced.swap(CrossbowEnhanced.getPreviousSlot(), CrossbowEnhanced.getCurrentSlot());
            CrossbowEnhanced.resetValues();
        }

        inventory.selectedSlot = i;
    }
}
