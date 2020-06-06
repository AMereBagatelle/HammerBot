package hammer.hammerbot;

import hammer.hammerbot.discord.Bot;
import hammer.hammerbot.settings.SettingsManager;
import net.fabricmc.api.ModInitializer;

public class HammerBot implements ModInitializer {
    public static Bot discordBot;
    public static Scoreboards scoreboard;
    public static SettingsManager settingsManager;

    @Override
    public void onInitialize() {
        settingsManager = new SettingsManager();
        settingsManager.initSettings();
        discordBot = new Bot();
        scoreboard = new Scoreboards();
    }
}
