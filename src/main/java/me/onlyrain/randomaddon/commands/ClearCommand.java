package me.onlyrain.randomaddon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.screen.slot.SlotActionType;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clearinv", "clears your inv");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            for (int i = 5; i < 46; i++) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 120, SlotActionType.SWAP, mc.player);
            }
            return SINGLE_SUCCESS;
        });
    }
}
