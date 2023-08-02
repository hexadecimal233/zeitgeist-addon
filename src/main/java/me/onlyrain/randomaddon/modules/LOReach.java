package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import meteordevelopment.meteorclient.events.meteor.MouseButtonEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;

public class LOReach extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> packetAmount = sgGeneral.add(new IntSetting.Builder()
        .name("packet-amount")
        .description("How many packets to send per tick.")
        .defaultValue(3)
        .min(1)
        .build()
    );

    private final Setting<Double> enableDistance = sgGeneral.add(new DoubleSetting.Builder()
        .name("enable-distance")
        .description("Minimum reach distance.")
        .defaultValue(4)
        .sliderMin(3)
        .max(6)
        .build()
    );

    private final Setting<Integer> maxDistance = sgGeneral.add(new IntSetting.Builder()
        .name("max-distance")
        .description("Maximum reach distance.")
        .defaultValue(100)
        .sliderMin(6)
        .max(100)
        .build()
    );
    private final LinkedList<Packet<?>> whitelistedPackets = new LinkedList<>();
    private Vec3d realPos;
    private Entity target;
    private Vec3d targetPos;
    private Status phase = Status.STANDBY;

    public LOReach() {
        super(Random.CATEGORY, "lo-reach", "Doing reach by tping you closer to your target and back.");
    }

    @Override
    public void onActivate() {
        phase = Status.STANDBY;
        whitelistedPackets.clear();
    }

    // Get target and cancel doAttack
    @EventHandler
    private void onMouseButton(MouseButtonEvent event) {
        if (event.action == KeyAction.Press && event.button == GLFW.GLFW_MOUSE_BUTTON_LEFT && mc.currentScreen == null) {
            DebugRenderer.getTargetedEntity(mc.player, maxDistance.get()).ifPresent(e -> {
                if (phase == Status.STANDBY && canUseReach(mc.player.getPos(), e.getPos())) {
                    realPos = mc.player.getPos();
                    target = e;
                    // Subtract a bit from the end
                    targetPos = e.getPos().subtract(e.getPos().subtract(realPos).normalize().multiply(2));
                    phase = Status.FORWARD;
                    whitelistedPackets.clear();
                    event.cancel();
                }
            });
        }
    }

    // Prevent unexpected movement packets
    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (phase == Status.STANDBY) return;
        if (event.packet instanceof PlayerMoveC2SPacket packet && !whitelistedPackets.contains(packet)) event.cancel();
    }

    // Move closer & move back
    @EventHandler
    private void onPostTick(TickEvent.Post event) {
        switch (phase) {
            case FORWARD -> {
                for (int i = 0; i < packetAmount.get(); i++) {
                    if (sendMovePackets(realPos, targetPos)) {
                        // Swing hand and attack, prepare for going back
                        mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
                        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(mc.player.getActiveHand()));
                        mc.player.resetLastAttackedTicks();
                        phase = Status.BACKWARD;
                        break;
                    }
                }
            }
            case BACKWARD -> {
                for (int i = 0; i < packetAmount.get(); i++) {
                    if (sendMovePackets(realPos, mc.player.getPos())) {
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true));
                        phase = Status.STANDBY;
                        break;
                    }
                }
            }
        }
    }

    private boolean sendMovePackets(Vec3d fromPos, Vec3d targetPos) {
        // Move only if you cannot attack your target
        if (canUseReach(realPos, targetPos)) {
            // Length squared is max 100 (otherwise "moved too quickly")
            Vec3d movement = targetPos.subtract(fromPos);
            if (movement.lengthSquared() >= 100) {
                movement = movement.normalize().multiply(9.9);
            }
            Vec3d realPos = fromPos.add(movement);
            PlayerMoveC2SPacket movePacket = new PlayerMoveC2SPacket.PositionAndOnGround(realPos.x, realPos.y, realPos.z, true);
            whitelistedPackets.add(movePacket);
            mc.player.networkHandler.sendPacket(movePacket);
            this.realPos = realPos;
            return false;
        } else {
            return true;
        }
    }

    private boolean canUseReach(Vec3d me, Vec3d entity) {
        return entity.distanceTo(me.add(0, mc.player.getStandingEyeHeight(), 0)) >= enableDistance.get();
    }

    public enum Status {
        STANDBY,
        FORWARD,
        BACKWARD,
    }
}
