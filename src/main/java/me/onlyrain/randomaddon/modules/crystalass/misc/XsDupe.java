package me.onlyrain.randomaddon.modules.crystalass.misc;

import me.onlyrain.randomaddon.RandomAddon;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.text.Text;

import java.util.Objects;

public class XsDupe extends Module {

    private final Setting<Boolean> autoDisable;

    public XsDupe() {
        super(RandomAddon.CATEGORYC, "XsDupe", "CRYSTAL || Attempts to dupe by desyncing and doing multiple actions.");
        SettingGroup sgGeneral = settings.getDefaultGroup();

        autoDisable = sgGeneral.add(new BoolSetting.Builder()
            .name("Auto Disable")
            .description("Disables module on kick.")
            .defaultValue(true)
            .build()
        );
    }

    @EventHandler
    public void onActivate() {
        try {
            Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new TeleportConfirmC2SPacket(0));
            info("Successfully desynced your player entity");

            if (mc.player != null) {
                mc.player.getInventory().dropSelectedItem(true);
            }

            mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal("Disconnected via .disconnect command.")));
        } catch (Exception e) {
            MeteorClient.LOG.error("An error occured during XsDupe onActivate, " + e);
        }

    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (!autoDisable.get()) {
            return;
        }
        toggle();
    }
}
