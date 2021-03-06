package me.dtbcds.carrier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.Unpooled;
import me.dtbcds.carrier.Carrier;
import me.dtbcds.carrier.api.CarrierPlayerExtension;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;

@SuppressWarnings({ "deprecation" })
@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity implements CarrierPlayerExtension {
    private boolean lastPressed = false;
    private boolean pressed = false;

    @Inject(method = "tick", at = @At("RETURN"))
    private void carrier_sendPacket(CallbackInfo ci) {
        if (lastPressed != pressed) {
            lastPressed = pressed;
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBoolean(pressed);
            ClientSidePacketRegistry.INSTANCE.sendToServer(Carrier.SET_CAN_CARRY_PACKET, buf);
        }
    }

    @Override
    public boolean canCarry() {
        return pressed;
    }

    @Override
    public void setCanCarry(boolean value) {
        this.pressed = value;
    }
}
