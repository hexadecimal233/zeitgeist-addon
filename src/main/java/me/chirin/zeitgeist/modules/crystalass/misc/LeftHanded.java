package me.chirin.zeitgeist.modules.crystalass.misc;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.util.Arm;

public class LeftHanded extends Module {
    public LeftHanded() {
        super(Zeitgeist.CATEGORYC, "Left handed", "CRYSTAL || Changes your main arm to your left arm.");
    }

    public void onActivate() {
        if (mc.player != null && mc.world != null) {
            mc.player.setMainArm(Arm.LEFT);
        }
    }

    public void onDeactivate() {
        if (mc.player != null && mc.world != null) {
            mc.player.setMainArm(Arm.RIGHT);
        }
    }
}
