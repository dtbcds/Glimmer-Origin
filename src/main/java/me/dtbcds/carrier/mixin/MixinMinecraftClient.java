package me.dtbcds.carrier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.Unpooled;
import me.dtbcds.carrier.Carrier;
import me.dtbcds.carrier.api.CarrierComponent;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

@SuppressWarnings("deprecation")
@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(
            method = "handleInputEvents",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I"),
            cancellable = true
    )
    private void carrier_cancelHotbarSelect(CallbackInfo ci) {
        @SuppressWarnings("resource")
		CarrierComponent carrier = Carrier.HOLDER.get(MinecraftClient.getInstance().player);
        if (carrier.getCarryingData() != null) ci.cancel();
    }

    @Inject(
            method = "handleInputEvents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doAttack()Z"),
            cancellable = true
    )
    private void carrier_cancelPunch(CallbackInfo ci) {
        @SuppressWarnings("resource")
		CarrierComponent carrier = Carrier.HOLDER.get(MinecraftClient.getInstance().player);
        if (carrier.getCarryingData() != null) ci.cancel();
    }

    @ModifyArg(
            method = "handleInputEvents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V")
    )
    private boolean carrier_cancelBlockBreak(boolean value) {
        @SuppressWarnings("resource")
		CarrierComponent carrier = Carrier.HOLDER.get(MinecraftClient.getInstance().player);
        if (carrier.getCarryingData() != null) return false;
        return value;
    }

    @Inject(
            method = "handleInputEvents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;dropSelectedItem(Z)Z"),
            cancellable = true
    )
    private void carrier_cancelDrop(CallbackInfo ci) {
        @SuppressWarnings("resource")
		CarrierComponent carrier = Carrier.HOLDER.get(MinecraftClient.getInstance().player);
        if (carrier.getCarryingData() != null) ci.cancel();
    }

    @Inject(
            method = "handleInputEvents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;inventoryKey:Lnet/minecraft/client/option/KeyBinding;")),
            cancellable = true
    )
    private void carrier_cancelOpenInventory(CallbackInfo ci) {
        @SuppressWarnings("resource")
		CarrierComponent carrier = Carrier.HOLDER.get(MinecraftClient.getInstance().player);
        if (carrier.getCarryingData() != null) ci.cancel();
    }
    @Inject(
            method = "handleInputEvents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V"),
            cancellable = true
    )
    private void rightclick(CallbackInfo ci) {
    	PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        ClientSidePacketRegistry.INSTANCE.sendToServer(Carrier.THROW, buf);
    }
}
