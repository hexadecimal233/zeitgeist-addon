package me.soda.randomaddon.modules;

import me.soda.randomaddon.Random;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class Jetpack extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> jetpackSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("jetpack-speed")
        .description("How fast while ascending.")
        .defaultValue(0.42)
        .min(0)
        .sliderMax(1)
        .build()
    );

    public Jetpack() {
        super(Random.CATEGORY, "jetpack", "Flies as if using a jetpack.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.options.jumpKey.isPressed()) {
            mc.player.setVelocity(mc.player.getVelocity().x * 1.1, jetpackSpeed.get(), mc.player.getVelocity().z * 1.1);
        }
    }
}

