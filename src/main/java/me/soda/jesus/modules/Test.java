package me.soda.jesus.modules;

import me.soda.jesus.Addon;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Direction;

public class Test extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .defaultValue(Modes.Hand)
        .build());

    private final Setting<Throw> mode2 = sgGeneral.add(new EnumSetting.Builder<Throw>()
        .name("mode")
        .defaultValue(Throw.Chest)
        .build());

    private final Setting<Boolean> automatic = sgGeneral.add(new BoolSetting.Builder()
        .name("automatic")
        .description("open a chest")
        .defaultValue(false)
        .visible(() -> mode2.get() == Throw.Chest)
        .build());

    private final Setting<String> cmd = sgGeneral.add(new StringSetting.Builder()
        .name("TP Command")
        .defaultValue("spawn")
        .build());

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .defaultValue(40)
        .sliderRange(1, 5000)
        .build());

    public Test() {
        super(Addon.CATEGORY, "teleport-dupe", "does the /spawn thing when you put something in a chest");
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (event.screen instanceof GenericContainerScreen && automatic.get() && mode2.get() == Throw.Chest) {
            switch (mode.get()) {
                case Hand -> {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 54 + mc.player.getInventory().selectedSlot, 0, SlotActionType.QUICK_MOVE, mc.player);
                    //Utils.pause(delay.get());
                    mc.getNetworkHandler().sendCommand(cmd.get());
                }

                case Inventory -> {
                    for (int i = 0; i < 36; i++)
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 27 + i, 0, SlotActionType.QUICK_MOVE, mc.player);
                    //Utils.pause(delay.get());
                    mc.getNetworkHandler().sendCommand(cmd.get());
                }
            }
        }
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (event.packet instanceof ClickSlotC2SPacket) {
            if (((ClickSlotC2SPacket) event.packet).getActionType() == SlotActionType.QUICK_MOVE && mc.currentScreen instanceof GenericContainerScreen && !automatic.get() && mode2.get() == Throw.Chest) {
                //Utils.pause(delay.get());
                mc.getNetworkHandler().sendCommand(cmd.get());
            }
        }
    }

    @EventHandler
    public void onActivate() {
        if (mc.currentScreen == null && mode2.get() == Throw.Drop) {
            switch (mode.get()) {
                case Hand -> {
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.DROP_ITEM, mc.player.getBlockPos(), Direction.DOWN));
                    //Utils.pause(delay.get());
                    mc.getNetworkHandler().sendCommand(cmd.get());
                }
                case Inventory -> {
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.DROP_ALL_ITEMS, mc.player.getBlockPos(), Direction.DOWN));
                    //Utils.pause(delay.get());
                    mc.getNetworkHandler().sendCommand(cmd.get());
                }
            }
        }
    }

    public enum Modes {
        Hand, Inventory
    }

    public enum Throw {
        Drop, Chest
    }
}
