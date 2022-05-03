package me.dtbcds.carrier.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import me.dtbcds.carrier.api.CarrierPlayerExtension;
import net.minecraft.entity.LivingEntity;

public class CarryPower extends Power{
	private final int tickRate = 5;

	public CarryPower(PowerType<?> type, LivingEntity entity) {
		super(type, entity);
		this.setTicking(true);
	}
	
	@Override
    public void tick() {
        if(entity.age % tickRate == 0) {
            if(this.isActive()) {
                addMods();
            } else {
                removeMods();
            }
        }
    }
	@Override
    public void onRemoved() {
        removeMods();
    }
	
	public void addMods() {
		((CarrierPlayerExtension) entity).setCanCarry(true);
	}
	public void removeMods() {
		((CarrierPlayerExtension) entity).setCanCarry(false);
	}


}
