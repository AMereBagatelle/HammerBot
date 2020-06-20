package hammer.hammerbot.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;

public class Scoreboards {
    public static void setScoreboardByName(String name) {
        MinecraftServer server = (MinecraftServer)FabricLoader.getInstance().getGameInstance();
        ScoreboardObjective objective = server.getScoreboard().getNullableObjective(name);
        if(objective != null) server.getScoreboard().setObjectiveSlot(1, objective); else {
            server.getPlayerManager().sendToAll(new LiteralText("No objective of that name."));
        }
    }
}
