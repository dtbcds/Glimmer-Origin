package me.dtbcds.carrier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.dtbcds.carrier.Carrier;
import me.dtbcds.carrier.api.Carriable;
import me.dtbcds.carrier.api.CarriableRegistry;
import me.dtbcds.carrier.api.CarrierComponent;
import me.dtbcds.carrier.api.CarryingData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerRenderer {
    @Inject(method = "render", at = @At("TAIL"))
    private void carrier_renderCarrying(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        CarrierComponent carrier = Carrier.HOLDER.get(player);
        CarryingData carrying = carrier.getCarryingData();
        if (carrying == null) return;
        Carriable<?> carriable = CarriableRegistry.INSTANCE.get(carrying.getType());
        if (carriable != null) {
            carriable.render(player, carrier, matrices, vertexConsumerProvider, tickDelta, light);
        }
    }
}
