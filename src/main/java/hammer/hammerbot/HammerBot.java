package hammer.hammerbot;

import hammer.hammerbot.discord.Bot;
import net.fabricmc.api.ModInitializer;

public class HammerBot implements ModInitializer {
    public static Bot discordBot;
    public static Scoreboards scoreboard;

    @Override
    public void onInitialize() {
        discordBot = new Bot();
        scoreboard = new Scoreboards();
    }
}
