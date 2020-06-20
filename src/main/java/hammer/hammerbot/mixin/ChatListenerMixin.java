package hammer.hammerbot.mixin;

import hammer.hammerbot.util.Scoreboards;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(PlayerManager.class)
public class ChatListenerMixin {
    @Inject(method = "broadcastChatMessage", at = @At("RETURN"))
    public void onChatMessage(Text text, boolean system, CallbackInfo cbi) {
        String chatMessage = text.asFormattedString();
        String formattedChatMessage = chatMessage.substring(chatMessage.indexOf(">")+2);
        if(formattedChatMessage.startsWith("!s")) {
            Scoreboards.setScoreboardByName(formattedChatMessage.substring(3));
        }
    }
}