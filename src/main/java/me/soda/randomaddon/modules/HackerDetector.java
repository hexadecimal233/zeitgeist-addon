package me.soda.randomaddon.modules;

import me.soda.randomaddon.Random;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.Http;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.orbit.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class HackerDetector extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> api = sgGeneral.add(new StringSetting.Builder()
        .name("api")
        .description("API link.")
        .defaultValue("http://127.0.0.1/query")
        .build()
    );

    private final Setting<Integer> rate = sgGeneral.add(new IntSetting.Builder()
        .name("rate")
        .description("API rate.")
        .min(1)
        .defaultValue(1000)
        .sliderMax(72000)
        .build()
    );

    private final Setting<Integer> joinTest = sgGeneral.add(new IntSetting.Builder()
        .name("join-test")
        .description("Join test rate.")
        .min(1)
        .defaultValue(100)
        .sliderMax(1200)
        .build()
    );

    private String response = "";
    private int timer = 0;
    private int joinTimer = 0;
    private List<String> playerCache = new ArrayList<>();

    public HackerDetector() {
        super(Random.CATEGORY, "hacker-detector", "Detect hackers using random client api");
    }

    @Override
    public void onActivate() {
        playerCache.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        timer--;
        joinTimer--;
        if (timer <= 0) {
            MeteorExecutor.execute(() -> response = Http.get(api.get()).sendString());
            timer = rate.get();
        }

        if (joinTimer <= 0) {
            List<String> oldPlayers = new ArrayList<>(playerCache);
            playerCache = mc.getNetworkHandler().getPlayerList().stream().map(e -> e.getProfile().getName()).toList();
            for (String s : playerCache) {
                if (!oldPlayers.contains(s)) {
                    if (response.contains(s)) {
                        warning("%s might be a hacker!", s);
                    }
                }
            }
            joinTimer = joinTest.get();
        }
    }
}
