package hammer.hammerbot;

import hammer.hammerbot.settings.SettingsManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public class HammerBot implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        SettingsManager.INSTANCE.initSettings();
    }
}
