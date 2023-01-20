package me.soda.jesus.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class VoidCommand extends Command {
    public VoidCommand() {
        super("void", "Delete the item stack in your hand.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            int id = 36 + mc.player.getInventory().selectedSlot;
            int button = 50;

            ScreenHandler screenHandler = mc.player.currentScreenHandler;
            DefaultedList<Slot> slots = screenHandler.slots;

            List<ItemStack> items = slots.stream().map(slot -> slot.getStack().copy()).toList();

            Int2ObjectMap<ItemStack> stacks = new Int2ObjectOpenHashMap<>();

            for (int i = 0; i < slots.size(); i++) {
                ItemStack s1 = items.get(i);
                ItemStack s2 = slots.get(i).getStack();

                if (!ItemStack.areEqual(s1, s2))
                    stacks.put(i, s2.copy());
            }

            mc.getNetworkHandler().sendPacket(
                new ClickSlotC2SPacket(0, screenHandler.getRevision(), id, button, SlotActionType.SWAP, screenHandler.getCursorStack().copy(), stacks)
            );
            return SINGLE_SUCCESS;
        });
    }
}
