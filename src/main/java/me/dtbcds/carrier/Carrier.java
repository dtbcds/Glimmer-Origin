package me.dtbcds.carrier;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import me.dtbcds.carrier.api.Carriable;
import me.dtbcds.carrier.api.CarriablePlacementContext;
import me.dtbcds.carrier.api.CarriableRegistry;
import me.dtbcds.carrier.api.CarrierComponent;
import me.dtbcds.carrier.api.CarrierPlayerExtension;
import me.dtbcds.carrier.impl.BallEntity;
import me.dtbcds.carrier.impl.CarriableBanner;
import me.dtbcds.carrier.impl.CarriableChest;
import me.dtbcds.carrier.impl.CarriableChicken;
import me.dtbcds.carrier.impl.CarriableCow;
import me.dtbcds.carrier.impl.CarriableEnchantingTable;
import me.dtbcds.carrier.impl.CarriableGeneric;
import me.dtbcds.carrier.impl.CarriableParrot;
import me.dtbcds.carrier.impl.CarriablePig;
import me.dtbcds.carrier.impl.CarriableRabbit;
import me.dtbcds.carrier.impl.CarriableSheep;
import me.dtbcds.carrier.impl.CarriableSpawner;
import me.dtbcds.carrier.impl.CarriableTurtle;
import me.dtbcds.carrier.impl.CarriableWolf;
import me.dtbcds.carrier.registry.ModPowers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("deprecation")
public class Carrier implements ModInitializer, EntityComponentInitializer {

    public static final ComponentKey<CarrierComponent> HOLDER = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("carrier", "holder"), CarrierComponent.class);

    public static final String MOD_ID = "carrier";

    public static Config CONFIG = new Config();

    public static final Identifier SET_CAN_CARRY_PACKET = new Identifier(MOD_ID, "can_carry_packet");
    
    public static final Identifier THROW = new Identifier(MOD_ID, "throw");

    public static final TagKey<Block> BLACKLIST = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "blacklist"));
    
    public static final EntityType<BallEntity> BALL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "ball"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BallEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );

    @Override
    public void onInitialize() {
    	ModPowers.init();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "carrier.json");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) throw new IOException("Failed to create file");
                FileUtils.write(file, gson.toJson(CONFIG), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LogManager.getLogger("Carrier").error("Failed to create carrier config");
                throw new RuntimeException(e);
            }
        } else {
            try {
                String lines = String.join("\n", FileUtils.readLines(file, StandardCharsets.UTF_8));
                CONFIG = gson.fromJson(lines, Config.class);
            } catch (IOException e) {
                LogManager.getLogger("Carrier").error("Failed to read config");
                throw new RuntimeException(e);
            }
        }

        ServerTickEvents.END_WORLD_TICK.register(new ServerWorldTickCallback());
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_cow"), new CarriableCow());
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_chicken"), new CarriableChicken());
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_parrot"), new CarriableParrot());
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_pig"), new CarriablePig());
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_rabbit"), new CarriableRabbit());
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_sheep"), new CarriableSheep());
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_turtle"), new CarriableTurtle());
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_wolf"), new CarriableWolf());
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_spawner"), new CarriableSpawner(new Identifier(MOD_ID, "minecraft_spawner")));
        CarriableRegistry.INSTANCE.register(new Identifier(MOD_ID, "minecraft_enchanting_table"), new CarriableEnchantingTable(new Identifier(MOD_ID, "minecraft_enchanting_table")));
        
        Registry.BLOCK.forEach((block) -> {
            Identifier id = Registry.BLOCK.getId(block);
            Identifier type = new Identifier("carrier", id.getNamespace() + "_" + id.getPath());
            registerGenericCarriable(block, type);
        });
        RegistryEntryAddedCallback.event(Registry.BLOCK).register((rawId, id, block) -> {
            Identifier type = new Identifier("carrier", id.getNamespace() + "_" + id.getPath());
            registerGenericCarriable(block, type);
        });

        ServerSidePacketRegistry.INSTANCE.register(SET_CAN_CARRY_PACKET, (ctx, buf) -> {
            boolean canCarry = buf.readBoolean();
            ctx.getTaskQueue().execute(() ->
                    ((CarrierPlayerExtension) ctx.getPlayer()).setCanCarry(canCarry));
        });
        ServerSidePacketRegistry.INSTANCE.register(THROW, (ctx, buf) -> {
            ctx.getTaskQueue().execute(() -> {
                    HolderInteractCallback.INSTANCE.throwInteract(ctx.getPlayer(), ctx.getPlayer().world);
            });
        });

        CommandRegistrationCallback.EVENT.register((commandDispatcher, b) ->
                commandDispatcher.register(CommandManager.literal("carrierinfo")
                        .executes((ctx) -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                            NbtCompound tag = new NbtCompound();
                            HOLDER.get(player).writeToNbt(tag);
                            ctx.getSource().sendFeedback(new LiteralText(tag.toString()), false);
                            return 1;
                        })));

        CommandRegistrationCallback.EVENT.register((commandDispatcher, b) ->
                commandDispatcher.register(CommandManager.literal("carrierdelete")
                        .executes((ctx) -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                            CarrierComponent component = HOLDER.get(player);
                            NbtCompound tag = new NbtCompound();
                            component.writeToNbt(tag);
                            component.setCarryingData(null);
                            ctx.getSource().sendFeedback(new LiteralText("Deleted ").append(new LiteralText(tag.toString()).setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, tag.toString())))), false);
                            return 1;
                        })));

        CommandRegistrationCallback.EVENT.register((commandDispatcher, b) ->
                commandDispatcher.register(CommandManager.literal("carrierplace")
                        .executes((ctx) -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                            CarrierComponent component = HOLDER.get(player);
                            Carriable<Object> carriable = CarriableRegistry.INSTANCE.get(component.getCarryingData().getType());
                            BlockPos pos = player.getBlockPos().offset(player.getHorizontalFacing());
                            ServerWorld world = ctx.getSource().getWorld();
                            if (!world.getBlockState(pos).getMaterial().isReplaceable()) {
                                ctx.getSource().sendFeedback(new LiteralText("Could not place! Make sure you have empty space in front of you."), false);
                                return 1;
                            }
                            CarriablePlacementContext placementCtx = new CarriablePlacementContext(component, carriable, pos, player.getHorizontalFacing().getOpposite(), player.getHorizontalFacing());
                            carriable.tryPlace(component, world, placementCtx);
                            component.setCarryingData(null);
                            return 1;
                        })));
        FabricDefaultAttributeRegistry.register(BALL, BallEntity.createLivingAttributes());
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(HOLDER, CarrierComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

    public static boolean canCarry(Identifier id) {
        if (Registry.BLOCK.getOrEmpty(id).map((b) -> b.getRegistryEntry().isIn(BLACKLIST)).orElse(false)) return false;

        if (CONFIG.getType() == Config.ListType.WHITELIST) return CONFIG.getList().stream().anyMatch((s) -> Pattern.compile(s).matcher(id.toString()).find());
        else return CONFIG.getList().stream().noneMatch((s) -> Pattern.compile(s).matcher(id.toString()).find());
    }

    public static boolean isHoldingKey(PlayerEntity playerEntity) {
        return playerEntity instanceof CarrierPlayerExtension && ((CarrierPlayerExtension) playerEntity).canCarry();
    }

    private static void registerGenericCarriable(Block block, Identifier type) {
            if (!CarriableRegistry.INSTANCE.contains(block)) {
                if (block instanceof AbstractChestBlock<?>)
                    CarriableRegistry.INSTANCE.register(type, new CarriableChest(type, block));
                else if (block instanceof AbstractBannerBlock)
                    CarriableRegistry.INSTANCE.register(type, new CarriableBanner(type, block));
                else if (!(block instanceof CropBlock) && !(block instanceof BedBlock) && !(block instanceof DoorBlock) && block != Blocks.BEDROCK && block != Blocks.OBSIDIAN)
                    CarriableRegistry.INSTANCE.register(type, new CarriableGeneric(type, block));
        }
    }
}
