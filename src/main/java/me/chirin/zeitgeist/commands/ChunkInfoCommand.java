package me.chirin.zeitgeist.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.netty.buffer.Unpooled;
import me.chirin.zeitgeist.utils.ChunkC2CPacket;
import me.chirin.zeitgeist.utils.ConversionHelper;
import meteordevelopment.meteorclient.commands.Command;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.ChunkPos;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class ChunkInfoCommand extends Command {
    public ChunkInfoCommand() {
        super("chunkInfo", "");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.then(literal("serialize")
            .then(literal("this").executes(ctx -> serialize(mc.player.getChunkPos())))
            .then(argument("x", IntegerArgumentType.integer()).then(argument("z", IntegerArgumentType.integer()).executes(ctx -> serialize(new ChunkPos(IntegerArgumentType.getInteger(ctx, "x"), IntegerArgumentType.getInteger(ctx, "z"))))))
        );
    }

    private int serialize(ChunkPos chunkPos) {
        info("Showing serialization info for chunk (%s, %s)", chunkPos.x, chunkPos.z);
        ChunkC2CPacket p = ChunkC2CPacket.of(chunkPos);
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        p.write(buf);
        byte[] uncompressedArray = buf.getWrittenBytes();
        info("Uncompressed size: %s bytes.", uncompressedArray.length);
        byte[] compressedArray = ConversionHelper.Gzip.compress(uncompressedArray);
        info("GZIP Compressed size: %s bytes.", compressedArray.length);

        p.debugOutput();

        return SINGLE_SUCCESS;
    }
}
