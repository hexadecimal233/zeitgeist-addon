package me.chirin.zeitgeist.modules.crystalass.misc;

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

public class BoatFling extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Double> speed;
    private final Setting<Double> updateAmount;

    public BoatFling() {
        super(Zeitgeist.CATEGORYC, "Boat Fling", "Allows you to fling using boats.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.speed = this.sgGeneral.add((new DoubleSetting.Builder()).name("speed").description("How fast to fling you.").defaultValue(4.0).min(0.0).sliderMax(10.0).build());
        this.updateAmount = this.sgGeneral.add((new DoubleSetting.Builder()).name("update-amount").description("How much to update your position per tick.").defaultValue(10.0).min(0.0).sliderMax(20.0).build());
    }

    public void onActivate() {
        if (this.mc.player != null && !(this.mc.player.getVehicle() instanceof BoatEntity)) {
            this.error("You must be in a boat to use this.");
            this.toggle();
        }

    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        assert this.mc.player != null;
        if (!(this.mc.player.getVehicle() instanceof BoatEntity)) {
            this.error("You must be in a boat to use this.");
            this.toggle();
        } else {
            IntStream.iterate(0, i -> {
                if (!((double) i < (Double) this.speed.get())) {
                    return false;
                } else {
                    return true;
                }
            }, i -> {
                return i + 1;
            }).forEach((int i) -> {
                this.mc.player.getVehicle().updatePosition(this.mc.player.getVehicle().getX(), this.mc.player.getVehicle().getY() + (Double) this.updateAmount.get(), this.mc.player.getVehicle().getZ());
                this.mc.player.networkHandler.sendPacket(new VehicleMoveC2SPacket(this.mc.player.getVehicle()));
                this.mc.player.getVehicle().setVelocity(0.0, 0.0, 0.0);
            });

        }
    }
}
