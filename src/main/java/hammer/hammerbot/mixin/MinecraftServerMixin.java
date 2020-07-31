package hammer.hammerbot.mixin;

import hammer.hammerbot.HammerBot;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "shutdown", at = @At("TAIL"))
    private void shutdownBot(CallbackInfo ci) {
        LOGGER.info("Shutting down HammerBot");
        HammerBot.bot.getBot().shutdownNow();
    }
}
