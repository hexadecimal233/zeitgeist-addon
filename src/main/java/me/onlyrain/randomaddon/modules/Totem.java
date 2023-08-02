package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.RandomAddon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class Totem extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> closeScreen = sgGeneral.add(new BoolSetting.Builder()
        .name("close-screen-on-pop")
        .description("Closes any screen handler while putting totem in offhand.")
        .defaultValue(false)
        .build());

    private final Setting<Integer> actiondelay = sgGeneral.add(new IntSetting.Builder()
        .name("action-delay")
        .defaultValue(0)
        .build());

    private final Setting<Boolean> totemspam = sgGeneral.add(new BoolSetting.Builder()
        .name("totem-spam")
        .defaultValue(false)
        .build());

    private final Setting<Integer> spamdelay = sgGeneral.add(new IntSetting.Builder()
        .name("spam-delay")
        .visible(totemspam::get)
        .defaultValue(0)
        .build());
    private boolean should_override_totem;
    private int selected_slot = 0;
    private int delay_ticks_left = 0, try_hard_ticks_left = 0;

    public Totem() {
        super(RandomAddon.CATEGORY, "instant-totem", "Best strict auto totem");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (delay_ticks_left > 0) {
            --delay_ticks_left;
            return;
        }

        if (mc.player.currentScreenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler) return;
        ItemStack offhand_stack = mc.player.getInventory().getStack(40),
            cursor_stack = mc.player.currentScreenHandler.getCursorStack();

        final boolean
            is_holding_totem = cursor_stack.getItem() == Items.TOTEM_OF_UNDYING,
            is_totem_in_offhand = offhand_stack.getItem() == Items.TOTEM_OF_UNDYING,
            can_click_offhand = mc.player.currentScreenHandler instanceof PlayerScreenHandler;

        if (totemspam.get()) {
            if (try_hard_ticks_left > 0)
                --try_hard_ticks_left;
            else {
                try_hard_ticks_left = spamdelay.get();
                should_override_totem = true;
            }
        }

        if (is_totem_in_offhand && !should_override_totem) return;
        final int totem_id = GetTotemId();
        if (totem_id == -1 && !is_holding_totem) return;
        if (is_holding_totem && can_click_offhand) {
            Click(45);
            return;
        }
        if (!can_click_offhand) {
            if (totem_id == -1) {
                for (Slot slot : mc.player.currentScreenHandler.slots) {
                    if (!slot.getStack().isEmpty()) continue;
                    Click(slot.id);
                    return;
                }
                Click(GetFirstHotbarSlotId() + selected_slot);
                return;
            }
        }
        Move(totem_id);
        should_override_totem = !is_totem_in_offhand;
    }

    @EventHandler
    private void onPacketSent(PacketEvent.Sent event) {
        if (event.packet instanceof ClickSlotC2SPacket) delay_ticks_left = actiondelay.get();
        else if (event.packet instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() == PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND)
                delay_ticks_left = actiondelay.get();
        } else if (event.packet instanceof UpdateSelectedSlotC2SPacket packet) {
            selected_slot = packet.getSelectedSlot();
        }
    }

    @EventHandler
    private void onPacketReceived(PacketEvent.Receive event) {
        if (event.packet instanceof EntityStatusS2CPacket packet) {
            if (mc.player.currentScreenHandler instanceof PlayerScreenHandler) return;
            if (closeScreen.get()) mc.player.closeHandledScreen();

            ItemStack mainhand_stack = mc.player.getInventory().getStack(selected_slot);
            if (mainhand_stack.getItem() == Items.TOTEM_OF_UNDYING) {
                mainhand_stack.decrement(1);
                return;
            }
            ItemStack offhand_stack = mc.player.getOffHandStack();
            if (offhand_stack.getItem() == Items.TOTEM_OF_UNDYING) offhand_stack.decrement(1);
        } else if (event.packet instanceof UpdateSelectedSlotS2CPacket packet) {
            selected_slot = packet.getSlot();
        }
    }

    @Override
    public void onActivate() {
        should_override_totem = true;
        selected_slot = mc.player.getInventory().selectedSlot;
    }

    private int GetTotemId() {
        final int hotbar_start = GetFirstHotbarSlotId();
        for (int i = hotbar_start; i < hotbar_start + 9; ++i) {
            if (mc.player.currentScreenHandler.getSlot(i).getStack().getItem() != Items.TOTEM_OF_UNDYING) continue;
            return i;
        }

        for (int i = 0; i < hotbar_start; ++i) {
            if (mc.player.currentScreenHandler.getSlot(i).getStack().getItem() != Items.TOTEM_OF_UNDYING) continue;
            return i;
        }
        return -1;
    }

    private void ClickSlot(int id, int button, SlotActionType action) {
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, id, button, action, mc.player);
    }

    public void Click(int id) {
        ClickSlot(id, 0, SlotActionType.PICKUP);
    }

    public void Swap(int id, int button) {
        ClickSlot(id, button, SlotActionType.SWAP);
    }

    public int GetFirstHotbarSlotId() {
        if (mc.player.currentScreenHandler instanceof PlayerScreenHandler) return 36;
        return mc.player.currentScreenHandler.slots.size() - 9;
    }

    public void Move(int id) {
        Swap(id, 40);
    }
}
