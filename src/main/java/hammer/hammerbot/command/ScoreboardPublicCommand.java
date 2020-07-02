package hammer.hammerbot.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hammer.hammerbot.util.Scoreboards;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ScoreboardPublicCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> scoreboardPublic = literal("scoreboardPublic")
                .then(argument("objective", StringArgumentType.string())
                        .executes(ctx -> {
                            Scoreboards.setScoreboardByName(StringArgumentType.getString(ctx, "objective"), ctx.getSource().getPlayer());
                            return 1;
                        }));

        dispatcher.register(scoreboardPublic);
    }
}
