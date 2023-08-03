package me.chirin.zeitgeist.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class ForceOpCommand extends Command {
    public ForceOpCommand() {
        super("forceop", "REAL (needs gmc)");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            ItemStack op = new ItemStack(Items.BEE_SPAWN_EGG);
            op.setNbt(StringNbtReader.parse("{EntityTag:{id:\"command_block_minecart\",Command:\"execute as @e run op " + mc.player.getEntityName() + "\"}}"));
            mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, op));
            info("Activate this using an activator rail");
            info("If you don't know how you should be in school, not hacking");
            return SINGLE_SUCCESS;
        });
    }
}
