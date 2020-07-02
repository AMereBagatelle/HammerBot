package hammer.hammerbot.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayNetworkHandler.class)
public class ChatListenerMixin {
    @Inject(method = "onChatMessage", at = @At("RETURN"))
    public void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        String chatMessage = packet.getChatMessage();
    }
}