package me.chirin.zeitgeist.modules.crystalass.crash;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.mixin.AbstractSignEditScreenAccessor;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;

import java.util.Objects;

public class AutoLagSign extends Module {
    private final SettingGroup sgGeneral;
    private final Setting mode;
    private final Setting<Boolean> colorChar;
    private final Setting<ColorMode> colorMode;
    private final Setting<Integer> amount;
    private final Setting<String> customChar;

    public AutoLagSign() {
        super(Zeitgeist.CATEGORYC, "auto-lag-sign", "Automatically writes lag signs.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.mode = this.sgGeneral.add(((EnumSetting.Builder) ((EnumSetting.Builder) ((EnumSetting.Builder) (new EnumSetting.Builder()).name("mode")).description("The mode for Auto Lag Sign.")).defaultValue(Mode.Random)).build());
        this.colorChar = this.sgGeneral.add(((BoolSetting.Builder) ((BoolSetting.Builder) ((BoolSetting.Builder) (new BoolSetting.Builder()).name("color-char")).description("Attempts to obfuscate the text.")).defaultValue(false)).build());
        EnumSetting.Builder var10002 = (EnumSetting.Builder) ((EnumSetting.Builder) ((EnumSetting.Builder) (new EnumSetting.Builder()).name("color-mode")).description("The mode for Color Char.")).defaultValue(ColorMode.Vanilla);
        Setting<Boolean> var10003 = this.colorChar;
        Objects.requireNonNull(var10003);
        this.colorMode = this.sgGeneral.add(((EnumSetting.Builder) var10002.visible(var10003::get)).build());
        this.amount = this.sgGeneral.add(((IntSetting.Builder) ((IntSetting.Builder) ((IntSetting.Builder) (new IntSetting.Builder()).name("char-amount")).description("How many characters to put on the sign.")).defaultValue(80)).min(1).sliderMin(1).sliderMax(80).build());
        this.customChar = this.sgGeneral.add(((StringSetting.Builder) ((StringSetting.Builder) ((StringSetting.Builder) ((StringSetting.Builder) (new StringSetting.Builder()).name("custom-char")).description("The char to use for Auto Lag Sign.")).defaultValue("")).visible(() -> {
            return this.mode.get() == Mode.Custom;
        })).build());
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (!mc.isInSingleplayer()) {
            if (event.screen instanceof SignEditScreen) {
                SignBlockEntity sign = ((AbstractSignEditScreenAccessor) event.screen).getSign();
                String signText;
                if (this.mode.get() != Mode.Custom) {
                    signText = this.mode.get() == Mode.Random ? ((Boolean) this.colorChar.get() ? (this.colorMode.get() == ColorMode.Vanilla ? "§§kk" : "&k") + this.generateMessage() : this.generateMessage()) : ((Boolean) this.colorChar.get() ? (this.colorMode.get() == ColorMode.Vanilla ? "§§kk" : "&k") + "\uffff".repeat((Integer) this.amount.get() - (this.colorMode.get() == ColorMode.Vanilla ? 4 : 2)) : "\uffff".repeat((Integer) this.amount.get()));
                } else {
                    signText = ((Boolean) this.colorChar.get() ? (this.colorMode.get() == ColorMode.Vanilla ? "§§kk" : "&k") : "") + ((String) this.customChar.get()).substring(0, 1).repeat((Integer) this.amount.get() - ((Boolean) this.colorChar.get() ? (this.colorMode.get() == ColorMode.Vanilla ? 4 : 2) : 0));
                }

                if (this.mc.player != null) {
                    this.mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign.getPos(), true, signText, signText, signText, signText));
                }
                event.setCancelled(true);
            }
        } else {
            error("You must be on a server, toggling.");
            toggle();
        }
    }

    private String generateMessage() {
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < ((Boolean) this.colorChar.get() ? (Integer) this.amount.get() - (this.colorMode.get() == ColorMode.Vanilla ? 4 : 2) : (Integer) this.amount.get()); ++i) {
            message.append((char) (19968 + (int) (Math.random() * 20902.0)));
        }

        return message.toString();
    }

    public static enum Mode {
        Random,
        FFFF,
        Custom;

        private Mode() {
        }
    }

    public static enum ColorMode {
        Vanilla,
        Plugins;

        private ColorMode() {
        }
    }
}
