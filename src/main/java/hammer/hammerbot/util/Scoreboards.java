package hammer.hammerbot.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.MessageType;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class Scoreboards {
    public static void setScoreboardByName(String name, ServerPlayerEntity player) {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        ScoreboardObjective objective = server.getScoreboard().getNullableObjective(name);
        if (objective != null) server.getScoreboard().setObjectiveSlot(1, objective);
        else {
            player.sendChatMessage(new LiteralText("No objective of that name."), MessageType.SYSTEM);
        }
    }
}
