package hammer.hammerbot.settings;

import hammer.hammerbot.HammerBot;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class SettingsManager {

    private static final File settingsFile = new File("config/hammerbot.properties");

    public static SettingsFile settings = new SettingsFile();

    public static void initSettings() {
        if (!settingsFile.exists()) {
            try {
                createDefault();
            } catch (IOException e) {
                HammerBot.LOGGER.info("Could not create default settings file.");
            }
        }

        loadSettings();
        validateSettings();
    }

    private static void loadSettings() {
        try (Reader reader = new FileReader(settingsFile)) {
            Properties properties = new Properties();
            properties.load(reader);
            settings.serverType = properties.getProperty("serverType", settings.serverType);
            settings.botToken = properties.getProperty("botToken", settings.botToken);
            settings.commandPrefix = properties.getProperty("commandPrefix", settings.commandPrefix);
            settings.friendRoleId = Long.parseLong(properties.getProperty("friendRoleId", Long.toString(settings.friendRoleId)));
            settings.memberRoleId = Long.parseLong(properties.getProperty("memberRoleId", Long.toString(settings.memberRoleId)));
            settings.adminRoleId = Long.parseLong(properties.getProperty("adminRoleId", Long.toString(settings.adminRoleId)));
            settings.linkChannelId = Long.parseLong(properties.getProperty("linkChannelId", Long.toString(settings.linkChannelId)));
            settings.opOnJoin = Boolean.parseBoolean(properties.getProperty("opOnJoin", Boolean.toString(settings.opOnJoin)));
        } catch (IOException e) {
            throw new RuntimeException("Settings file is broken");
        }
    }

    private static void validateSettings() {
        if (settings.botToken.length() == 0) throw new RuntimeException("botToken is null, throwing error");
        if (settings.serverType.length() == 0) HammerBot.LOGGER.info("serverType is not set");
    }

    private static void createDefault() throws IOException {
        // this will create the config dir for me
        FabricLoader.getInstance().getConfigDir();

        try (Writer writer = new FileWriter(SettingsManager.settingsFile)) {
            writer.write("#Configuration File For HammerBot");
            Properties properties = new Properties();
            properties.setProperty("serverType", settings.serverType);
            properties.setProperty("botToken", settings.botToken);
            properties.setProperty("friendRoleId", Long.toString(settings.friendRoleId));
            properties.setProperty("memberRoleId", Long.toString(settings.memberRoleId));
            properties.setProperty("adminRoleId", Long.toString(settings.adminRoleId));
            properties.setProperty("linkChannelId", Long.toString(settings.linkChannelId));
            properties.store(writer, null);
        }
    }
}
