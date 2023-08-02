package me.onlyrain.randomaddon.modules.crystalass.misc;

import me.onlyrain.randomaddon.RandomAddon;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.util.math.MathHelper.cos;

public class ElytraFix extends Module {

    private final Setting<Boolean> instantFly;

    private final Setting<Boolean> speedCtrl;

    private final Setting<Boolean> heightCtrl;

    private final Setting<Boolean> stopInWater;
    private final Setting<Boolean> stopInLava;

    private final Setting<Boolean> autoDisable;

    private int jumpTimer;

    public ElytraFix() {
        super(RandomAddon.CATEGORYC, "Elytra Fix", "CRYSTAL || Easier elytra");
        SettingGroup sgGeneral = settings.getDefaultGroup();
        instantFly = sgGeneral.add(new BoolSetting.Builder()
            .name("Instant Fly")
            .description("Jump to fly, no weird double-jump needed!")
            .defaultValue(true)
            .build()
        );
        speedCtrl = sgGeneral.add(new BoolSetting.Builder()
            .name("Speed Control")
            .description("Control your speed with the Forward and Back keys.")
            .defaultValue(true)
            .build()
        );
        heightCtrl = sgGeneral.add(new BoolSetting.Builder()
            .name("Height Control")
            .description("Control your height with the Jump and Sneak keys.")
            .defaultValue(false)
            .build()
        );
        stopInWater = sgGeneral.add(new BoolSetting.Builder()
            .name("Stop in water")
            .description("Stop flying in water")
            .defaultValue(true)
            .build()
        );
        stopInLava = sgGeneral.add(new BoolSetting.Builder()
            .name("Stop in lava")
            .description("Stop flying in lava")
            .defaultValue(true)
            .build()
        );
        autoDisable = sgGeneral.add(new BoolSetting.Builder()
            .name("Auto Disable")
            .description("Disables module on kick.")
            .defaultValue(true)
            .build()
        );
    }

    @Override
    public void onActivate() {
        jumpTimer = 0;
    }

    @EventHandler
    public void onGameLeft(GameLeftEvent event) {
        if (!autoDisable.get()) {
            return;
        }
        toggle();
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (jumpTimer <= 0) {
        } else {
            jumpTimer--;
        }

        assert mc.player != null;
        ItemStack chest = mc.player.getEquippedStack(EquipmentSlot.CHEST);
        if (chest.getItem() == Items.ELYTRA) {
            if (!mc.player.isFallFlying()) {
                if (!ElytraItem.isUsable(chest) || !mc.options.jumpKey.isPressed()) {
                    return;
                }
                doInstantFly();
            } else {
                if (!mc.player.isTouchingWater()) {
                    controlSpeed();
                    controlHeight();
                    return;
                } else if (!stopInWater.get()) {
                    controlSpeed();
                    controlHeight();
                    return;
                } else if (!mc.player.isInLava()) {
                    controlSpeed();
                    controlHeight();
                    return;
                } else if (!stopInLava.get()) {
                    controlSpeed();
                    controlHeight();
                } else {
                    sendStartStopPacket();
                    return;
                }

            }

        }

    }

    private void sendStartStopPacket() {
        ClientCommandC2SPacket packet = null;
        if (mc.player != null) {
            packet = new ClientCommandC2SPacket(mc.player,
                ClientCommandC2SPacket.Mode.START_FALL_FLYING);
        }
        if (mc.player != null) {
            mc.player.networkHandler.sendPacket(packet);
        }
    }

    private void controlHeight() {
        if (heightCtrl.get()) {
            Vec3d v = null;
            if (mc.player != null) {
                v = mc.player.getVelocity();
            }

            if (!mc.options.jumpKey.isPressed()) {
                if (!mc.options.sneakKey.isPressed()) {
                    return;
                }
                mc.player.setVelocity(v.x, v.y - 0.04, v.z);
            } else {
                mc.player.setVelocity(v.x, v.y + 0.08, v.z);
            }
        }

    }

    private void controlSpeed() {
        if (speedCtrl.get()) {
            float yaw = 0;
            if (mc.player != null) {
                yaw = (float) Math.toRadians(mc.player.getYaw());
            }
            Vec3d forward = new Vec3d(-MathHelper.sin(yaw) * 0.05, 0,
                cos(yaw) * 0.05);

            Vec3d v = mc.player.getVelocity();

            if (!mc.options.forwardKey.isPressed()) {
                if (!mc.options.backKey.isPressed()) {
                    return;
                }
                mc.player.setVelocity(v.subtract(forward));
            } else {
                mc.player.setVelocity(v.add(forward));
            }
        }

    }

    private void doInstantFly() {
        if (instantFly.get()) {
            if (jumpTimer > 0) {
            } else {
                jumpTimer = 20;
                if (mc.player != null) {
                    mc.player.setJumping(false);
                }
                mc.player.setSprinting(true);
                mc.player.jump();
            }

            sendStartStopPacket();
        }

    }
}
