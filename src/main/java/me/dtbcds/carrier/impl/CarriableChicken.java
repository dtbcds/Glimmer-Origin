package me.dtbcds.carrier.impl;

import org.jetbrains.annotations.NotNull;

import me.dtbcds.carrier.ClientUtils;
import me.dtbcds.carrier.api.CarrierComponent;
import me.dtbcds.carrier.api.EntityCarriable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ChickenEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class CarriableChicken  extends EntityCarriable<ChickenEntity> {

    public static final Identifier TYPE = new Identifier("carrier", "minecraft_chicken");
    @Environment(EnvType.CLIENT)
    private static ChickenEntity dummyChicken;
    @Environment(EnvType.CLIENT)
    private static ChickenEntityRenderer chickenRenderer;

    public CarriableChicken() {
        super(TYPE, EntityType.CHICKEN);
    }

    @NotNull
    @Override
    public EntityType<ChickenEntity> getParent() {
        return EntityType.CHICKEN;
    }

    @SuppressWarnings("resource")
	@Override
    @Environment(EnvType.CLIENT)
    public ChickenEntity getEntity() {
        if (dummyChicken == null)
            dummyChicken = new ChickenEntity(EntityType.CHICKEN, MinecraftClient.getInstance().world);
        return dummyChicken;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public EntityRenderer<ChickenEntity> getEntityRenderer() {
        if (chickenRenderer == null)
            chickenRenderer = new ChickenEntityRenderer(ClientUtils.defaultEntityCtx());
        return chickenRenderer;
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void render(@NotNull PlayerEntity player, @NotNull CarrierComponent carrier, @NotNull MatrixStack matrices, @NotNull VertexConsumerProvider vcp, float tickDelta, int light) {
        updateEntity(carrier.getCarryingData());
        matrices.push();
        matrices.scale(0.9f, 0.9f, 0.9f);
        float yaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-yaw + 90));
        matrices.translate(-0.4, 0.6, 0.00);
        getEntityRenderer().render(getEntity(), 0, tickDelta, matrices, vcp, light);
        matrices.pop();
    }

	@Override
	public void tryThrow(@NotNull CarrierComponent carrier, @NotNull World world) {
	}
}