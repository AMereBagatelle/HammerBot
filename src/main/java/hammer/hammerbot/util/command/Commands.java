package hammer.hammerbot.util.command;

import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import java.io.File;

public class Commands {
    public static MessageReceivedEvent currentEvent;

    @Command(
            desc = "Whitelisting command for servers. Structure: /whitelist server <add/remove> player",
            permittedServers = {"CMPFLAT", "CMPCOPY", "SMP"}
    )
    public static void whitelist(String serverType, String addOrRemove, String player) throws Exception {
        if (serverType.equalsIgnoreCase(SettingsManager.INSTANCE.loadSettingOrDefault("serverType", ""))) {
            if (addOrRemove.equals("add")) {
                MinecraftDedicatedServer server = (MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance();
                server.enqueueCommand("whitelist add " + player, server.getCommandSource());
                currentEvent.getChannel().sendMessage(String.format("Player %s added to %s", player, SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP"))).queue();
            } else if (addOrRemove.equals("remove")) {
                MinecraftDedicatedServer server = (MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance();
                server.enqueueCommand("whitelist remove " + player, server.getCommandSource());
                currentEvent.getChannel().sendMessage(String.format("Player %s removed from %s.", player, SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP"))).queue();
            } else {
                throw new Exception("Invalid argument(s)");
            }
        }
    }

    @Command(
            desc = "Prints out currently online players on the server.",
            permittedServers = {"SMP", "CMPCOPY", "CMPFLAT"}
    )
    public static void online(String serverType) {
        if (serverType.equalsIgnoreCase(SettingsManager.INSTANCE.loadSettingOrDefault("serverType", ""))) {
            MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
            String[] players = server.getPlayerManager().getPlayerNames();
            StringBuilder finalMessage = new StringBuilder(String.format("Currently online players on %s:\n", SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP")));
            for (String player : players) {
                finalMessage.append(player).append("\n");
            }
            if (players.length == 0) {
                finalMessage = new StringBuilder(String.format("No players currently online on %s!", SettingsManager.INSTANCE.loadSettingOrDefault("serverType", "SMP")));
            }
            currentEvent.getChannel().sendMessage(finalMessage.toString()).queue();
        }
    }

    @Command(
            desc = "Uploads a file (scarpet script, schematic, etc) that is attached to the message to the server.",
            permittedServers = {"CMPFLAT", "CMPCOPY"}
    )
    public static void uploadFile(String serverType) {
        if (serverType.equalsIgnoreCase(SettingsManager.INSTANCE.loadSettingOrDefault("serverType", ""))) {
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

}
