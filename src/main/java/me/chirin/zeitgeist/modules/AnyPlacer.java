package me.chirin.zeitgeist.modules;

import me.chirin.zeitgeist.Zeitgeist;
import me.chirin.zeitgeist.utils.QolUtils;
import meteordevelopment.meteorclient.events.meteor.MouseButtonEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AnyPlacer extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> fluidplace = sgGeneral.add(new BoolSetting.Builder()
        .name("fluid-place")
        .description("Places on fluids.")
        .defaultValue(true)
        .build());

    public AnyPlacer() {
        super(Zeitgeist.CATEGORY, "anyplacer", "Place spawn eggs anywhere in creative");
    }

    @EventHandler
    private void onMouseButton(MouseButtonEvent event) {
        HitResult hitResult = mc.getCameraEntity().raycast(300, 0, fluidplace.get());
        Vec3d hr = hitResult.getPos();
        if (mc.currentScreen == null && mc.player.getMainHandStack().getItem() instanceof SpawnEggItem && mc.options.useKey.isPressed()) {
            ItemStack egg = mc.player.getInventory().getMainHandStack();
            String nbt = mc.player.getInventory().getMainHandStack().getOrCreateNbt().toString();

            NbtCompound tag = egg.getOrCreateSubNbt("EntityTag");
            NbtList list = new NbtList();
            list.add(NbtDouble.of(hr.x));
            list.add(NbtDouble.of(hr.y));
            list.add(NbtDouble.of(hr.z));
            tag.put("Pos", list);
            QolUtils.spawnItem(egg);
            BlockHitResult bhr = new BlockHitResult(mc.player.getPos(), Direction.DOWN, mc.player.getBlockPos(), false);
            mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0));
            QolUtils.spawnItem(QolUtils.createItem(nbt, egg.getItem()));
        }
    }

    @Override
    public void onActivate() {
        if (!mc.player.getAbilities().creativeMode) {
            error("You need to be in creative mode.");
            toggle();
        }
    }
}
