package hammer.hammerbot;

import hammer.hammerbot.bot.Bot;
import hammer.hammerbot.settings.SettingsManager;
import hammer.hammerbot.util.command.CommandManager;
import hammer.hammerbot.util.command.Commands;
import net.fabricmc.api.DedicatedServerModInitializer;

public class HammerBot implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        SettingsManager.INSTANCE.initSettings();
        CommandManager.INSTANCE.parseCommandClass(Commands.class);
        new Bot();
    }
}
