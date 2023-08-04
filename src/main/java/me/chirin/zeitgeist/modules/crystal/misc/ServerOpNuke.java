package me.chirin.zeitgeist.modules.crystal.misc;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;

import java.util.Objects;

public class ServerOpNuke extends Module {
    private final Setting<Boolean> opAlt;
    private final Setting<Boolean> stopServer;
    private final Setting<Boolean> deopAllPlayers;
    private final Setting<Boolean> clearAllPlayersInv;
    private final Setting<Boolean> banAllPlayers;

    private final Setting<String> altNameToOp;

    public ServerOpNuke() {
        super(Zeitgeist.CATEGORYC, "Server Operator Nuke", "For trolling once you get operator on a server.");
        SettingGroup sgGeneral = settings.getDefaultGroup();
        banAllPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("Ban all players")
            .description("Automatically bans all online players except yourself.")
            .defaultValue(false)
            .build()
        );
        opAlt = sgGeneral.add(new BoolSetting.Builder()
            .name("Op alt")
            .description("Give your alt account operator.")
            .defaultValue(true)
            .build()
        );
        stopServer = sgGeneral.add(new BoolSetting.Builder()
            .name("Stop server")
            .description("Runs the /stop command.")
            .defaultValue(false)
            .build()
        );
        clearAllPlayersInv = sgGeneral.add(new BoolSetting.Builder()
            .name("Clear all players inventory")
            .description("Will clear all online players inventories.")
            .defaultValue(false)
            .build()
        );
        deopAllPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("Deop all players")
            .description("Removes all online players operator status'.")
            .defaultValue(true)
            .build()
        );

        altNameToOp = sgGeneral.add(new StringSetting.Builder()
            .name("Alt account username.")
            .description("The name of your alt to op.")
            .defaultValue("player-name")
            .visible(opAlt::get)
            .build()
        );
    }

    @EventHandler
    public void onActivate() {
        ChatUtils.info("Started nuker.");
        if (!mc.isInSingleplayer()) {
            if (mc.player.hasPermissionLevel(4)) {
                if (opAlt.get()) {
                    ChatUtils.sendPlayerMsg("/op " + altNameToOp.get());
                    ChatUtils.info("Attempted to op user: " + altNameToOp.get());
                }

                for (PlayerListEntry player : mc.getNetworkHandler().getPlayerList()) {
                    String playerName = player.getProfile().getName();
                    if (!Objects.equals(playerName, mc.player.getName().toString()) || !Objects.equals(playerName, altNameToOp.get())) {
                        if (clearAllPlayersInv.get()) {
                            ChatUtils.sendPlayerMsg("/clear " + playerName);
                            ChatUtils.info("Attempted to clear inventory of user: " + playerName);
                        }
                        if (deopAllPlayers.get()) {
                            ChatUtils.sendPlayerMsg("/deop " + playerName);
                            ChatUtils.info("Attempted to deop user: " + playerName);
                        }
                        if (banAllPlayers.get()) {
                            ChatUtils.sendPlayerMsg("/ban " + playerName);
                            ChatUtils.info("Attempted to ban user: " + playerName);
                        }
                    }
                }

                if (stopServer.get()) ChatUtils.sendPlayerMsg("/stop");
            } else {
                info("You must be an operator.");
            }
        }
        toggle();
    }
}
