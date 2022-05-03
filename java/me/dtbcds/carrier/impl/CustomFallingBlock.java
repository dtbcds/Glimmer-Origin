package me.dtbcds.carrier.impl;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.world.World;

public class CustomFallingBlock extends FallingBlockEntity{
	private List<Entity> hitEntities= new ArrayList<Entity>();
	private PlayerEntity player;

	public CustomFallingBlock(EntityType<? extends FallingBlockEntity> entityType, World world) {
		super(entityType, world);
	}
	public static CustomFallingBlock fromFallingBlock(FallingBlockEntity entity) {
		return new CustomFallingBlock(EntityType.FALLING_BLOCK, entity.world);
	}
	public void updateNbt(PlayerEntity player, BlockState state) {
		this.player = player;
		NbtCompound tag = new NbtCompound();
		tag.putInt("Time", 1);
		tag.put("BlockState", NbtHelper.fromBlockState(state));
		this.readCustomDataFromNbt(tag);
	}
	@Override
	public void tick() {
		List<Entity> entities= this.world.getOtherEntities(this, this.getBoundingBox());
		for (Entity e : entities) {
			if (!hitEntities.contains(e) && e != player) {
				e.damage(DamageSource.FALLING_BLOCK, 4f);
				hitEntities.add(e);
			}
		}
		super.tick();
	}
}
