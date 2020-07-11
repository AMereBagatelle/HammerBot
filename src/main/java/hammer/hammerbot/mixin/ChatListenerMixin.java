package hammer.hammerbot.mixin;

import hammer.hammerbot.HammerBot;
import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayNetworkHandler.class)
public class ChatListenerMixin {
    @Shadow
    public ServerPlayerEntity player;

    /**
     * Passes our chat message to the chat bridge.
     */
    @Inject(method = "onChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Z)V"))
    public void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        String chatMessage = packet.getChatMessage();
        JDA bot = HammerBot.bot.getBot();
        TextChannel linkChannel = (TextChannel) bot.getGuildChannelById(SettingsManager.INSTANCE.loadLongSettingOrDefault("linkChannelId", 0));
        if (linkChannel != null) {
            linkChannel.sendMessage(String.format("[%s] <%s> %s", SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP"), player.getName().asFormattedString(), chatMessage)).queue();
        }
    }
}