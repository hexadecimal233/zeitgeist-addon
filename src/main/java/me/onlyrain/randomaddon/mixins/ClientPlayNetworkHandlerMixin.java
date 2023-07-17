package me.onlyrain.randomaddon.mixins;

import me.onlyrain.randomaddon.modules.NoRandomPackets;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameStateChange", at = @At("HEAD"), cancellable = true)
    private void onGameStateChange(GameStateChangeS2CPacket packet, CallbackInfo info) {
        NoRandomPackets noRandomPackets = Modules.get().get(NoRandomPackets.class);
        if (noRandomPackets.isActive()) {
            GameStateChangeS2CPacket.Reason reason = packet.getReason();
            if (noRandomPackets.demo.get() && reason.equals(GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN) ||  // Demo popup
                noRandomPackets.fakeCreative.get() && reason == GameStateChangeS2CPacket.GAME_MODE_CHANGED && GameMode.byId(MathHelper.floor(packet.getValue() + 0.5F)) == GameMode.CREATIVE) {  // Creative mode
                // Completely ignore packets
                info.cancel();
            } else if (noRandomPackets.end.get() && reason.equals(GameStateChangeS2CPacket.GAME_WON)) {  // End credits (still send respawn packet)
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
                info.cancel();
            }
        }
    }
}
