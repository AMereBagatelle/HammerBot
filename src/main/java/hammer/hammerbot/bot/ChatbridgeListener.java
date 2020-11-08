package hammer.hammerbot.bot;

import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;

import javax.annotation.Nonnull;

public class ChatbridgeListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (event.getChannel().getId().equals(Long.toString(SettingsManager.settings.linkChannelId)) && !message.startsWith("[" + SettingsManager.settings.serverType + "]")) {
            MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
            if (message.startsWith("[SMP]")) {
                server.getPlayerManager().broadcastChatMessage(new LiteralText(String.format("[SMP] %s", event.getMessage().getContentRaw().substring(6))), false);
            } else if (message.startsWith("[CMPFLAT]")) {
                server.getPlayerManager().broadcastChatMessage(new LiteralText(String.format("[CMPFLAT] %s", event.getMessage().getContentRaw().substring(10))), false);
            } else if (message.startsWith("[CMPCOPY]")) {
                server.getPlayerManager().broadcastChatMessage(new LiteralText(String.format("[CMPCOPY] %s", event.getMessage().getContentRaw().substring(10))), false);
            } else {
                server.getPlayerManager().broadcastChatMessage(new LiteralText(String.format("[DISCORD] <%s> %s", event.getAuthor().getName(), event.getMessage().getContentRaw())), false);
            }
        }
    }
}
