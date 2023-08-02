package me.onlyrain.randomaddon.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.onlyrain.randomaddon.utils.QolUtils;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class EffectCommand extends Command {
    public EffectCommand() {
        super("effect", "gives you a potion effect");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("effect", RegistryEntryArgumentType.registryEntry(REGISTRY_ACCESS, RegistryKeys.STATUS_EFFECT))).then(argument("duration", IntegerArgumentType.integer()).then(argument("potency", IntegerArgumentType.integer()).executes(ctx -> {
            ItemStack bfr = mc.player.getMainHandStack();
            ItemStack egg = new ItemStack(Items.SALMON_SPAWN_EGG);
            BlockHitResult bhr = new BlockHitResult(mc.player.getEyePos(), Direction.DOWN, BlockPos.ofFloored(mc.player.getEyePos()), false);
            int potency = ctx.getArgument("potency", Integer.class) - 1;
            RegistryEntry.Reference<StatusEffect> effect = ctx.getArgument("effect", RegistryEntry.Reference.class);
            info(effect.value().toString());
            egg.setNbt(StringNbtReader.parse("{EntityTag:{id:\"minecraft:area_effect_cloud\",Duration:2,Radius:2,WaitTime:0,Effects:[{Id:" + effect + ",Amplifier:" + potency + ",Duration:" + ctx.getArgument("duration", Integer.class) * 20 + "}]}}"));
            QolUtils.spawnItem(egg);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
            QolUtils.spawnItem(bfr);
            return SINGLE_SUCCESS;
        })));
    }
}
