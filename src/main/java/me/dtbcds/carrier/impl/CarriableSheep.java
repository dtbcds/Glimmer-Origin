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
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class CarriableSheep extends EntityCarriable<SheepEntity> {

    public static final Identifier TYPE = new Identifier("carrier", "minecraft_sheep");
    @Environment(EnvType.CLIENT)
    private static SheepEntity dummySheep;
    @Environment(EnvType.CLIENT)
    private static SheepEntityRenderer sheepRenderer;

    public CarriableSheep() {
        super(TYPE, EntityType.SHEEP);
    }

    @NotNull
    @Override
    public EntityType<SheepEntity> getParent() {
        return EntityType.SHEEP;
    }

    @SuppressWarnings("resource")
	@Override
    @Environment(EnvType.CLIENT)
    public SheepEntity getEntity() {
        if (dummySheep == null)
            dummySheep = new SheepEntity(EntityType.SHEEP, MinecraftClient.getInstance().world);
        return dummySheep;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public EntityRenderer<SheepEntity> getEntityRenderer() {
        if (sheepRenderer == null)
            sheepRenderer = new SheepEntityRenderer(ClientUtils.defaultEntityCtx());
        return sheepRenderer;
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void render(@NotNull PlayerEntity player, @NotNull CarrierComponent carrier, @NotNull MatrixStack matrices, @NotNull VertexConsumerProvider vcp, float tickDelta, int light) {
        updateEntity(carrier.getCarryingData());
        matrices.push();
        matrices.scale(0.6f, 0.6f, 0.6f);
        float yaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-yaw + 90));
        matrices.translate(-0.6, 0.8, -0.1);
        getEntityRenderer().render(getEntity(), 0, tickDelta, matrices, vcp, light);
        matrices.pop();
    }
	@Override
	public void tryThrow(@NotNull CarrierComponent carrier, @NotNull World world) {
	}
}
