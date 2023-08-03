package me.chirin.zeitgeist.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

@Environment(EnvType.CLIENT)
public class ChunkC2CPacket {
    public final int chunkX;
    public final int chunkZ;
    public final SerializableChunkData chunkData;

    public ChunkC2CPacket(PacketByteBuf buf) {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.chunkData = new SerializableChunkData(buf);
    }

    public ChunkC2CPacket(WorldChunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        this.chunkX = chunkPos.x;
        this.chunkZ = chunkPos.z;
        this.chunkData = new SerializableChunkData(chunk);
    }

    public static ChunkC2CPacket of(ChunkPos chunkPos) {
        return new ChunkC2CPacket(MinecraftClient.getInstance().world.getChunk(chunkPos.x, chunkPos.z));
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        chunkData.write(buf);
    }

    public void debugOutput() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf);
        int totalSize = buf.getWrittenBytes().length;
        System.out.printf("Debugging ChunkC2CPacket %s, %s (%s bytes)%n", chunkX, chunkZ, totalSize);

        PacketByteBuf chunkDataBuf = new PacketByteBuf(Unpooled.buffer());
        chunkData.write(chunkDataBuf);
        int chunkDataSize = chunkDataBuf.getWrittenBytes().length;
        System.out.printf("Chunk data (%s bytes): {%n", chunkDataSize);

        System.out.printf("- Section data (%s bytes)%n", chunkData.sectionsData.length);
        System.out.printf("- Heightmap (%s bytes)%n", chunkDataSize - chunkData.sectionsData.length);;
        System.out.printf("}%n");
    }

    public static class SerializableChunkData {
        public final Byte2ObjectMap<long[]> heightmap = new Byte2ObjectOpenHashMap<>();
        public final byte[] sectionsData;

        public SerializableChunkData(WorldChunk chunk) {
            for (var heightmapEntry : chunk.getHeightmaps()) {
                if (!heightmapEntry.getKey().shouldSendToClient()) continue;
                this.heightmap.put((byte) heightmapEntry.getKey().ordinal(), heightmapEntry.getValue().asLongArray());
            }

            this.sectionsData = new byte[getSectionsPacketSize(chunk)];
            writeSectionsData(new PacketByteBuf(this.getWritableSectionsDataBuf()), chunk);
        }

        public SerializableChunkData(PacketByteBuf buf) {
            int heightmapCount = buf.readByte();
            for (int i = 0; i < heightmapCount; i++) {
                byte enumOrdinal = buf.readByte();
                long[] data = buf.readLongArray();
                this.heightmap.put(enumOrdinal, data);
            }

            int sectionsDataLength = buf.readVarInt();
            if (sectionsDataLength > 0x200000) throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
            this.sectionsData = new byte[sectionsDataLength];
            buf.readBytes(this.sectionsData);
        }

        public void write(PacketByteBuf buf) {
            buf.writeByte(this.heightmap.size()); //write heightmap type count
            for (var heightmapEntry : this.heightmap.byte2ObjectEntrySet()) {
                buf.writeByte(heightmapEntry.getByteKey()); //write heightmap enum ordinal
                buf.writeLongArray(heightmapEntry.getValue()); //write heightmap data
            }

            buf.writeVarInt(this.sectionsData.length);
            buf.writeBytes(this.sectionsData);
        }

        public ByteBuf getWritableSectionsDataBuf() {
            ByteBuf byteBuf = Unpooled.wrappedBuffer(this.sectionsData);
            byteBuf.writerIndex(0);
            return byteBuf;
        }

        public PacketByteBuf getSectionsDataBuf() {
            return new PacketByteBuf(Unpooled.wrappedBuffer(this.sectionsData));
        }

        private static int getSectionsPacketSize(WorldChunk chunk) {
            int i = 0;
            for (var section : chunk.getSectionArray()) {
                i += section.getPacketSize();
            }
            return i;
        }

        public static void writeSectionsData(PacketByteBuf buf, WorldChunk chunk) {
            for (var section : chunk.getSectionArray()) {
                section.toPacket(buf);
            }
        }
    }
}
