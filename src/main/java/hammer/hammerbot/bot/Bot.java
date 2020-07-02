package hammer.hammerbot.bot;

import hammer.hammerbot.settings.SettingsManager;
import hammer.hammerbot.util.command.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

public class Bot {
    private JDA bot;
    private final Logger LOGGER = LogManager.getLogger();

    public Bot() {
        try {
            bot = JDABuilder.createDefault(SettingsManager.INSTANCE.loadSettingOrDefault("botToken", "")).build();
            bot.addEventListener(new CommandListener());
            bot.addEventListener(new ChatbridgeListener());
            CommandManager commandManager = CommandManager.INSTANCE;
            switch (SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP")) {
                case "SMP":
                    commandManager.registerCommand("whitelist", CommandManager.Roles.ADMIN);
                    commandManager.registerCommand("online", CommandManager.Roles.EVERYONE);
                    commandManager.registerCommand("scoreboard", CommandManager.Roles.EVERYONE);
                    break;

                case "CMPFLAT":

                case "CMPCOPY":
                    commandManager.registerCommand("whitelist", CommandManager.Roles.MEMBER);
                    commandManager.registerCommand("online", CommandManager.Roles.EVERYONE);
                    commandManager.registerCommand("uploadFile", CommandManager.Roles.MEMBER);
                    commandManager.registerCommand("help", CommandManager.Roles.EVERYONE);
                    break;

                default:
                    LOGGER.info("Unrecognized server type.");
            }
            bot.awaitReady();
        } catch (LoginException | InterruptedException e) {
            LOGGER.info(e.getStackTrace());
        }
    }

    public JDA getBot() {
        return bot;
    }
}
