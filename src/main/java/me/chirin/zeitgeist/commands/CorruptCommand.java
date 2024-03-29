package me.chirin.zeitgeist.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class CorruptCommand extends Command {
    public CorruptCommand() {
        super("corrupt", "terraria reference???");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("charge", IntegerArgumentType.integer(1)).executes(context -> {
            Vec3d sex = new Vec3d(Math.round(mc.player.getX()), Math.round(mc.player.getY()) - 1, Math.round(mc.player.getZ()));
            Vec3d balls = new Vec3d(Math.round(mc.player.getX()), Math.round(mc.player.getY()) - 1, Math.round(mc.player.getZ()) - 1);
            ItemStack bfr = mc.player.getMainHandStack();
            ItemStack cat = new ItemStack(Items.SCULK_CATALYST);
            String charge = "{charge:1000,decay_delay:1,facings:[\"east\"],pos:[I;" + (int) sex.x + "," + (int) sex.y + "," + (int) sex.z + "],update_delay:1}";
            cat.setNbt(StringNbtReader.parse("{BlockEntityTag:{bloom:1,cursors:[" + charge + ("," + charge).repeat(context.getArgument("charge", Integer.class) - 1) + "],id:\"minecraft:sculk_catalyst\"}}"));

            BlockHitResult bhr = new BlockHitResult(sex, Direction.DOWN, BlockPos.ofFloored(sex), false);
            BlockHitResult a = new BlockHitResult(balls, Direction.DOWN, BlockPos.ofFloored(balls), false);
            mc.interactionManager.clickCreativeStack(new ItemStack(Items.SCULK), 36 + mc.player.getInventory().selectedSlot);
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, BlockPos.ofFloored(balls), Direction.DOWN, 0));
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, a);
            mc.interactionManager.clickCreativeStack(cat, 36 + mc.player.getInventory().selectedSlot);
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, BlockPos.ofFloored(sex), Direction.DOWN, 0));
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
            mc.interactionManager.clickCreativeStack(bfr, 36 + mc.player.getInventory().selectedSlot);
            return SINGLE_SUCCESS;
        }));
    }
}
