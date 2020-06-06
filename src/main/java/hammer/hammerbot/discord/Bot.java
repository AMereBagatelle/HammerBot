package hammer.hammerbot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Bot {
    private JDA bot;
    public boolean botActive = false;

    public Bot() {
        try {
            bot = JDABuilder.createDefault("Njk2NTE0ODkwMzYxMzM5OTM0.Xtt_Mw.zn6U2jajIjnCVGUzoB4EP9fEmYQ").build(); //TODO: Get actual bot token settings working
            bot.addEventListener(new CommandMessageListener());
            bot.awaitReady();
            botActive = true;
        } catch(LoginException | InterruptedException e) {
            bot = null;
        }
    }

    public JDA getBotInstance() {
        return bot;
    }
}
