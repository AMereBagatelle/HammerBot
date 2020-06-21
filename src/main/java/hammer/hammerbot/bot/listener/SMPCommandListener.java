package hammer.hammerbot.bot.listener;

import hammer.hammerbot.bot.listener.MainCommandListener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

public class SMPCommandListener extends MainCommandListener {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
    }
}
