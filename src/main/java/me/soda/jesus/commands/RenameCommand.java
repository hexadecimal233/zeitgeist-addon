package me.soda.jesus.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.RenameItemC2SPacket;
import net.minecraft.text.Text;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class RenameCommand extends Command {
    public RenameCommand() {
        super("rename", "Renames the item in your hand.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("name", StringArgumentType.greedyString()).executes(context -> {
            ItemStack stack = mc.player.getMainHandStack();
            String newName = StringArgumentType.getString(context, "name");

            stack.setCustomName(Text.of(newName));
            mc.getNetworkHandler().sendPacket(new RenameItemC2SPacket(newName));
            return SINGLE_SUCCESS;
        }));
    }
}
