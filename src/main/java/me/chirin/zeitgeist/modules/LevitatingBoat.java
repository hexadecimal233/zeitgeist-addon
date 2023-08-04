package me.chirin.zeitgeist.modules;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

import java.util.stream.IntStream;

public class LevitatingBoat extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();;
    private final Setting<Double> updatePerTick = sgGeneral.add(new DoubleSetting.Builder()
        .name("update-per-tick")
        .defaultValue(4)
        .min(0)
        .sliderMax(10)
        .build()
    );

    private final Setting<Double> blockPerUpdate = sgGeneral.add(new DoubleSetting.Builder()
        .name("block-per-update")
        .defaultValue(10)
        .min(0)
        .sliderMax(20)
        .build()
    );

    public LevitatingBoat() {
        super(Zeitgeist.CATEGORY, "levitating-boat", "Levitate!.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!(this.mc.player.getVehicle() instanceof BoatEntity)) {
            this.error("You must be in a boat to use this.");
            this.toggle();
        } else {
            IntStream.iterate(0, i -> i < this.updatePerTick.get(), i -> i + 1).forEach(i -> {
                this.mc.player.getVehicle().updatePosition(mc.player.getVehicle().getX(), this.mc.player.getVehicle().getY() + this.blockPerUpdate.get(), this.mc.player.getVehicle().getZ());
                this.mc.player.networkHandler.sendPacket(new VehicleMoveC2SPacket(mc.player.getVehicle()));
                this.mc.player.getVehicle().setVelocity(0.0, 0.0, 0.0);
            });
        }
    }
}
