package me.onlyrain.randomaddon.modules.crystalass.crash;

import io.netty.channel.Channel;
import me.onlyrain.randomaddon.RandomAddon;
import meteordevelopment.meteorclient.mixin.ClientConnectionAccessor;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class BoatCrash extends Module {
    public BoatCrash() {
        super(RandomAddon.CATEGORYC, "boat-crash", "dfghjkrthyrh");
    }

    @Override
    public void onActivate() {
        if (mc.player != null && mc.player.getVehicle() != null && mc.player.getVehicle() instanceof BoatEntity) {
            for (int i = 0; i < 100000; i++) {
                if (getChannel() != null) {
                    Vec3d prevPos = mc.player.getVehicle().getPos();
                    mc.player.getVehicle().setPos(mc.player.getVehicle().getX() - 3, mc.player.getVehicle().getY() - 3, mc.player.getVehicle().getZ() - 3);
                    getChannel().writeAndFlush(new VehicleMoveC2SPacket(mc.player.getVehicle()));
                    mc.player.getVehicle().setPos(prevPos.getX(), prevPos.getY(), prevPos.getZ());
                    getChannel().writeAndFlush(new VehicleMoveC2SPacket(mc.player.getVehicle()));
                }
            }
            toggle();
        }
    }

    private Channel getChannel() {
        return ((ClientConnectionAccessor) mc.getNetworkHandler().getConnection()).getChannel();
    }
}
