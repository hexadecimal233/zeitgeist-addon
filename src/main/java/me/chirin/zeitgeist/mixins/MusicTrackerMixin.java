package me.chirin.zeitgeist.mixins;

import me.chirin.zeitgeist.modules.tokyo.Jukebox;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MusicTracker.class)
public abstract class MusicTrackerMixin {
    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void jukeboxBlacklist(MusicSound type, CallbackInfo ci) {
        if (Modules.get().get(Jukebox.class).shouldCancelMusic(type.getSound().value())) ci.cancel();
    }
}
