package me.dtbcds.carrier.api;

import com.google.common.collect.ImmutableList;

import me.dtbcds.carrier.impl.BallEntity;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;

public class BallEntityModel extends EntityModel<BallEntity> {
	 
    private final ModelPart base;
 
    public BallEntityModel(ModelPart modelPart) {
        this.base = modelPart.getChild(EntityModelPartNames.CUBE);
        this.base.setPivot(0.0F, 24.0F, 0.0F);
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
    	ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(96, 22).cuboid(-4.0F, -16.0F, -4.0F, 8.0F, 8.0F, 8.0F, false).uv(88, 38).cuboid(-5.0F, -17.0F, -5.0F, 10.0F, 10.0F, 10.0F, false), ModelTransform.pivot(0F, 0F, 0F));
        return TexturedModelData.of(modelData, 256, 256);
    }
    @Override
    public void setAngles(BallEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }
 
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.base).forEach((modelRenderer) -> {
            modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
    }
}