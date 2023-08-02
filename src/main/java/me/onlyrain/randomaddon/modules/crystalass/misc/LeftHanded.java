package me.onlyrain.randomaddon.modules.crystalass.misc;

import me.onlyrain.randomaddon.RandomAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.util.Arm;

public class LeftHanded extends Module {
    public LeftHanded() {
        super(RandomAddon.CATEGORYC, "Left handed", "CRYSTAL || Changes your main arm to your left arm.");
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
