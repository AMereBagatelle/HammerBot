package hammer.hammerbot.util.command;

import net.dv8tion.jda.api.entities.MessageChannel;

public class Commands {
    public static MessageChannel currentChannel;

    @Command(
            desc = "Whitelisting command.",
            permittedServers = {"CMPFLAT"}
    )
    public void whitelist(boolean add, String player) {
        currentChannel.sendMessage("Whitelist").queue();
    }

}
