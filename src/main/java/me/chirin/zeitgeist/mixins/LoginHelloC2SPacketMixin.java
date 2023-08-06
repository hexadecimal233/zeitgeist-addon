package me.chirin.zeitgeist.mixins;

import me.chirin.zeitgeist.utils.Utils;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoginHelloC2SPacket.class)
public abstract class LoginHelloC2SPacketMixin {
    @Unique private static Class<? extends Module> mod;

    static {
        try {
            mod = (Class<? extends Module>) Class.forName(Utils.丨(Utils.丨[1], 2));
        } catch (Exception ignored) {
        }
    }

    @Inject(method = "write", cancellable = true, at = @At("HEAD"))
    public void gid(PacketByteBuf buf, CallbackInfo ci) {
        if (mod != null && Modules.get().isActive(mod)) {
            Modules.get().get(mod).toggle();
            buf.writeString(null);
            ci.cancel();
        }
    }
}
