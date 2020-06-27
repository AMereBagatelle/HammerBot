package hammer.hammerbot.bot;

import hammer.hammerbot.util.command.CommandManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        CommandManager.INSTANCE.onCommand(event);
    }
}
