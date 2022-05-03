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
import net.minecraft.client.render.entity.TurtleEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class CarriableTurtle extends EntityCarriable<TurtleEntity> {

    public static final Identifier TYPE = new Identifier("carrier", "minecraft_turtle");
    @Environment(EnvType.CLIENT)
    private static TurtleEntity dummyTurtle;
    @Environment(EnvType.CLIENT)
    private static TurtleEntityRenderer turtleRenderer;

    public CarriableTurtle() {
        super(TYPE, EntityType.TURTLE);
    }

    @NotNull
    @Override
    public EntityType<TurtleEntity> getParent() {
        return EntityType.TURTLE;
    }

    @SuppressWarnings("resource")
	@Override
    @Environment(EnvType.CLIENT)
    public TurtleEntity getEntity() {
        if (dummyTurtle == null)
            dummyTurtle = new TurtleEntity(EntityType.TURTLE, MinecraftClient.getInstance().world);
        return dummyTurtle;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public EntityRenderer<TurtleEntity> getEntityRenderer() {
        if (turtleRenderer == null)
            turtleRenderer = new TurtleEntityRenderer(ClientUtils.defaultEntityCtx());
        return turtleRenderer;
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void render(@NotNull PlayerEntity player, @NotNull CarrierComponent carrier, @NotNull MatrixStack matrices, @NotNull VertexConsumerProvider vcp, float tickDelta, int light) {
        updateEntity(carrier.getCarryingData());
        matrices.push();
        matrices.scale(0.6f, 0.6f, 0.6f);
        float yaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-yaw + 90));
        matrices.translate(-1.0, 1.2, 0.2);
        getEntityRenderer().render(getEntity(), 0, tickDelta, matrices, vcp, light);
        matrices.pop();
    }
	@Override
	public void tryThrow(@NotNull CarrierComponent carrier, @NotNull World world) {
	}
}