package me.onlyrain.randomaddon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class DropAllCommand extends Command {
    public DropAllCommand() {
        super("dropall", "Drops all items in your inventory.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            assert mc.player != null;
            mc.player.getInventory().dropAll();
            return SINGLE_SUCCESS;
        });
    }
}
