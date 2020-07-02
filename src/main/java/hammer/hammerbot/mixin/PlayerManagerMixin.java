package hammer.hammerbot.mixin;

import hammer.hammerbot.HammerBot;
import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    public void sendPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        JDA bot = HammerBot.bot.getBot();
        TextChannel linkChannel = (TextChannel) bot.getGuildChannelById(SettingsManager.INSTANCE.loadLongSettingOrDefault("linkChannelId", 0));
        if (linkChannel != null) {
            linkChannel.sendMessage(String.format("[%s] %s joined the server", SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP"), player.getDisplayName().asFormattedString())).queue();
        }
    }

    @Inject(method = "remove", at = @At("TAIL"))
    public void sendPlayerDisconnect(ServerPlayerEntity player, CallbackInfo ci) {
        JDA bot = HammerBot.bot.getBot();
        TextChannel linkChannel = (TextChannel) bot.getGuildChannelById(SettingsManager.INSTANCE.loadLongSettingOrDefault("linkChannelId", 0));
        if (linkChannel != null) {
            linkChannel.sendMessage(String.format("[%s] %s left the server", SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP"), player.getDisplayName().asFormattedString())).queue();
        }
    }
}
