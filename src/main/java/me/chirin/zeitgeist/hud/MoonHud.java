package me.chirin.zeitgeist.hud;

import meteordevelopment.meteorclient.gui.renderer.packer.TextureRegion;
import meteordevelopment.meteorclient.renderer.GL;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringHelper;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class MoonHud extends HudElement {
    private static final Identifier TEXTURE = new Identifier("zeitgeist-addon", "moon_phases_icons.png");
    private static final double IMG_SIZE = 18;
    private static double imgSize = 18;    public static final HudElementInfo<MoonHud> INFO = new HudElementInfo<>(Hud.GROUP, "moon-hud", "Show moon phase.", MoonHud::new);
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgScale = settings.createGroup("Scale");
    private final Setting<Boolean> shadow = sgGeneral.add(new BoolSetting.Builder()
        .name("shadow")
        .description("Renders shadow behind text.")
        .defaultValue(true)
        .build()
    );
    // General
    private final Setting<Boolean> icon = sgGeneral.add(new BoolSetting.Builder()
        .name("icon")
        .description("Show moon icon.")
        .defaultValue(false)
        .build()
    );
    private final Setting<Boolean> customScale = sgScale.add(new BoolSetting.Builder()
        .name("custom-scale")
        .description("Applies custom text scale rather than the global one.")
        .defaultValue(false)
        .build()
    );
    // Scale
    private final Setting<Double> scale = sgScale.add(new DoubleSetting.Builder()
        .name("scale")
        .description("Custom scale.")
        .visible(customScale::get)
        .defaultValue(1)
        .min(0.5)
        .sliderRange(0.5, 3)
        .build()
    );
    String size = "";
    String nextPhase = "";
    TextureRegion region = new TextureRegion(0, 0);
    public MoonHud() {
        super(INFO);
    }

    @Override
    public void tick(HudRenderer renderer) {
        double w = 0;
        double h = 0;
        if (!Utils.canUpdate()) {
            size = "Size: 100%";
            nextPhase = "Next phase in: 00:00";
        } else {
            if (icon.get()) {
                imgSize = IMG_SIZE * getScale() * mc.getWindow().getScaleFactor();
                w = imgSize;
                h = imgSize;
                int phase = mc.world.getMoonPhase();
                double x = (((4 - phase % 4) % 4) * IMG_SIZE);
                double y = phase >= 1 && phase <= 4 ? IMG_SIZE : 0;
                region = new TextureRegion(0, 0);
                region.x1 = x / (IMG_SIZE * 4);
                region.x2 = (x + IMG_SIZE) / (IMG_SIZE * 4);
                region.y1 = y / (IMG_SIZE * 2);
                region.y2 = (y + IMG_SIZE) / (IMG_SIZE * 2);
            }

            int sizePercent = (int) (mc.world.getMoonSize() * 100f);
            size = "Size: " + sizePercent + "%";
            nextPhase = "Next phase in: " + StringHelper.formatTicks(24000 - (int) (mc.world.getTimeOfDay() % 24000));
        }

        w += renderer.textWidth(nextPhase, shadow.get(), getScale());
        h = Math.max(h, renderer.textHeight(shadow.get(), getScale()) * 2);
        setSize(w, h);
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = this.x;
        double y = this.y;

        if (Utils.canUpdate() && icon.get()) {
            GL.bindTexture(TEXTURE);
            Renderer2D.TEXTURE.begin();
            Renderer2D.TEXTURE.texQuad(this.x, this.y, imgSize, imgSize, region, Color.WHITE);
            Renderer2D.TEXTURE.render(null);
            x += imgSize;
        }

        renderer.text(size, x, y, Color.WHITE, shadow.get(), getScale());
        y += renderer.textHeight(shadow.get(), getScale());
        renderer.text(nextPhase, x, y, Color.WHITE, shadow.get(), getScale());
    }

    private double getScale() {
        return customScale.get() ? scale.get() : 1;
    }
}
