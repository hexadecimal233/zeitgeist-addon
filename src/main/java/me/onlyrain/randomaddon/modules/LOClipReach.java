package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.RandomAddon;
import me.onlyrain.randomaddon.utils.ClipUtils;
import meteordevelopment.meteorclient.events.meteor.MouseButtonEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class LOClipReach extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

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

    public LOClipReach() {
        super(RandomAddon.CATEGORY, "lo-clip-reach", "Reach target by tping up -> straight -> down.");
    }

    @EventHandler
    private void onMouseButton(MouseButtonEvent event) {
        if (event.action == KeyAction.Press && mc.currentScreen == null) {
            Optional<Entity> entity = DebugRenderer.getTargetedEntity(mc.player, maxDistance.get());
            entity.ifPresent(e -> {
                if (canUseReach(mc.player.getPos(), e.getPos())) {
                    switch (event.button) {
                        case GLFW.GLFW_MOUSE_BUTTON_LEFT -> {
                            hitEntity(e);
                            event.cancel();
                        }
                        case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> {
                            interactAtEntity(e);
                            event.cancel();
                        }
                    }
                }
            });

        }
    }

    private boolean canUseReach(Vec3d me, Vec3d entity) {
        return entity.distanceTo(me.add(0, mc.player.getStandingEyeHeight(), 0)) >= enableDistance.get();
    }

    public void hitEntity(Entity target) {
        Vec3d pos = mc.player.getPos();
        Vec3d targetPos = target.getPos();

        double maxDistance = 99.0D;
        Vec3d diff = pos.subtract(targetPos);
        double flatUp = Math.sqrt(maxDistance * maxDistance - (diff.x * diff.x + diff.z * diff.z));
        double targetUp = flatUp + diff.y;

        for (int i = 0; i < 9; i++) {  // Build up TP range
            ClipUtils.moveTo(pos);
        }
        ClipUtils.moveTo(pos.add(0, maxDistance, 0));  // V-Clip up
        ClipUtils.moveTo(targetPos.add(0, targetUp, 0));  // Can now move freely
        ClipUtils.moveTo(targetPos);  // V-Clip down to target

        mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));  // Hit packet
        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(mc.player.getActiveHand()));  // Serverside animation

        ClipUtils.moveTo(targetPos.add(0, targetUp + 0.01, 0));  // V-Clip up
        ClipUtils.moveTo(pos.add(0, maxDistance + 0.01, 0));  // Can now move freely
        ClipUtils.moveTo(pos);  // V-Clip down to original position
        mc.player.setPosition(pos);  // Set position on client-side
    }

    public void interactAtEntity(Entity target) {
        Vec3d pos = mc.player.getPos();
        Vec3d targetPos = target.getPos();

        double maxDistance = 99.0D;
        Vec3d diff = pos.subtract(targetPos);
        double flatUp = Math.sqrt(maxDistance * maxDistance - (diff.x * diff.x + diff.z * diff.z));
        double targetUp = flatUp + diff.y;

        for (int i = 0; i < 9; i++) {  // Build up TP range
            ClipUtils.moveTo(pos);
        }
        ClipUtils.moveTo(pos.add(0, maxDistance, 0));  // V-Clip up
        ClipUtils.moveTo(targetPos.add(0, targetUp, 0));  // Can now move freely
        ClipUtils.moveTo(targetPos, mc.player.getYaw(), 90);  // V-Clip down to target (looking down)

        mc.getNetworkHandler().sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));  // Interact item packet
        mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.interact(target, mc.player.isSneaking(), Hand.MAIN_HAND));  // Interact entity packet
        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(mc.player.getActiveHand()));  // Serverside animation

        ClipUtils.moveTo(targetPos.add(0, targetUp + 0.01, 0), mc.player.getYaw(), mc.player.getPitch());  // V-Clip up (looking normal)
        ClipUtils.moveTo(pos.add(0, maxDistance + 0.01, 0));  // Can now move freely
        ClipUtils.moveTo(pos);  // V-Clip down to original position
        mc.player.setPosition(pos);  // Set position on client-side
    }
}
