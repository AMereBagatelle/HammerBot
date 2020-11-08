package hammer.hammerbot.bot;

import hammer.hammerbot.HammerBot;
import hammer.hammerbot.settings.SettingsManager;
import hammer.hammerbot.util.command.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Bot {
    private final JDA bot;

    /**
     * Set up the bot.
     * Has separate cases for each server to register commands for each server.  Also registers ChatbridgeListener.
     */
    public Bot() {
        try {
            bot = JDABuilder.createDefault(SettingsManager.settings.botToken).build();
            bot.addEventListener(new CommandListener());
            bot.addEventListener(new ChatbridgeListener());
            CommandManager commandManager = CommandManager.INSTANCE;
            switch (SettingsManager.settings.serverType) {
                case "SMP":
                    commandManager.registerCommand("whitelist", CommandManager.Roles.ADMIN);
                    commandManager.registerCommand("online", CommandManager.Roles.EVERYONE);
                    commandManager.registerCommand("scoreboard", CommandManager.Roles.EVERYONE);
                    commandManager.registerCommand("ping", CommandManager.Roles.EVERYONE);
                    break;

                case "CMPFLAT":

                case "CMPCOPY":
                    commandManager.registerCommand("whitelist", CommandManager.Roles.MEMBER);
                    commandManager.registerCommand("online", CommandManager.Roles.EVERYONE);
                    commandManager.registerCommand("uploadFile", CommandManager.Roles.MEMBER);
                    commandManager.registerCommand("ping", CommandManager.Roles.MEMBER);
                    break;

                default:
                    HammerBot.LOGGER.info("Unrecognized server type.");
            }
            bot.awaitReady();
        } catch (LoginException | InterruptedException e) {
            throw new RuntimeException("Could not log in.");
        }
    }

    public JDA getBot() {
        return bot;
    }
}
