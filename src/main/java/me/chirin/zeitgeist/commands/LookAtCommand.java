package me.chirin.zeitgeist.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.chirin.zeitgeist.commands.arguements.PositionArgumentType;
import meteordevelopment.meteorclient.commands.Command;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityAnchorArgumentType;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class LookAtCommand extends Command {
    public LookAtCommand() {
        super("lookAt", "Looks at the specified location");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.then(argument("target", PositionArgumentType.pos()).executes(ctx -> {
            mc.player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, PositionArgumentType.getPos(ctx, "target"));
            return SINGLE_SUCCESS;
        }));
    }
}
