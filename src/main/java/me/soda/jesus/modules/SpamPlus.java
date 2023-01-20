package me.soda.jesus.modules;

import me.soda.jesus.Addon;
import me.soda.jesus.util.FileUtil;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Arrays;
import java.util.List;

public class SpamPlus extends Module {

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<String> text = sgGeneral.add(new StringSetting.Builder()
        .name("text-path")
        .defaultValue("text.txt")
        .onChanged(e -> onActivate())
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(20)
        .min(0)
        .sliderMax(200)
        .build()
    );

    private int messageI, timer;
    private List<String> script;

    public SpamPlus() {
        super(Addon.CATEGORY, "spam-plus", "Better than spam.");
    }

    @Override
    public void onActivate() {
        timer = delay.get();
        messageI = 0;
        script = Arrays.stream(FileUtil.read(text.get()).split("\n")).toList();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (timer <= 0) {
            String line;
            line = script.get(messageI);
            ChatUtils.sendPlayerMsg(line);
            timer = delay.get();
            if (messageI++ == script.size() - 1) {
                messageI = 0;
            }
        } else {
            timer--;
        }
    }
}
