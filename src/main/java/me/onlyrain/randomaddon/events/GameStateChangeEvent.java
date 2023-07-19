package me.onlyrain.randomaddon.events;

import meteordevelopment.meteorclient.events.Cancellable;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;

public class GameStateChangeEvent extends Cancellable {
    private static final GameStateChangeEvent INSTANCE = new GameStateChangeEvent();

    public GameStateChangeS2CPacket packet;

    public static GameStateChangeEvent get(GameStateChangeS2CPacket packet) {
        INSTANCE.setCancelled(false);
        INSTANCE.packet = packet;
        return INSTANCE;
    }
}
