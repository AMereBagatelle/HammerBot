package hammer.hammerbot.bot;

import hammer.hammerbot.bot.cmpcopy.CMPCopyCommandListener;
import hammer.hammerbot.bot.cmpflat.CMPFlatCommandListener;
import hammer.hammerbot.bot.smp.SMPCommandListener;
import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Bot {
    public static Bot INSTANCE = new Bot();
    private JDA bot;

    public Bot() {
        try {
            bot = JDABuilder.createDefault(SettingsManager.INSTANCE.loadSettingOrDefault("botToken", "")).build();
            switch(SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP")) {
                case "SMP":
                    bot.addEventListener(new SMPCommandListener());
                    break;

                case "CMPFLAT":
                    bot.addEventListener(new CMPFlatCommandListener());
                    break;

                case "CMPCOPY":
                    bot.addEventListener(new CMPCopyCommandListener());
                    break;
            }
            bot.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
