package me.onlyrain.randomaddon.mixins;

import me.onlyrain.randomaddon.events.GameStateChangeEvent;
import me.onlyrain.randomaddon.events.ScoreboardEvent;
import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameStateChange", at = @At("HEAD"), cancellable = true)
    private void onGameStateChange(GameStateChangeS2CPacket packet, CallbackInfo info) {
        if (MeteorClient.EVENT_BUS.post(GameStateChangeEvent.get(packet)).isCancelled()) info.cancel();
    }

    @Inject(method = "onScoreboardObjectiveUpdate", at = @At(value = "HEAD"))
    private void injectedUpdate(ScoreboardObjectiveUpdateS2CPacket packet, CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post(ScoreboardEvent.get());
    }

    @Inject(method = "onScoreboardDisplay", at = @At(value = "HEAD"))
    private void injectedDisplay(ScoreboardDisplayS2CPacket packet, CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post(ScoreboardEvent.get());
    }
}
