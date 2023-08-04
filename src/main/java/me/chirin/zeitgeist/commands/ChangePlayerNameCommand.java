package me.chirin.zeitgeist.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.chirin.zeitgeist.commands.arguements.PlayerNameArgumentType;
import me.chirin.zeitgeist.mixins.GameProfileAccessor;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class ChangePlayerNameCommand extends Command {
    public ChangePlayerNameCommand() {
        super("change-player-name", "change certain player's name");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", PlayerNameArgumentType.player()).then(argument("message", StringArgumentType.greedyString()).executes(ctx -> {
            String playerName = PlayerNameArgumentType.getPlayer(ctx, "player");
            String name = ctx.getArgument("message", String.class).replace("&", "\247");
            for (AbstractClientPlayerEntity p : mc.world.getPlayers()) {
                if (p.getGameProfile().getName().equals(playerName)) {
                    ((GameProfileAccessor) p.getGameProfile()).setName(name);
                    info("success");
                }
            }
            return SINGLE_SUCCESS;
        })));
    }
}
