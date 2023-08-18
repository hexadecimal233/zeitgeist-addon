package me.chirin.zeitgeist.mixins;

import me.chirin.zeitgeist.modules.SkinProtection;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
    @Inject(method = "getSkinTexture", at = @At("RETURN"), cancellable = true)
    private void modifySkinTexture(CallbackInfoReturnable<Identifier> info) {
        if(SkinProtection.INSTANCE.isActive()) info.setReturnValue(DefaultSkinHelper.getTexture());
    }
}
