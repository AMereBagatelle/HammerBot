package hammer.hammerbot.bot;

import hammer.hammerbot.bot.cmpcopy.CMPCopyCommandListener;
import hammer.hammerbot.bot.cmpflat.CMPFlatCommandListener;
import hammer.hammerbot.bot.smp.SMPCommandListener;
import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

public class Bot {
    private JDA bot;
    private Logger LOGGER = LogManager.getLogger();

    public Bot() {
        try {
            bot = JDABuilder.createDefault(SettingsManager.INSTANCE.loadSettingOrDefault("botToken", "")).build();
            switch(SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP")) {
                case "SMP":
                    bot.addEventListener(new SMPCommandListener());
                    LOGGER.info("SMP bot activated.");
                    break;

                case "CMPFLAT":
                    bot.addEventListener(new CMPFlatCommandListener());
                    LOGGER.info("CMPFLAT bot activated.");
                    break;

                case "CMPCOPY":
                    bot.addEventListener(new CMPCopyCommandListener());
                    LOGGER.info("CMPCOPY bot activated.");
                    break;

                default:
                    LOGGER.info("Unrecognized server type.");
            }
            bot.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
