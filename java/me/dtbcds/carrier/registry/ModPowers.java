package me.dtbcds.carrier.registry;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import me.dtbcds.carrier.Carrier;
import me.dtbcds.carrier.power.CarryPower;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModPowers {
	public static final PowerFactory<Power> CARRY = new PowerFactory<>(new Identifier(Carrier.MOD_ID, "carry"),
			new SerializableData(), data -> (type, entity) -> new CarryPower(type, entity)).allowCondition();
	public static void init() {
		Registry.register(ApoliRegistries.POWER_FACTORY, CARRY.getSerializerId(), CARRY);
	}
}
