package hammer.hammerbot;

import hammer.hammerbot.settings.SettingsManager;
import net.fabricmc.api.ModInitializer;

public class HammerBot implements ModInitializer {
    public static Scoreboards scoreboard;

    @Override
    public void onInitialize() {
        SettingsManager.INSTANCE.initSettings();
        scoreboard = new Scoreboards();
    }
}
