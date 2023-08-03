package me.chirin.zeitgeist.modules;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.Freecam;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class BuildRandom extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
            .name("range")
            .description("Range.")
            .defaultValue(5)
            .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
            .name("rotate")
            .description("Rotate.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Integer> maxAttempts = sgGeneral.add(new IntSetting.Builder()
            .name("max-attempts")
            .description("Maximum number os positions it will try to place in.")
            .min(1)
            .max(1024)
            .sliderMin(1)
            .defaultValue(128)
            .build()
    );

    private final Random random = new Random();

    public BuildRandom() {
        super(Zeitgeist.CATEGORY, "build-random", "Randomly place blocks around you :)");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (Modules.get().isActive(Freecam.class)) {
            return;
        }

        int blockRange = range.get();
        int bound = blockRange * 2 + 1;

        FindItemResult result = InvUtils.findInHotbar(stack -> !stack.isEmpty() && stack.getItem() instanceof BlockItem);
        if (!result.found()) return;

        for (int i = 0; i < maxAttempts.get(); i++) {
            BlockPos pos = BlockPos.ofFloored(mc.player.getEyePos().add(random.nextInt(bound) - blockRange, random.nextInt(bound) - blockRange, random.nextInt(bound) - blockRange));
            if (BlockUtils.place(pos, result, rotate.get(), 10)) break;
        }
    }
}
