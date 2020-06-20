package hammer.hammerbot.bot.smp;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import javax.annotation.Nonnull;

public class SMPCommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if(content.startsWith("/")) {
            String formattedContent = content.substring(1);
            // Member-only commands
            if(event.getMember().getRoles().contains(event.getGuild().getRoleById("711894618027065405"))) { //TODO Another setting
                if(formattedContent.equals("online")) {
                    sendOnlineListMessage(event.getChannel());
                }
            }
            // Admin-only commands
            if(event.getMember().getRoles().contains(event.getGuild().getRoleById(""))) { //TODO Another setting
                if(formattedContent.startsWith("whitelist")) {
                    String nextCommand = formattedContent.substring("whitelist".length()+1);
                    if(nextCommand.startsWith("add")) {
                        String player = nextCommand.substring("add".length()+1);
                        whitelistPlayer(player);
                    } else if(nextCommand.startsWith("remove")) {
                        String player = nextCommand.substring("remove".length()+1);
                        removePlayerFromWhitelist(player);
                    }
                }
            }
        }
    }

    public void sendOnlineListMessage(MessageChannel channel) {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        String[] players = server.getPlayerManager().getPlayerNames();
        StringBuilder finalMessage = new StringBuilder("Currently online players:\n");
        for (String player : players) {
            finalMessage.append(player).append("\n");
        }
        if(players.length == 0) {
            finalMessage = new StringBuilder("No players currently online!");
        }
        channel.sendMessage(finalMessage.toString()).queue();
    }

    public void whitelistPlayer(String player) {
        MinecraftDedicatedServer server = (MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance();
        server.enqueueCommand("whitelist add " + player, server.getCommandSource());
    }

    public void removePlayerFromWhitelist(String player) {
        MinecraftDedicatedServer server = (MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance();
        server.enqueueCommand("whitelist remove " + player, server.getCommandSource());
    }
}
