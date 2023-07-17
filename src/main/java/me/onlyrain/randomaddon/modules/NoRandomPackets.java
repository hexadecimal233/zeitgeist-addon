package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;

public class NoRandomPackets extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Boolean> demo = sgGeneral.add(new BoolSetting.Builder()
        .name("less-annoying-bats")
        .defaultValue(false)
        .build());

    public final Setting<Boolean> fakeCreative = sgGeneral.add(new BoolSetting.Builder()
        .name("less-annoying-bats")
        .defaultValue(false)
        .build());

    public final Setting<Boolean> end = sgGeneral.add(new BoolSetting.Builder()
        .name("less-annoying-bats")
        .defaultValue(false)
        .build());

    public NoRandomPackets(){
        super(Random.CATEGORY, "no-random-packets", "No random packets.");
    }
}
