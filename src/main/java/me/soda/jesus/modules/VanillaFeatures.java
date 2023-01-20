package me.soda.jesus.modules;

import me.soda.jesus.Addon;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;

public class VanillaFeatures extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> hudHidden = sgGeneral.add(new BoolSetting.Builder()
        .name("hide-HUD")
        .description("Hide the player's HUD.")
        .defaultValue(mc.options.hudHidden)
        .onChanged(b -> mc.options.hudHidden = b)
        .build());

    private final Setting<Boolean> pauseOnLostFocus = sgGeneral.add(new BoolSetting.Builder()
        .name("pause-on-lost-focus")
        .description("Whether or not to pause the game when you're tabbed out.")
        .defaultValue(mc.options.pauseOnLostFocus)
        .onChanged(b -> mc.options.pauseOnLostFocus = b)
        .build());

    private final Setting<Boolean> heldItemTooltips = sgGeneral.add(new BoolSetting.Builder()
        .name("held-item-tooltips")
        .description("Whether or not to display an item's name above your hotbar when you swap to it.")
        .defaultValue(mc.options.heldItemTooltips)
        .onChanged(b -> mc.options.heldItemTooltips = b)
        .build());

    private final Setting<Boolean> skipMultiplayerWarning = sgGeneral.add(new BoolSetting.Builder()
        .name("skip-multiplayer-warning")
        .description("Skip the Multiplayer warning.")
        .defaultValue(mc.options.skipMultiplayerWarning)
        .onChanged(b -> mc.options.skipMultiplayerWarning = b)
        .build());

    private final Setting<Boolean> smoothCameraEnabled = sgGeneral.add(new BoolSetting.Builder()
        .name("cinematic-camera")
        .description("Smoothen your camera movements.")
        .defaultValue(mc.options.smoothCameraEnabled)
        .onChanged(b -> mc.options.smoothCameraEnabled = b)
        .build());

    private final Setting<Boolean> advancedTooltips = sgGeneral.add(new BoolSetting.Builder()
        .name("advanced-tooltips")
        .description("Advanced item tooltips in your inventory, showing durability, item ID, etc.")
        .defaultValue(mc.options.advancedItemTooltips)
        .onChanged(b -> mc.options.advancedItemTooltips = b)
        .build());

    public VanillaFeatures() {
        super(Addon.CATEGORY, "vanilla-features", "Adds Meteor settings for easier access to Minecraft's toggle settings.");
    }
}
