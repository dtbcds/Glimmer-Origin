package me.dtbcds.carrier.mixin;

import org.spongepowered.asm.mixin.Mixin;

import me.dtbcds.carrier.api.CarrierPlayerExtension;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity implements CarrierPlayerExtension {
    private boolean carrier_isCarryReady;

    @Override
    public boolean canCarry() {
        return carrier_isCarryReady;
    }

    @Override
    public void setCanCarry(boolean value) {
        this.carrier_isCarryReady = value;
    }
}
