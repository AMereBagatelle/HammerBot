package hammer.hammerbot;

import hammer.hammerbot.settings.SettingsManager;
import net.fabricmc.api.ModInitializer;

public class HammerBot implements ModInitializer {
    @Override
    public void onInitialize() {
        SettingsManager.INSTANCE.initSettings();
    }
}
