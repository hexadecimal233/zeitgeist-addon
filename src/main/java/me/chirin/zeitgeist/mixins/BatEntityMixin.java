package me.chirin.zeitgeist.mixins;

import me.chirin.zeitgeist.modules.GameTweaks;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BatEntity.class)
public abstract class BatEntityMixin extends AmbientEntity {

    protected BatEntityMixin(EntityType<? extends AmbientEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract float getSoundPitch();

    @Override
    public float getSoundVolume() {
        if (Modules.get().get(GameTweaks.class).bats())
            return 0;
        else
            return 0.1f;
    }

    @Override
    public boolean canHit() {
        if (Modules.get().get(GameTweaks.class).bats())
            return false;
        else
            return !this.isRemoved();
    }

}
