package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.util.math.Vec3d;

public class VelocityBoost extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("set or add")
        .defaultValue(Modes.Set)
        .build());

    private final Setting<Double> boost = sgGeneral.add(new DoubleSetting.Builder()
        .name("boost")
        .description("the boost")
        .defaultValue(0)
        .sliderRange(-10, 10)
        .build());

    private final Setting<Double> yboost = sgGeneral.add(new DoubleSetting.Builder()
        .name("y")
        .description("boost in y")
        .defaultValue(0)
        .sliderRange(-10, 10)
        .build());

    public VelocityBoost() {
        super(Random.CATEGORY, "velocity-boost", "changes your velocity (much faster than you think)");
    }

    @Override
    public void onActivate() {
        Vec3d sex = Vec3d.fromPolar(0, mc.player.getYaw()).normalize().multiply(boost.get());
        if (mode.get() == Modes.Set) mc.player.setVelocity(sex);
        else mc.player.addVelocity(sex.x, yboost.get(), sex.z);
        toggle();
    }

    public enum Modes {
        Set, Add
    }
}
