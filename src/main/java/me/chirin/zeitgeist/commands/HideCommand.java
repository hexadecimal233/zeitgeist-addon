package me.chirin.zeitgeist.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class HideCommand extends Command {
    public HideCommand() {
        super("hide", "adds the HideFlags nbt to your item");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("flags", IntegerArgumentType.integer()).executes(ctx -> {
            ItemStack ww = mc.player.getMainHandStack();
            int aaa = ctx.getArgument("flags", Integer.class);
            NbtCompound tag = ww.getOrCreateNbt();
            tag.putInt("HideFlags", aaa);
            return SINGLE_SUCCESS;
        }));
    }
}
