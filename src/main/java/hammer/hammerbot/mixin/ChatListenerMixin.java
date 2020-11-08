package hammer.hammerbot.mixin;

import hammer.hammerbot.HammerBot;
import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
    @Inject(method = "method_31286", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    public void onChatMessage(String string, CallbackInfo ci) {
        JDA bot = HammerBot.bot.getBot();
        TextChannel linkChannel = (TextChannel) bot.getGuildChannelById(SettingsManager.settings.linkChannelId);
        if (linkChannel != null) {
            linkChannel.sendMessage(String.format("[%s] <%s> %s", SettingsManager.settings.serverType, player.getName().asString(), string)).queue();
        }
    }
}