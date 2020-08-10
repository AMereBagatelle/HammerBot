package hammer.hammerbot.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.MessageType;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Collection;

public class Scoreboards {
    public static void setScoreboardByName(String name, ServerPlayerEntity player) {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        ScoreboardObjective objective = server.getScoreboard().getNullableObjective(name);
        if (objective != null) server.getScoreboard().setObjectiveSlot(1, objective);
        else {
            player.sendChatMessage(new LiteralText("No objective of that name."), MessageType.SYSTEM);
        }
    }

    public static void listScoreboardObjectives(ServerPlayerEntity player) {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        Collection<ScoreboardObjective> objectives = server.getScoreboard().getObjectives();
        player.sendChatMessage(new LiteralText("Objectives:"), MessageType.SYSTEM);
        for (ScoreboardObjective objective : objectives) {
            player.sendChatMessage(new LiteralText(objective.getName()), MessageType.SYSTEM);
        }
    }

    public static boolean checkPlayerOnWhitelist(MinecraftServer server, String nameToCheck) {
        for (String name : server.getPlayerManager().getWhitelistedNames()) {
            if (nameToCheck.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
