package me.dtbcds.carrier.api;

import me.dtbcds.carrier.CarrierClient;
import me.dtbcds.carrier.impl.BallEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;

public class BallEntityRenderer extends LivingEntityRenderer<BallEntity, BallEntityModel> {
	 
    public BallEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BallEntityModel(context.getPart(CarrierClient.MODEL_BALL_LAYER)), 0.5f);
    }
 
    @Override
    public Identifier getTexture(BallEntity entity) {
        return new Identifier("carrier", "textures/entity/ball.png");
    }
}