package me.onlyrain.randomaddon.modules;

import me.onlyrain.randomaddon.Random;
import me.onlyrain.randomaddon.utils.QolUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class ProjectileDeflector extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .defaultValue(Modes.Normal)
        .build());

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .defaultValue(4)
        .sliderRange(3, 6)
        .build());

    private final Setting<Boolean> check = sgGeneral.add(new BoolSetting.Builder()
        .name("velocity-check")
        .description("checks if the projectile is coming towards you")
        .defaultValue(true)
        .build());

    public ProjectileDeflector() {
        super(Random.CATEGORY, "projectile-deflector", "omg it works!");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.player.isAlive() || mc.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR) return;
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof ShulkerBulletEntity && QolUtils.inRange(entity.getPos(), range.get())) {
                mc.interactionManager.attackEntity(mc.player, entity);
            } else if (entity instanceof FireballEntity && QolUtils.inRange(entity.getPos(), range.get()) && isApproaching(entity)) {
                switch (mode.get()) {
                    case Up -> {
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(), -90, mc.player.isOnGround()));
                        mc.interactionManager.attackEntity(mc.player, entity);
                    }
                    case Reflect -> {
                        Vec3d pos = mc.player.getPos().add(entity.getPos().multiply(mc.player.getPos().distanceTo(entity.getPos())));
                        Vec3d bfr = mc.player.getPos().add(mc.player.getRotationVector());
                        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(0, 0, mc.player.isOnGround()));
                        mc.player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, pos);
                        mc.interactionManager.attackEntity(mc.player, entity);
                        mc.player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, bfr);
                    }
                    case Normal -> {
                        mc.interactionManager.attackEntity(mc.player, entity);
                    }
                }
            }
        }
    }

    private boolean isApproaching(Entity entity) {
        if (check.get()) return true;
        return !(mc.player.getPos().distanceTo(entity.getPos().add(entity.getVelocity())) < mc.player.getPos().distanceTo(entity.getPos()));
    }

    public enum Modes {
        Up, Normal, Reflect
    }
}
