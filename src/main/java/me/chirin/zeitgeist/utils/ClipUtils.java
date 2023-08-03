package me.chirin.zeitgeist.utils;

import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class ClipUtils {
    public static void moveTo(Vec3d pos) {
        if (mc.player.getVehicle() != null) {
            mc.player.getVehicle().setPosition(pos);
            mc.getNetworkHandler().sendPacket(new VehicleMoveC2SPacket(mc.player.getVehicle()));
        } else {
            mc.player.setPosition(pos);
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, true));
        }
    }

    public static void moveTo(Vec3d pos, float yaw, float pitch) {
        if (mc.player == null) return;

        if (mc.player.getVehicle() != null) {
            mc.player.getVehicle().setPosition(pos);
            mc.getNetworkHandler().sendPacket(new VehicleMoveC2SPacket(mc.player.getVehicle()));
        } else {
            mc.player.setPosition(pos);
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(
                pos.x, pos.y, pos.z,
                yaw, pitch,
                mc.player.isOnGround()));
        }
    }

    public static void clipStraight(Vec3d targetPos) {
        Vec3d pos = mc.player.getPos();

        for (int i = 0; i < 18; i++) {  // Send a lot of the same movement packets to increase max travel distance
            moveTo(pos);
        }
        // Send one big movement packet to actually move the player
        moveTo(targetPos);
    }

    // TODO: maybe refactor this to be the same as ClipReach#hitEntity
    public static void clipUpDown(Vec3d targetPos) {
        Vec3d pos = mc.player.getPos();

        for (int i = 0; i < 15; i++) {  // Send a lot of the same movement packets to increase max travel distance
            moveTo(pos);
        }

        pos = pos.add(0, 100, 0);  // Up
        moveTo(pos);

        pos = new Vec3d(targetPos.x, pos.y, targetPos.z);  // Horizontal
        moveTo(pos);

        moveTo(targetPos);  // Down
    }

    public static Vec3d getAutoClipPos(int direction) {
        boolean inside = false;
        for (float i = 0; i < 150; i += 0.25) {
            Vec3d pos = mc.player.getPos();
            Vec3d targetPos = pos.add(0, direction * i, 0);

            boolean collides = !mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(targetPos.subtract(pos)));

            if (!inside && collides) {  // Step 1: Into the blocks
                inside = true;
            } else if (inside && !collides) {  // Step 2: Out of the blocks
                return targetPos;
            }
        }

        return null;  // Nothing found
    }

    public static int executeAutoClip(int direction) {
        Vec3d pos = getAutoClipPos(direction);
        if (pos == null) {
            ChatUtils.error("No valid position found within 150 blocks");
        } else {
            ChatUtils.info(String.format("Clipping (highlight)%.0f(default) blocks", pos.y - (int) mc.player.getPos().y));
            clipStraight(pos);
        }
        return 1;
    }
}
