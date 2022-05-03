package me.dtbcds.carrier.impl;

import org.jetbrains.annotations.NotNull;

import me.dtbcds.carrier.ClientUtils;
import me.dtbcds.carrier.api.CarrierComponent;
import me.dtbcds.carrier.api.EntityCarriable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ParrotEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class CarriableParrot extends EntityCarriable<ParrotEntity> {

    public static final Identifier TYPE = new Identifier("carrier", "minecraft_parrot");
    @Environment(EnvType.CLIENT)
    private static ParrotEntity dummyParrot;
    @Environment(EnvType.CLIENT)
    private static ParrotEntityRenderer parrotRenderer;

    public CarriableParrot() {
        super(TYPE, EntityType.PARROT);
    }

    @NotNull
    @Override
    public EntityType<ParrotEntity> getParent() {
        return EntityType.PARROT;
    }

    @SuppressWarnings("resource")
	@Override
    @Environment(EnvType.CLIENT)
    public ParrotEntity getEntity() {
        if (dummyParrot == null)
            dummyParrot = new ParrotEntity(EntityType.PARROT, MinecraftClient.getInstance().world);
        return dummyParrot;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public EntityRenderer<ParrotEntity> getEntityRenderer() {
        if (parrotRenderer == null)
            parrotRenderer = new ParrotEntityRenderer(ClientUtils.defaultEntityCtx());
        return parrotRenderer;
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void render(@NotNull PlayerEntity player, @NotNull CarrierComponent carrier, @NotNull MatrixStack matrices, @NotNull VertexConsumerProvider vcp, float tickDelta, int light) {
        updateEntity(carrier.getCarryingData());
        matrices.push();
        matrices.scale(0.9f, 0.9f, 0.9f);
        float yaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-yaw + 90));
        matrices.translate(-0.4, 0.8, -0.1);
        getEntityRenderer().render(getEntity(), 0, tickDelta, matrices, vcp, light);
        matrices.pop();
    }
	@Override
	public void tryThrow(@NotNull CarrierComponent carrier, @NotNull World world) {
	}
}