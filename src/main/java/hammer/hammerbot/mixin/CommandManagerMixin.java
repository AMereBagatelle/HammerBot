package hammer.hammerbot.mixin;

import com.mojang.brigadier.CommandDispatcher;
import hammer.hammerbot.command.PingCommand;
import hammer.hammerbot.command.ScoreboardPublicCommand;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    /**
     * Registers our command to the server.
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onRegister(boolean isDedicatedServer, CallbackInfo ci) {
        ScoreboardPublicCommand.register(dispatcher);
        PingCommand.register(dispatcher);
    }
}
