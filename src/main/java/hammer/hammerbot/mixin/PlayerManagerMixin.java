package hammer.hammerbot.mixin;

import hammer.hammerbot.HammerBot;
import hammer.hammerbot.settings.SettingsManager;
import hammer.hammerbot.util.MessageFormatting;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    /**
     * Sends player join message to chat bridge.
     */
    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    public void sendPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        JDA bot = HammerBot.bot.getBot();
        TextChannel linkChannel = (TextChannel) bot.getGuildChannelById(SettingsManager.settings.linkChannelId);
        if (linkChannel != null) {
            linkChannel.sendMessage(MessageFormatting.removeFormattingFromString(String.format("[%s] %s joined the server", SettingsManager.settings.serverType, player.getDisplayName().asFormattedString()))).queue();
        }
    }

    /**
     * Sends player disconnect message to chat bridge.
     */
    @Inject(method = "remove", at = @At("TAIL"))
    public void sendPlayerDisconnect(ServerPlayerEntity player, CallbackInfo ci) {
        JDA bot = HammerBot.bot.getBot();
        TextChannel linkChannel = (TextChannel) bot.getGuildChannelById(SettingsManager.settings.linkChannelId);
        if (linkChannel != null) {
            linkChannel.sendMessage(MessageFormatting.removeFormattingFromString(String.format("[%s] %s left the server", SettingsManager.settings.serverType, player.getDisplayName().asFormattedString()))).queue();
        }
    }
}
