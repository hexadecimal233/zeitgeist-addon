package me.onlyrain.randomaddon.mixins;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.onlyrain.randomaddon.modules.ScoreboardPlus;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow @Final private MinecraftClient client;

    @Unique private ScoreboardPlus module;
    @Unique private int xShift;

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;collect(Ljava/util/stream/Collector;)Ljava/lang/Object;"))
    private Object injected2(Stream<ScoreboardPlayerScore> stream, Collector<?, ?, ?> collector) {
        List<ScoreboardPlayerScore> list = stream.toList();
        if (module.isActive() && module.pages.get()) {
            int max = module.maxLineCount.get();
            int listStart = list.size() - max - module.listOff;
            if (listStart < 0) {
                module.listOff = 0;
                listStart = 0;
            }
            int listEnd = listStart + max;
            if (listEnd > list.size()) {
                module.listOff = listEnd;
                listEnd = list.size();
            }
            return list.subList(listStart, listEnd);
        }
        return list;
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void onRenderHead(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        if (module == null) module = Modules.get().get(ScoreboardPlus.class);
        if (module.isActive() && module.hide.get() || module.hideWhenDebugMenu.get() && client.options.debugEnabled)
            ci.cancel();
    }

    @WrapWithCondition(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"))
    private boolean onDrawScore(DrawContext context, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow) {
        return !(module.isActive() && module.hideScores.get());
    }

    // Tweakermore compat

    @ModifyArg(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 0), index = 4)
    private int modBGColor(int orig) {
        return module.isActive() ? module.titleBGColor.get().getPacked() : orig;
    }

    @ModifyArg(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 2), index = 4)
    private int modBGColor2(int orig) {
        return module.isActive() ? module.titleBGColor.get().getPacked() : orig;
    }

    @ModifyArg(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 1), index = 4)
    private int modTitleBGColor(int orig) {
        return module.isActive() ? module.BGColor.get().getPacked() : orig;
    }

    @ModifyArg(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I", ordinal = 0))
    private String onGetScoreWidth(String orig) {
        return module.isActive() && module.hideScores.get() ? "" : orig;
    }

    @ModifyArg(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I", ordinal = 1))
    private String onGetScoreWidth2(String orig) {
        return module.isActive() && module.hideScores.get() ? "" : orig;
    }

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", ordinal = 1))
    private int drawTitleText(DrawContext context, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow) {
        if (module.isActive()) {
            color = module.titleTextColor.get().getPacked();
            if (module.overrideTitleTextColor.get()) text = Text.of(text.getString());
        }
        return context.drawText(textRenderer, text, x, y, color, shadow);
    }

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", ordinal = 0))
    private int drawText(DrawContext context, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow) {
        if (module.isActive()) {
            color = module.textColor.get().getPacked();
            if (module.overrideTextColor.get()) text = Text.of(text.getString());
        }
        return context.drawText(textRenderer, text, x, y, color, shadow);
    }

    @Redirect(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I", ordinal = 0))
    private int drawScoreText(DrawContext context, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow) {
        if (module.isActive()) {
            text = Formatting.strip(text);
            color = module.scoreColor.get().getPacked();
        }
        return context.drawText(textRenderer, text, x, y, color, shadow);
    }

    @ModifyConstant(method = "renderScoreboardSidebar", constant = @Constant(intValue = 15))
    private int modifyMaxLineCount(int maxLineCount) {
        return module.isActive() ? module.maxLineCount.get() : maxLineCount;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE"), ordinal = 5)
    private int modifyX1(int x1) {
        if (!module.isActive() || !module.left.get()) {
            xShift = 0;
            return x1;
        } else {
            xShift = x1 - 1;
            return 1;
        }
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE"), ordinal = 11)
    private int modifyX2(int x2) {
        return x2 - xShift;
    }

    @ModifyVariable(method = "renderScoreboardSidebar", at = @At(value = "STORE"), ordinal = 3)
    private int modifyY(int y) {
        return !module.isActive() ? y : MathHelper.clamp(y + module.yOff.get(), (y - scaledHeight / 2) * 3 + 9 + 1, scaledHeight - 1);
    }
}
