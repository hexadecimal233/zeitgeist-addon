package me.chirin.zeitgeist.mixins;

import com.mojang.authlib.GameProfile;
import me.chirin.zeitgeist.mixininterfaces.IGameProfile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameProfile.class)
public class GameProfileMixin implements IGameProfile {
    @Mutable @Shadow @Final private String name;

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
