package me.dtbcds.carrier.impl;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class BallEntity extends ArmorStandEntity{
	private int lifeTime;

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
}
