package me.chirin.zeitgeist.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.chirin.zeitgeist.utils.ClipUtils;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.Vec3d;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class LOClipCommand extends Command {
    public LOClipCommand() {
        super("lo-clip", "Doing some clips");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("vclip")  // Vertical clip
            .then(argument("distance", IntegerArgumentType.integer())
                .executes(context -> {
                    int distance = context.getArgument("distance", Integer.class);

                    Vec3d pos = mc.player.getPos();
                    Vec3d targetPos = pos.add(0, distance, 0);

                    ClipUtils.clipStraight(targetPos);

                    return 1;
                })
            )
        );

        builder.then(literal("hclip")  // Horizontal clip (up -> horizontal -> down: to go through walls)
            .then(argument("distance", IntegerArgumentType.integer())
                .executes(context -> {
                    int distance = context.getArgument("distance", Integer.class);

                    // Move `direction` blocks into viewing direction
                    Vec3d targetPos = mc.player.getPos().add(
                        mc.player.getRotationVector().multiply(1, 0, 1).normalize().multiply(distance)
                    );
                    ClipUtils.clipUpDown(targetPos);

                    return 1;
                })
            )
        );

        builder.then(literal("dclip")  // Directional clip
            .then(argument("distance", IntegerArgumentType.integer())
                .executes(context -> {
                    int distance = context.getArgument("distance", Integer.class);
                    Vec3d pos = mc.player.getPos();
                    // Move into players viewing direction
                    Vec3d targetPos = pos.add(mc.player.getRotationVector().normalize().multiply(distance));

                    ClipUtils.clipStraight(targetPos);

                    return 1;
                })
            )
        );

        builder.then(literal("autoclip")
            .then(literal("up")
                .executes(context -> ClipUtils.executeAutoClip(1))
            )
            .then(literal("down")
                .executes(context -> ClipUtils.executeAutoClip(-1))
            )
        );
    }
}
