package me.dtbcds.carrier.impl;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class BallEntity extends ArmorStandEntity implements IAnimatable{
	private int lifeTime;
	private AnimationFactory factory = new AnimationFactory(this);
	public AnimationBuilder idle = new AnimationBuilder().addAnimation("load.anim" , true);

	public BallEntity(EntityType<? extends ArmorStandEntity> type, World world) {
		super(type, world);
		this.setCustomName(Text.of(""));
		this.setCustomNameVisible(false);
	}
	@Override
	public boolean isMarker() {
		return true;
	}
	@Override
	public void tick() {
		super.tick();
		lifeTime++;
		if(lifeTime>45) {
			this.remove(RemovalReason.KILLED);
		}
	}
	private <E extends IAnimatable> PlayState animate(AnimationEvent<E> event) {
            event.getController().setAnimation(idle);
            return PlayState.CONTINUE;
	}
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<BallEntity>(this, "controller", 0, this::animate));
		
	}
	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
}
