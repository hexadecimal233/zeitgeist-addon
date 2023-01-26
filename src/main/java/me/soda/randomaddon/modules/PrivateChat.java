package me.soda.randomaddon.modules;

import me.soda.randomaddon.Random;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;

import java.util.Collection;
import java.util.List;

public class PrivateChat extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> players = sgGeneral.add(new StringListSetting.Builder()
        .name("players")
        .description("The players to privately message. Invalid users will be ignored.")
        .defaultValue()
        .build());

    private final Setting<String> format = sgGeneral.add(new StringSetting.Builder()
        .name("command-format")
        .description("The format of the message command on the server.")
        .defaultValue("/msg {player} {message}")
        .build());

    public PrivateChat() {
        super(Random.CATEGORY, "private-chat", "Turns your chat into a private conversation.");
    }

    @EventHandler
    private void onSendMessage(SendMessageEvent event) {
        Collection<PlayerListEntry> playerList = mc.getNetworkHandler().getPlayerList();
        for (PlayerListEntry entry : playerList) {
            String name = entry.getProfile().getName();
            if (players.get().contains(name)) {
                ChatUtils.sendPlayerMsg(format.get()
                    .replace("{player}", name)
                    .replace("{message}", event.message)
                );
                event.cancel();
            }
        }

    }
}
