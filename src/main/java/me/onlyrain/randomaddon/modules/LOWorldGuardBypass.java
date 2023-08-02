package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class LOWorldGuardBypass extends Module {
    private static final double MAX_DELTA = 0.05001;  // Absolute maximum is sqrt(1/256) = 0.0625
    int flyingTimer = 0;

    public LOWorldGuardBypass() {
        super(Random.CATEGORY, "lo-worldguard-bypass", "wg bypass glitchy");
    }

    public static boolean inSameBlock(Vec3d vector, Vec3d other) {
        return other.x >= Math.floor(vector.x) && other.x <= Math.ceil(vector.x) &&
            other.y >= Math.floor(vector.y) && other.y <= Math.ceil(vector.y) &&
            other.z >= Math.floor(vector.z) && other.z <= Math.ceil(vector.z);
    }

    @Override
    public void onDeactivate() {
        flyingTimer = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        mc.player.setVelocity(0, 0, 0);

        if (++flyingTimer > 20) {  // Max 80, to bypass "Flying is not enabled"
            Vec3d pos = mc.player.getPos();
            pos = pos.add(0, -MAX_DELTA, 0);  // Small down position

            mc.player.setPosition(pos.x, pos.y, pos.z);
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, true));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(pos.x + 1337.0, pos.y + 1337.0,  // Far packet again to keep bypassing
                pos.z + 1337.0, mc.player.getYaw(), mc.player.getPitch(), true));

            flyingTimer = 0;  // Reset
        } else {
            Vec3d vec = getMovementVec();
            assert vec != null;

            if (vec.length() > 0) {
                if (!(vec.x == 0 && vec.z == 0)) {  // Rotate by looking yaw (won't change length)
                    double moveAngle = Math.atan2(vec.x, vec.z) + Math.toRadians(mc.player.getYaw() + 90);
                    double x = Math.cos(moveAngle);
                    double z = Math.sin(moveAngle);
                    vec = new Vec3d(x, vec.y, z);
                }

                vec = vec.multiply(MAX_DELTA);  // Scale to maxDelta

                Vec3d newPos = new Vec3d(mc.player.getX() + vec.x, mc.player.getY() + vec.y, mc.player.getZ() + vec.z);
                if (collides(newPos)) return;  // Don't move if it would collide

                // If able to add more without going over a block boundary, add more
                boolean extra = false;
                if (mc.options.sprintKey.isPressed()) {  // Trigger by sprinting
                    // If doesn't cross block boundary, and doesn't collide with anything, add more
                    while (inSameBlock(newPos.add(vec.multiply(1.5)), new Vec3d(mc.player.prevX, mc.player.prevY, mc.player.prevZ)) &&
                        !collides(newPos.add(vec.multiply(1.5)))) {
                        newPos = newPos.add(vec);
                        extra = true;
                    }
                }

                mc.player.setPosition(newPos);

                // Send tiny movement so delta is small enough
                PlayerMoveC2SPacket.Full smallMovePacket = new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(),
                    mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), true);
                mc.getNetworkHandler().sendPacket(smallMovePacket);

                // Send far away packet for "moving too quickly!" to reset position
                if (!extra) {
                    PlayerMoveC2SPacket.Full farPacket = new PlayerMoveC2SPacket.Full(mc.player.getX() + 1337.0, mc.player.getY() + 1337.0,
                        mc.player.getZ() + 1337.0, mc.player.getYaw(), mc.player.getPitch(), true);
                    mc.getNetworkHandler().sendPacket(farPacket);
                }
            }
        }
    }

    public boolean collides(Vec3d pos) {
        return !mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(pos.subtract(mc.player.getPos())));
    }

    public Vec3d getMovementVec() {
        Vec3d vec = new Vec3d(0, 0, 0);

        // Key presses changing position
        if (mc.player.input.jumping) {  // Move up
            vec = vec.add(new Vec3d(0, 1, 0));
        } else if (mc.player.input.sneaking) {  // Move down
            vec = vec.add(new Vec3d(0, -1, 0));
        } else {
            // Horizontal movement (not at the same time as vertical)
            if (mc.player.input.pressingForward) {
                vec = vec.add(new Vec3d(0, 0, 1));
            }
            if (mc.player.input.pressingRight) {
                vec = vec.add(new Vec3d(1, 0, 0));
            }
            if (mc.player.input.pressingBack) {
                vec = vec.add(new Vec3d(0, 0, -1));
            }
            if (mc.player.input.pressingLeft) {
                vec = vec.add(new Vec3d(-1, 0, 0));
            }
        }

        return vec.normalize();
    }
}
