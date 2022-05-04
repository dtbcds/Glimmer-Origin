package me.dtbcds.carrier.api;

import me.dtbcds.carrier.impl.BallEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BallEntityRenderer extends GeoEntityRenderer<BallEntity> {

	public BallEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new BallEntityModel());
	}
	 
//    public BallEntityRenderer(EntityRendererFactory.Context context) {
//        super(context, new BallEntityModel(context.getPart(CarrierClient.MODEL_BALL_LAYER)), 0.5f);
//    }
// 
//    @Override
//    public Identifier getTexture(BallEntity entity) {
//        return new Identifier("carrier", "textures/entity/ball.png");
//    }
}
