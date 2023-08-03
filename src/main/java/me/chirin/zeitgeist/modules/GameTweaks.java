package me.chirin.zeitgeist.modules;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;

public class GameTweaks extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> lessAnnoyingBats = sgGeneral.add(new BoolSetting.Builder()
        .name("less-annoying-bats")
        .description("Makes bats less annoying and obtrusive.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> clipboardScreenshots = sgGeneral.add(new BoolSetting.Builder()
        .name("copy-screenshots")
        .description("Copy screenshots to the clipboard instead of creating a file in the game directory.")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> showScore = sgGeneral.add(new BoolSetting.Builder()
        .name("score")
        .description("Whether or not to show the weird Score on the death screen.")
        .defaultValue(true)
        .build());

    public GameTweaks() {
        super(Zeitgeist.CATEGORY, "game-tweaks", "Minor changes to the game experience to improve gameplay.");
    }

    public boolean bats() {
        return isActive() && lessAnnoyingBats.get();
    }

    public boolean noScore() {
        return isActive() && showScore.get();
    }

    public boolean screenshots() {
        return isActive() && clipboardScreenshots.get();
    }
}
