package hammer.hammerbot.util.command;

import hammer.hammerbot.settings.SettingsManager;
import hammer.hammerbot.util.Scoreboards;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import java.io.File;
import java.util.List;

public class Commands {
    public static MessageReceivedEvent currentEvent;

    @Command(
            desc = "Whitelisting command for servers. Usage: `/whitelist server <add/remove> player`.",
            permittedServers = {"CMPFLAT", "CMPCOPY", "SMP"},
            arguments = {"serverType", "addOrRemove", "player"}
    )
    public static void whitelist(String serverType, String addOrRemove, String player) throws Exception {
        if (serverType.equalsIgnoreCase(SettingsManager.settings.serverType)) {
            if (addOrRemove.equals("add")) {
                MinecraftDedicatedServer server = (MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance();
                server.enqueueCommand("whitelist add " + player, server.getCommandSource());
                currentEvent.getChannel().sendMessage(String.format("Player %s added to %s", player, SettingsManager.settings.serverType)).queue();
            } else if (addOrRemove.equals("remove")) {
                MinecraftDedicatedServer server = (MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance();
                server.enqueueCommand("whitelist remove " + player, server.getCommandSource());
                currentEvent.getChannel().sendMessage(String.format("Player %s removed from %s.", player, SettingsManager.settings.serverType)).queue();
            } else {
                throw new Exception("Invalid argument(s)");
            }
        }
    }

    @Command(
            desc = "Prints out currently online players on the server.  Usage: `/online server`.",
            permittedServers = {"SMP", "CMPCOPY", "CMPFLAT"},
            arguments = {"serverType"}
    )
    public static void online(String serverType) {
        if (serverType.equalsIgnoreCase(SettingsManager.settings.serverType)) {
            MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
            String[] players = server.getPlayerManager().getPlayerNames();
            StringBuilder finalMessage = new StringBuilder(String.format("Currently online players on %s:\n", SettingsManager.settings.serverType));
            for (String player : players) {
                finalMessage.append(player).append("\n");
            }
            if (players.length == 0) {
                finalMessage = new StringBuilder(String.format("No players currently online on %s!", SettingsManager.settings.serverType));
            }
            currentEvent.getChannel().sendMessage(finalMessage.toString()).queue();
        }
    }

    @Command(
            desc = "Uploads a file (scarpet script, schematic, etc) that is attached to the message to the server.  Usage: `/uploadFile server` (attach the file you would like to upload).",
            permittedServers = {"CMPFLAT", "CMPCOPY"},
            arguments = {"serverType"}
    )
    public static void uploadFile(String serverType) {
        if (serverType.equalsIgnoreCase(SettingsManager.settings.serverType)) {
            for (Message.Attachment fileAttachment : currentEvent.getMessage().getAttachments()) {
                String fileExtension = fileAttachment.getFileExtension();
                if (fileExtension.equals("sc")) {
                    File downloadPath = new File("world/scripts/" + fileAttachment.getFileName());
                    fileAttachment.downloadToFile(downloadPath)
                            .thenAccept(file -> currentEvent.getChannel().sendMessage("Uploaded file " + fileAttachment.getFileName() + " to " + serverType).queue());
                }
            }
        }
    }

    @Command(
            desc = "Shows a scoreboard of the selected objective.  Usage: `/scoreboard objective`",
            permittedServers = {"SMP"},
            arguments = {"objective"}
    )
    public static void scoreboard(String objective) {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        ScoreboardObjective scoreboardObjective = server.getScoreboard().getNullableObjective(objective);
        if (scoreboardObjective != null) {
            List<ScoreboardPlayerScore> playerScores = (List<ScoreboardPlayerScore>) server.getScoreboard().getAllPlayerScores(scoreboardObjective);
            StringBuilder builder = new StringBuilder();
            builder.append("```css\n");
            builder.append("-------- ").append(objective).append(" --------\n");
            for (int i = playerScores.size() - 1; i > 0; i--) {
                if (Scoreboards.checkPlayerOnWhitelist(server, playerScores.get(i).getPlayerName())) {
                    builder.append(playerScores.get(i).getPlayerName()).append(": ").append(playerScores.get(i).getScore()).append("\n");
                }
            }
            builder.append("```");
            if (!builder.toString().equals("")) currentEvent.getChannel().sendMessage(builder.toString()).queue();
            else currentEvent.getChannel().sendMessage("No one has done that!");
        } else {
            currentEvent.getChannel().sendMessage("Invalid objective.").queue();
        }
    }

    @Command(
            desc = "Lets us know what servers are up.",
            permittedServers = {"SMP", "CMPFLAT", "CMPCOPY"}
    )
    public static void ping() {
        currentEvent.getChannel().sendMessage(String.format("%s is up!", SettingsManager.settings.serverType)).queue();
    }
}
