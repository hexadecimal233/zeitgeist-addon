package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.starscript.Script;

import java.util.List;

public class AutoMessage extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Integer> delay1 = sgGeneral.add(new IntSetting.Builder()
        .name("message-delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(6000)
        .min(0)
        .noSlider()
        .build()
    );
    private final Setting<Integer> delay2 = sgGeneral.add(new IntSetting.Builder()
        .name("message2-delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(6000)
        .min(0)
        .noSlider()
        .build()
    );
    private int timer1 = 0;
    private int timer2 = 0;
    private List<Script> scripts1;
    private final Setting<List<String>> messages1 = sgGeneral.add(new StringListSetting.Builder()
        .name("messages1")
        .description("Messages to send.")
        .defaultValue(List.of("Meteor on Crack!"))
        .onChanged(this::recompileM1)
        .build()
    );
    private List<Script> scripts2;
    private final Setting<List<String>> messages2 = sgGeneral.add(new StringListSetting.Builder()
        .name("messages2")
        .description("Messages to send.")
        .defaultValue(List.of("Meteor on Crack!"))
        .onChanged(this::recompileM2)
        .build()
    );

    public AutoMessage() {
        super(Random.CATEGORY, "auto-message", "Sends a configurable Starscript message every so often.");
    }

    @Override
    public void onActivate() {
        timer1 = delay1.get();
        timer2 = delay2.get();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (messages1.get().isEmpty() && messages2.get().isEmpty()) return;

        if (timer1 <= 0) {
            scripts1.forEach(script -> ChatUtils.sendPlayerMsg(MeteorStarscript.run(script)));
            timer1 = delay1.get();
        } else {
            timer1--;
        }

        if (timer2 <= 0) {
            scripts2.forEach(script -> ChatUtils.sendPlayerMsg(MeteorStarscript.run(script)));
            timer2 = delay2.get();
        } else {
            timer2--;
        }
    }

    private void recompileM1(List<String> scripts) {
        scripts1 = scripts.stream().map(MeteorStarscript::compile).toList();
    }

    private void recompileM2(List<String> scripts) {
        scripts2 = scripts.stream().map(MeteorStarscript::compile).toList();
    }
}
