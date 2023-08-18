package me.chirin.zeitgeist.modules;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.systems.modules.Module;

public class SkinProtection extends Module {
    public static final Module INSTANCE = new SkinProtection();
    public SkinProtection() {
        super(Zeitgeist.CATEGORY, "skin-protection", "everyone is steve");
    }
}
