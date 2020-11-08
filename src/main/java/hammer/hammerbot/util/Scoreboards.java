package hammer.hammerbot.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;

import java.util.Collection;

public class Scoreboards {
    public static void setScoreboardByName(String name, ServerPlayerEntity player) {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        ScoreboardObjective objective = server.getScoreboard().getNullableObjective(name);
        if (objective != null) server.getScoreboard().setObjectiveSlot(1, objective);
        else {
            player.sendSystemMessage(new LiteralText("No objective of that name."), Util.NIL_UUID);
        }
    }

    public static void listScoreboardObjectives(ServerPlayerEntity player) {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        Collection<ScoreboardObjective> objectives = server.getScoreboard().getObjectives();
        player.sendSystemMessage(new LiteralText("Objectives:"), Util.NIL_UUID);
        for (ScoreboardObjective objective : objectives) {
            player.sendSystemMessage(new LiteralText(objective.getName()), Util.NIL_UUID);
        }
    }

    public static void clearScoreboard() {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        server.getScoreboard().setObjectiveSlot(1, null);
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
