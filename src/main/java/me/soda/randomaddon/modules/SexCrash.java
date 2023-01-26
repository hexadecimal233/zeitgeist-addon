package me.soda.randomaddon.modules;

import me.soda.randomaddon.Random;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class SexCrash extends Module {
    public SexCrash() {
        super(Random.CATEGORY, "sex-crash", "unfinished");
    }

    int timer;

    @Override
    public void onActivate() {
        PlayerUtils.centerPlayer();
        mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, createItem("{}", Items.CHEST)));
        BlockPos a = new BlockPos(mc.player.getBlockPos().getX() + 1, mc.player.getBlockPos().getY(), mc.player.getBlockPos().getZ());
        BlockPos b = new BlockPos(mc.player.getBlockPos().getX() + 2, mc.player.getBlockPos().getY(), mc.player.getBlockPos().getZ());

        if (mc.world.getBlockState(a).getBlock() != Blocks.AIR)
            BlockUtils.breakBlock(a, false);
        if (mc.world.getBlockState(b).getBlock() != Blocks.AIR)
            BlockUtils.breakBlock(b, false);

        BlockUtils.place(a, Hand.MAIN_HAND, mc.player.getInventory().selectedSlot, false, 0, false, true, true);
        BlockUtils.place(b, Hand.MAIN_HAND, mc.player.getInventory().selectedSlot, false, 0, false, true, true);
        mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, new ItemStack(Items.AIR)));
        mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(a.getX(), a.getY(), a.getZ()), Direction.DOWN, a, false), 0));
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (timer == 0) {
            mc.player.setVelocity(0, 0, 0);
            BlockPos a = new BlockPos(mc.player.getBlockPos().getX() + 1, mc.player.getBlockPos().getY(), mc.player.getBlockPos().getZ());
            mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(a.getX(), a.getY(), a.getZ()), Direction.DOWN, a, false), 0));
        }

        if (timer < 55) {
            mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(81 + mc.player.getInventory().selectedSlot, book()));
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 81 + mc.player.getInventory().selectedSlot, 0, SlotActionType.QUICK_MOVE, mc.player);
        }
        timer++;

        if (timer >= 55) {
            timer = 0;
            info("Spam click the chest");
            toggle();
        }
    }


    public static ItemStack createItem(String nbt, Item item) {
        try {
            ItemStack stack = new ItemStack(item);
            stack.setNbt(StringNbtReader.parse(nbt));
            return stack;
        } catch (Exception ignored) {
            return new ItemStack(item);
        }
    }

    private ItemStack book() { // incomplete
        return createItem("{}", Items.WRITTEN_BOOK);
    }
}
