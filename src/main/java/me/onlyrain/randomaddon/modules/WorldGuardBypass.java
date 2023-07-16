package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;

public class WorldGuardBypass extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public WorldGuardBypass() {
        super(Random.CATEGORY, "worldguard-bypass", "barely works and is bad");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        Vec3d player = mc.player.getPos();
        Vec3d owo = Vec3d.fromPolar(0, mc.player.getYaw()).normalize().multiply(0.0625);
        Vec3d left = Vec3d.fromPolar(0, mc.player.getYaw() - 90).normalize().multiply(0.0625);
        Vec3d right = Vec3d.fromPolar(0, mc.player.getYaw() + 90).normalize().multiply(0.0625);

        if (mc.options.forwardKey.isPressed()) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.setPosition(player.x + owo.x, 1000, player.x + owo.z);
        }
        if (mc.options.backKey.isPressed()) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.setPosition(player.x - owo.x, 1000, player.x - owo.z);
        }
        if (mc.options.leftKey.isPressed()) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.setPosition(player.x + left.x, 1000, player.x + left.z);
        }
        if (mc.options.rightKey.isPressed()) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.setPosition(player.x + right.x, 1000, player.x + right.z);
        }
        if (mc.options.jumpKey.isPressed()) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.setPosition(player.x, player.y + 0.0625, player.z + 99);
        }
        if (mc.options.sneakKey.isPressed()) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.setPosition(player.x, player.y - 0.0625, player.z + 99);
        }

        mc.player.setVelocity(0, 0, 0);
    }
}
