package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.RandomAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;

public class GhostBlockFly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<Block>> block = sgGeneral.add(new BlockListSetting.Builder()
        .name("block")
        .description("The ghost block created (if multiple are selected the top one will be used)")
        .defaultValue(Blocks.BARRIER)
        .build());
    BlockPos pos = null;
    BlockState state = null;
    int timer = 0;

    public GhostBlockFly() {
        super(RandomAddon.CATEGORY, "ghost-block-fly", "Fly using ghost blocks");
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (block.get().isEmpty()) {
            if (mc.world.getBlockState(pos).getBlock().getDefaultState() == state)
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(mc.player.getPos().add(0, -1, 0), Direction.UP, pos, false));
            return;
        }

        if (mc.player.getBlockPos().add(0, -1, 0) != pos && pos != null && state != null)
            mc.world.setBlockState(pos, state);

        pos = mc.player.getBlockPos().add(0, -1, 0);
        state = mc.world.getBlockState(pos);

        if (!mc.options.sneakKey.isPressed() && mc.world.getBlockState(pos).getBlock() instanceof AirBlock && pos != null) {
            mc.world.setBlockState(pos, block.get().get(0).getDefaultState());
        }

        if (mc.options.sneakKey.isPressed() && mc.world.getBlockState(pos).getBlock() instanceof AirBlock && pos != null) {
            mc.world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void onDeactivate() {
        mc.world.setBlockState(pos, state);
        pos = null;
        state = null;
        timer = 0;
    }
}
