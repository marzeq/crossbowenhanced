package me.marzeq.crossbowenhanced.mixins;

import me.marzeq.crossbowenhanced.SlotManager;
import net.minecraft.entity.player.PlayerInventory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerInventory.class)
public class ScrollInHotbar {
    @Redirect(method = "scrollInHotbar", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD))
    private void selectedSlot(PlayerInventory inventory, int i) {
        if (SlotManager.isSwapped()) {
            SlotManager.swap(SlotManager.getPreviousSlot(), SlotManager.getCurrentSlot());
            SlotManager.resetValues();
        }

        inventory.selectedSlot = i;
    }
}
