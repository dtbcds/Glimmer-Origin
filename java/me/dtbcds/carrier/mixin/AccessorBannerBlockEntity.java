package me.dtbcds.carrier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.util.DyeColor;

@Mixin(BannerBlockEntity.class)
public interface AccessorBannerBlockEntity {
    @Accessor
    void setBaseColor(DyeColor color);
}
