package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class VeloFly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("how fast")
        .defaultValue(1)
        .sliderRange(0, 10)
        .build());

    private final Setting<Boolean> entityfly = sgGeneral.add(new BoolSetting.Builder()
        .name("entity-fly")
        .description("Allows you to fly while riding an entity")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> antikick = sgGeneral.add(new BoolSetting.Builder()
        .name("anti-kick")
        .description("Stops you from being kicked")
        .defaultValue(false)
        .build());

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("delay between going down")
        .defaultValue(69)
        .sliderRange(1, 80)
        .visible(antikick::get)
        .build());
    int timer = 0;

    public VeloFly() {
        super(Random.CATEGORY, "velo-fly", "flight using velocity");
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        mc.player.getAbilities().flying = false;
        Entity vehicle = mc.player.getVehicle();

        Vec3d owo = Vec3d.fromPolar(0, mc.player.getYaw()).normalize().multiply(speed.get() / 10);
        Vec3d left = Vec3d.fromPolar(0, mc.player.getYaw() - 90).normalize().multiply(speed.get() / 10);
        Vec3d right = Vec3d.fromPolar(0, mc.player.getYaw() + 90).normalize().multiply(speed.get() / 10);

        if (timer >= delay.get() && antikick.get() && vehicle == null) { // pls help :((((
            mc.player.setVelocity(mc.player.getVelocity().x, -0.5, mc.player.getVelocity().z);
            timer = 0;
        }

        if (vehicle == null) {
            if (mc.options.forwardKey.isPressed())
                mc.player.addVelocity(owo.x, 0, owo.z);
            if (mc.options.backKey.isPressed())
                mc.player.addVelocity(-owo.x, 0, -owo.z);
            if (mc.options.jumpKey.isPressed())
                mc.player.addVelocity(0, speed.get() / 10, 0);
            if (mc.options.sneakKey.isPressed())
                mc.player.addVelocity(0, -speed.get() / 10, 0);
            if (mc.options.leftKey.isPressed())
                mc.player.addVelocity(left.x, 0, left.z);
            if (mc.options.rightKey.isPressed())
                mc.player.addVelocity(right.x, 0, right.z);
            if (!mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed())
                mc.player.addVelocity(0, -mc.player.getVelocity().y, 0);

        } else if (vehicle != null && entityfly.get()) {
            if (mc.options.forwardKey.isPressed())
                vehicle.addVelocity(owo.x, 0, owo.z);
            if (mc.options.backKey.isPressed())
                vehicle.addVelocity(-owo.x, 0, -owo.z);
            if (mc.options.jumpKey.isPressed())
                vehicle.addVelocity(0, speed.get() / 10, 0);
            if (mc.options.sneakKey.isPressed())
                vehicle.addVelocity(0, -speed.get() / 10, 0);
            if (mc.options.leftKey.isPressed())
                vehicle.addVelocity(left.x, 0, left.z);
            if (mc.options.rightKey.isPressed())
                vehicle.addVelocity(right.x, 0, right.z);
            if (!mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed())
                vehicle.addVelocity(0, -vehicle.getVelocity().y, 0);
        }
        timer++;
    }

    @Override
    public void onActivate() {
        timer = 0;
    }
}
