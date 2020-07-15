package hammer.hammerbot.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hammer.hammerbot.HammerBot;
import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PingCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> ping = literal("ping")
                .then(argument("name", StringArgumentType.string()))
                .executes(ctx -> {
                    String nameToPing = StringArgumentType.getString(ctx, "name");
                    TextChannel linkChannel = (TextChannel) HammerBot.bot.getBot().getGuildChannelById(SettingsManager.INSTANCE.loadLongSettingOrDefault("linkChannelId", 0));
                    List<Member> matchingMembers = linkChannel.getGuild().getMembersByName(nameToPing, true);
                    if (!matchingMembers.isEmpty()) {
                        linkChannel.sendMessage(matchingMembers.get(0).getAsMention()).queue();
                    } else {
                        ctx.getSource().sendFeedback(new LiteralText("Couldn't find members to ping."), false);
                    }
                    return 1;
                });
        dispatcher.register(ping);
    }
}
