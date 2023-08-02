package me.onlyrain.randomaddon.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.onlyrain.randomaddon.commands.arguements.PositionArgumentType;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class TeleportCommand extends Command {
    public TeleportCommand() {
        super("crystal-teleport", "Allows to teleport small distances.", "tp");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        LiteralArgumentBuilder<CommandSource> then = builder.then(argument("pos", PositionArgumentType.pos()).executes(context -> {
            Vec3d pos = PositionArgumentType.getPos(context, "pos");

            assert mc.player != null;
            if (!mc.player.hasVehicle()) {
            } else {
                Entity vehicle = mc.player.getVehicle();

                assert vehicle != null;
                vehicle.setPosition(pos.getX(), pos.getY(), pos.getZ());
                Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new VehicleMoveC2SPacket(vehicle));
            }

            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), mc.player.isOnGround()));
            mc.player.updatePosition(pos.getX(), pos.getY(), pos.getZ());

            return SINGLE_SUCCESS;
        }));

        builder.then(argument("pos", PositionArgumentType.pos()).then(argument("yaw", FloatArgumentType.floatArg()).then(argument("pitch", FloatArgumentType.floatArg()).executes(context -> {
            Vec3d pos = PositionArgumentType.getPos(context, "pos");

            float yaw = FloatArgumentType.getFloat(context, "yaw");
            float pitch = FloatArgumentType.getFloat(context, "pitch");

            assert mc.player != null;
            if (!mc.player.hasVehicle()) {
            } else {
                Entity vehicle = mc.player.getVehicle();

                assert vehicle != null;
                vehicle.setPosition(pos.getX(), pos.getY(), pos.getZ());
                Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new VehicleMoveC2SPacket(vehicle));
            }

            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch, mc.player.isOnGround()));
            mc.player.updatePositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);

            return SINGLE_SUCCESS;
        }))));


        builder.then(argument("pos", PositionArgumentType.pos()).then(argument("ticks", IntegerArgumentType.integer(0)).executes(context -> {
            Vec3d pos = PositionArgumentType.getPos(context, "pos");
            int ticks = IntegerArgumentType.getInteger(context, "ticks");

            int i = 0;
            while (true) {
                if (i < (ticks <= 0 ? 1 : ticks)) {
                    assert mc.player != null;
                    if (!mc.player.hasVehicle()) {
                    } else {
                        Entity vehicle = mc.player.getVehicle();

                        assert vehicle != null;
                        vehicle.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new VehicleMoveC2SPacket(vehicle));
                    }

                    Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), mc.player.isOnGround()));
                    mc.player.updatePosition(pos.getX(), pos.getY(), pos.getZ());
                    i++;
                } else {
                    break;
                }
            }

            return SINGLE_SUCCESS;
        })));

        builder.then(argument("pos", PositionArgumentType.pos()).then(argument("yaw", FloatArgumentType.floatArg()).then(argument("pitch", FloatArgumentType.floatArg()).then(argument("ticks", IntegerArgumentType.integer(0)).executes(context -> {
            Vec3d pos = PositionArgumentType.getPos(context, "pos");
            int ticks = IntegerArgumentType.getInteger(context, "ticks");

            float yaw = FloatArgumentType.getFloat(context, "yaw");
            float pitch = FloatArgumentType.getFloat(context, "pitch");

            int i = 0;
            while (true) {
                if (i < (ticks <= 0 ? 1 : ticks)) {
                    assert mc.player != null;
                    if (!mc.player.hasVehicle()) {
                    } else {
                        Entity vehicle = mc.player.getVehicle();

                        assert vehicle != null;
                        vehicle.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new VehicleMoveC2SPacket(vehicle));
                    }

                    Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.Full(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch, mc.player.isOnGround()));
                    mc.player.updatePositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
                    i++;
                } else {
                    break;
                }
            }

            return SINGLE_SUCCESS;
        })))));
    }
}
