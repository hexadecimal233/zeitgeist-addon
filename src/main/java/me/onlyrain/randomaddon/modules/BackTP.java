package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class BackTP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .defaultValue(3)
        .sliderRange(1, 5)
        .build());

    public BackTP() {
        super(Random.CATEGORY, "back-tp", "Teleports behind the entity you look at");
    }

    @Override
    public void onActivate() {
        Entity target = mc.targetedEntity;
        if (target == null) {
            toggle();
            return;
        }

        Vec3d ppos = target.getPos();
        Vec3d tp = Vec3d.fromPolar(0, mc.player.getYaw()).normalize().multiply(range.get());
        mc.player.setPosition(new Vec3d(ppos.x + tp.x, ppos.y, ppos.z + tp.z));
        float yaw = mc.player.getYaw();
        mc.player.setYaw(-yaw);
        toggle();
    }
}
