package me.chirin.zeitgeist.modules;

import me.chirin.zeitgeist.Zeitgeist;
import me.chirin.zeitgeist.events.GameStateChangeEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;

public class NoRandomPackets extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> demo = sgGeneral.add(new BoolSetting.Builder()
        .name("less-annoying-bats")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> fakeCreative = sgGeneral.add(new BoolSetting.Builder()
        .name("less-annoying-bats")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> end = sgGeneral.add(new BoolSetting.Builder()
        .name("less-annoying-bats")
        .defaultValue(false)
        .build());

    public NoRandomPackets() {
        super(Zeitgeist.CATEGORY, "no-random-packets", "No random packets.");
    }

    @EventHandler
    private void onGameStateChange(GameStateChangeEvent event) {
        GameStateChangeS2CPacket.Reason reason = event.packet.getReason();
        int value = MathHelper.floor(event.packet.getValue() + 0.5F);
        if (demo.get() && reason.equals(GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN) ||  // Demo popup
            fakeCreative.get() && reason == GameStateChangeS2CPacket.GAME_MODE_CHANGED && GameMode.byId(value) == GameMode.CREATIVE) {  // Creative mode
            // Completely ignore packets
            event.cancel();
        } else if (end.get() && reason.equals(GameStateChangeS2CPacket.GAME_WON)) {  // End credits (still send respawn packet)
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
            event.cancel();
        }
    }
}
