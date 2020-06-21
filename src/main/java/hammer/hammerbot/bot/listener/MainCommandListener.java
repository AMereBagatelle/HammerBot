package hammer.hammerbot.bot.listener;

import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import javax.annotation.Nonnull;

public class MainCommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        SettingsManager settingsManager = SettingsManager.INSTANCE;
        if(FabricLoader.getInstance().getGameInstance() != null) {
            Message message = event.getMessage();
            String content = message.getContentRaw();
            if (content.startsWith("/")) {
                String formattedContent = content.substring(1);
                // Member-only commands
                if (event.getMember().getRoles().contains(event.getGuild().getRoleById(settingsManager.loadSettingOrDefault("memberRoleId", "")))) { //TODO Another setting
                    if (formattedContent.equals("online")) {
                        sendOnlineListMessage(event.getChannel());
                    }
                }
                // Admin-only commands
                if (event.getMember().getRoles().contains(event.getGuild().getRoleById(settingsManager.loadSettingOrDefault("adminRoleId", "")))) { //TODO Another setting
                    if (formattedContent.startsWith("whitelist")) {
                        String nextCommand = formattedContent.substring("whitelist".length() + 1);
                        if (nextCommand.startsWith("add")) {
                            String player = nextCommand.substring("add".length() + 1);
                            whitelistPlayer(player, event.getChannel());
                        } else if (nextCommand.startsWith("remove")) {
                            String player = nextCommand.substring("remove".length() + 1);
                            removePlayerFromWhitelist(player, event.getChannel());
                        }
                    }
                }
            }
        } else {
            event.getChannel().sendMessage("Server is null!  Is it not up yet?").queue();
        }
    }

    public void sendOnlineListMessage(MessageChannel channel) {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        String[] players = server.getPlayerManager().getPlayerNames();
        StringBuilder finalMessage = new StringBuilder(String.format("Currently online players on %s:\n", SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP")));
        for (String player : players) {
            finalMessage.append(player).append("\n");
        }
        if(players.length == 0) {
            finalMessage = new StringBuilder(String.format("No players currently online on %s!", SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP")));
        }
        channel.sendMessage(finalMessage.toString()).queue();
    }

    public void whitelistPlayer(String player, MessageChannel eventChannel) {
        MinecraftDedicatedServer server = (MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance();
        server.enqueueCommand("whitelist add " + player, server.getCommandSource());
        eventChannel.sendMessage(String.format("Player %s removed from %s", player, SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP"))).queue();
    }

    public void removePlayerFromWhitelist(String player, MessageChannel eventChannel) {
        MinecraftDedicatedServer server = (MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance();
        server.enqueueCommand("whitelist remove " + player, server.getCommandSource());
        eventChannel.sendMessage(String.format("Player %s removed from %s.", player, SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP"))).queue();
    }
}