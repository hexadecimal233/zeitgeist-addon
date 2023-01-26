package me.soda.randomaddon.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class BloatCommand extends Command {
    public BloatCommand() {
        super("bloat", "inflates data, also meteor byte counter is wrong", "inflate");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("bytes", IntegerArgumentType.integer(1)).executes(context -> {
            ItemStack item = mc.player.getMainHandStack();
            int bytes = context.getArgument("bytes", Integer.class);
            item.setNbt(StringNbtReader.parse("{a:[{}" + ",{}".repeat((bytes - (bytes % 3)) / 3) + "]}"));
            return SINGLE_SUCCESS;
        }));
    }
}
