package me.dtbcds.carrier;

import me.dtbcds.carrier.api.BallEntityModel;
import me.dtbcds.carrier.api.BallEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@SuppressWarnings("deprecation")
@Environment(EnvType.CLIENT)
public class CarrierClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_BALL_LAYER = new EntityModelLayer(new Identifier("ballentity", "ball"), "main");
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(Carrier.BALL, (context) -> {
            return new BallEntityRenderer(context);
        });
		EntityModelLayerRegistry.registerModelLayer(MODEL_BALL_LAYER, BallEntityModel::getTexturedModelData);
	}
}