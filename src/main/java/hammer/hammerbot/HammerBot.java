package hammer.hammerbot;

import hammer.hammerbot.bot.Bot;
import hammer.hammerbot.settings.SettingsManager;
import hammer.hammerbot.util.command.CommandManager;
import hammer.hammerbot.util.command.Commands;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HammerBot implements DedicatedServerModInitializer {
    public static Bot bot;

    public static final Logger LOGGER = LogManager.getLogger("HammerBot");

    @Override
    public void onInitializeServer() {
        SettingsManager.initSettings();
        CommandManager.INSTANCE.parseCommandClass(Commands.class);
        bot = new Bot();
    }
}
