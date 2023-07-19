package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import me.onlyrain.randomaddon.events.ScoreboardEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;

public class ScoreboardPlus extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgColors = settings.createGroup("Colors");

    public int listOff;

    public final Setting<Boolean> hide = sgGeneral.add(new BoolSetting.Builder()
        .name("hide")
        .defaultValue(false)
        .build()
    );

    public final Setting<Boolean> hideScores = sgGeneral.add(new BoolSetting.Builder()
        .name("hide-scores")
        .defaultValue(false)
        .build()
    );

    public final Setting<Integer> maxLineCount = sgGeneral.add(new IntSetting.Builder()
        .name("max-line-count")
        .defaultValue(15)
        .min(0)
        .sliderMax(30)
        .build()
    );

    public final Setting<Boolean> hideWhenDebugMenu = sgGeneral.add(new BoolSetting.Builder()
        .name("hide-when-debug-menu")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> left = sgGeneral.add(new BoolSetting.Builder()
        .name("left")
        .defaultValue(false)
        .build()
    );

    public final Setting<Integer> yOff = sgGeneral.add(new IntSetting.Builder()
        .name("y-offset")
        .defaultValue(0)
        .noSlider()
        .build()
    );

    public final Setting<Boolean> pages = sgGeneral.add(new BoolSetting.Builder()
        .name("pages")
        .defaultValue(true)
        .build()
    );

    public final Setting<Keybind> pagePrev = sgGeneral.add(new KeybindSetting.Builder()
        .name("previous-page")
        .defaultValue(Keybind.fromKey(GLFW_KEY_PAGE_UP))
        .action(() -> listOff++)
        .build()
    );

    public final Setting<Keybind> pageNext = sgGeneral.add(new KeybindSetting.Builder()
        .name("next-page")
        .defaultValue(Keybind.fromKey(GLFW_KEY_PAGE_DOWN))
        .action(() -> listOff--)
        .build()
    );

    public final Setting<SettingColor> titleBGColor = sgColors.add(new ColorSetting.Builder()
        .name("title-bg-color")
        .defaultValue(new Color(mc.options.getTextBackgroundColor(0.4f)))
        .build()
    );

    public final Setting<SettingColor> BGColor = sgColors.add(new ColorSetting.Builder()
        .name("bg-color")
        .defaultValue(new Color(mc.options.getTextBackgroundColor(0.3f)))
        .build()
    );

    public final Setting<Boolean> overrideTitleTextColor = sgColors.add(new BoolSetting.Builder()
        .name("override-title-text-color")
        .defaultValue(false)
        .build()
    );

    public final Setting<SettingColor> titleTextColor = sgColors.add(new ColorSetting.Builder()
        .name("title-text-color")
        .defaultValue(Color.WHITE)
        .build()
    );

    public final Setting<Boolean> overrideTextColor = sgColors.add(new BoolSetting.Builder()
        .name("override-text-color")
        .defaultValue(false)
        .build()
    );

    public final Setting<SettingColor> textColor = sgColors.add(new ColorSetting.Builder()
        .name("text-color")
        .defaultValue(Color.WHITE)
        .build()
    );

    public final Setting<SettingColor> scoreColor = sgColors.add(new ColorSetting.Builder()
        .name("score-color")
        .defaultValue(new Color(0xffff5555))
        .build()
    );

    public ScoreboardPlus() {
        super(Random.CATEGORY, "scoreboard-plus", "Various tweaks to your scoreboard.");
    }

    @Override
    public void onActivate() {
        listOff = 0;
    }

    @EventHandler
    private void onScoreboard(ScoreboardEvent event) {
        listOff = 0;
    }
}
