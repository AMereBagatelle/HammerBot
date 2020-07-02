package hammer.hammerbot.settings;

import java.io.*;
import java.util.Properties;

public class SettingsManager {
    public static SettingsManager INSTANCE = new SettingsManager();
    public String[][] settings = new String[][]{
            {"botToken", ""},
            {"serverType", "SMP"},
            {"memberRoleId", ""},
            {"adminRoleId", ""},
            {"comradeRoleId", ""},
            {"commandPrefix", "/"},
            {"linkChannelId", "728334101706440715"}
    };

    public static File settingsFile = new File("hammerbot.properties");

    public void initSettings() {
        // Init settings file
        if(!settingsFile.exists()) {
            try {
                boolean fileCreated = settingsFile.createNewFile();

                if (fileCreated) {
                    Properties prop = new Properties();
                    for (String[] setting : settings) {
                        prop.put(setting[0], setting[1]);
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile));
                    prop.store(writer, null);
                    writer.flush();
                    writer.close();
                } else {
                    throw new IOException();
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create settings file for DiscordConnect!");
            }
        }
        try {
            Properties settingProperties = new Properties();
            settingProperties.load(new BufferedReader(new FileReader(settingsFile)));
            for (String[] setting : settings) {
                if(!settingProperties.containsKey(setting[0])) {
                    settingProperties.put(setting[0], setting[1]);
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile));
            settingProperties.store(writer, null);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not read settings file for DiscordConnect!");
        }

        // Special settings, in the case that we absolutely need something to be set.
        if (loadSettingOrDefault("botToken", "").length() == 0) throw new RuntimeException("Set a bot token!");
    }

    public String loadSettingOrDefault(String setting, String normal) {
        BufferedReader reader;
        Properties prop = new Properties();

        try {
            reader = new BufferedReader(new FileReader(settingsFile));
            prop.load(reader);
            reader.close();

            return prop.getProperty(setting, normal);
        } catch (IOException e) {
            throw new RuntimeException("Can't read settings for DiscordConnect!");
        }
    }

    public boolean loadBooleanSettingOrDefault(String setting, boolean normal) {
        return Boolean.parseBoolean(loadSettingOrDefault(setting, Boolean.toString(normal)));
    }

    public int loadIntSettingOrDefault(String setting, int normal) {
        return Integer.parseInt(loadSettingOrDefault(setting, Integer.toString(normal)));
    }

    public long loadLongSettingOrDefault(String setting, long normal) {
        return Long.parseLong(loadSettingOrDefault(setting, Long.toString(normal)));
    }
}
