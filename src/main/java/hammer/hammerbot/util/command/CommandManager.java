package hammer.hammerbot.util.command;

import hammer.hammerbot.settings.SettingsManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {
    public static CommandManager INSTANCE = new CommandManager();

    public enum Roles {
        ADMIN,
        MEMBER,
        COMRADE,
        EVERYONE
    }

    public static final Logger LOGGER = LogManager.getLogger();
    public String commandPrefix = SettingsManager.INSTANCE.loadSettingOrDefault("commandPrefix", "");
    public HashMap<String, ParsedCommand> commands = new HashMap<>();
    private final HashMap<String, ParsedCommand> activeCommands = new HashMap<>();

    public void parseCommandClass(Class toParse) {
        for (Method command : toParse.getDeclaredMethods()) {
            Command commandAnnotation = command.getAnnotation(Command.class);
            if (commandAnnotation == null) continue;
            ParsedCommand commandParsed = new ParsedCommand(command, commandAnnotation);
            commands.put(commandParsed.getName(), commandParsed);
        }
    }

    public void registerCommand(String name, Roles permissionLevel) {
        ParsedCommand command = commands.get(name);
        if (command != null) {
            command.setPermissionLevel(permissionLevel);
            activeCommands.put(command.getName(), command);
        }
    }

    public void removeCommand(String name) {
        activeCommands.remove(name);
    }

    public void onCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        try {
            if (message.startsWith("/")) {
                message = message.substring(1);
                for (ParsedCommand command : activeCommands.values()) {
                    if (message.startsWith(command.getName()) && checkPermittedServer(command) && checkPermissionLevel(event, command)) {
                        String[] arguments = null;
                        if (message.length() != command.getName().length()) {
                            message = message.substring(command.getName().length() + 1);
                            arguments = message.split("\\s");
                        }
                        Commands.currentEvent = event;
                        command.getMethod().invoke(Commands.class.newInstance(), arguments);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            LOGGER.info("Discord command not executed: Wrong amount of arguments!");
            event.getChannel().sendMessage("Command could not be executed: Wrong number of arguments.").queue();
        } catch (Exception e) {
            LOGGER.info("Discord command not executed: There was a unexpected exception!");
            event.getChannel().sendMessage("Command could not be executed.").queue();
        }
    }

    public boolean checkPermittedServer(ParsedCommand command) {
        if (command.getPermittedServers().length == 0) return true;
        return Arrays.asList(command.getPermittedServers()).contains(SettingsManager.INSTANCE.loadSettingOrDefault("serverType", ""));
    }

    public boolean checkPermissionLevel(MessageReceivedEvent event, ParsedCommand command) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
        boolean result = false;
        switch (command.getPermissionLevel()) {
            case EVERYONE:
                result = true;

            case COMRADE:
                if (member.getRoles().contains(guild.getRoleById(SettingsManager.INSTANCE.loadLongSettingOrDefault("comradeRoleId", 0)))) {
                    result = true;
                    break;
                }

            case MEMBER:
                if (member.getRoles().contains(guild.getRoleById(SettingsManager.INSTANCE.loadLongSettingOrDefault("memberRoleId", 0)))) {
                    result = true;
                    break;
                }

            case ADMIN:
                if (member.getRoles().contains(guild.getRoleById(SettingsManager.INSTANCE.loadLongSettingOrDefault("adminRoleId", 0)))) {
                    result = true;
                    break;
                }
        }
        return result;
    }
}
