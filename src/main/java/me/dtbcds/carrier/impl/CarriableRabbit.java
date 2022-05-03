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
import net.minecraft.client.render.entity.RabbitEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class CarriableRabbit extends EntityCarriable<RabbitEntity> {

    public static final Identifier TYPE = new Identifier("carrier", "minecraft_rabbit");
    @Environment(EnvType.CLIENT)
    private static RabbitEntity dummyRabbit;
    @Environment(EnvType.CLIENT)
    private static RabbitEntityRenderer rabbitRenderer;

    public CarriableRabbit() {
        super(TYPE, EntityType.RABBIT);
    }

    @NotNull
    @Override
    public EntityType<RabbitEntity> getParent() {
        return EntityType.RABBIT;
    }

    @SuppressWarnings("resource")
	@Override
    @Environment(EnvType.CLIENT)
    public RabbitEntity getEntity() {
        if (dummyRabbit == null)
            dummyRabbit = new RabbitEntity(EntityType.RABBIT, MinecraftClient.getInstance().world);
        return dummyRabbit;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public EntityRenderer<RabbitEntity> getEntityRenderer() {
        if (rabbitRenderer == null)
            rabbitRenderer = new RabbitEntityRenderer(ClientUtils.defaultEntityCtx());
        return rabbitRenderer;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(@NotNull PlayerEntity player, @NotNull CarrierComponent carrier, @NotNull MatrixStack matrices, @NotNull VertexConsumerProvider vcp, float tickDelta, int light) {
        updateEntity(carrier.getCarryingData());
        matrices.push();
        float yaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-yaw + 90));
        matrices.translate(-0.3, 0.8, 0.1);
        getEntityRenderer().render(getEntity(), 0, tickDelta, matrices, vcp, light);
        matrices.pop();
    }
	@Override
	public void tryThrow(@NotNull CarrierComponent carrier, @NotNull World world) {
	}
}