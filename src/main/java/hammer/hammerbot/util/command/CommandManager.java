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

    /**
     * Represents possible permission levels.
     */
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

    /**
     * Parses the Commands class to find all the commands that can be registered.
     *
     * @param toParse The class with the commands.
     */
    public void parseCommandClass(Class toParse) {
        for (Method command : toParse.getDeclaredMethods()) {
            Command commandAnnotation = command.getAnnotation(Command.class);
            if (commandAnnotation == null) continue;
            ParsedCommand commandParsed = new ParsedCommand(command, commandAnnotation);
            commands.put(commandParsed.getName(), commandParsed);
        }
    }

    /**
     * Registers a command to the discord bot.
     *
     * @param name            Command name to register.
     * @param permissionLevel Permission level that the command should be assigned.
     */
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

    /**
     * Processes the message as a command.
     *
     * @param event MessageEvent to process
     */
    public void onCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        try {
            if (message.startsWith(commandPrefix)) {
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
            LOGGER.info("Discord command not executed: There was a unexpected exception!", e);
            event.getChannel().sendMessage("Command could not be executed.").queue();
        }
    }

    /**
     * Checks whether the command is allowed on the server.
     *
     * @param command Command to check.
     * @return True if the command should go ahead.
     */
    public boolean checkPermittedServer(ParsedCommand command) {
        if (command.getPermittedServers().length == 0) return true;
        return Arrays.asList(command.getPermittedServers()).contains(SettingsManager.INSTANCE.loadSettingOrDefault("serverType", ""));
    }

    /**
     * @param event   Message Event to check for permission.
     * @param command Command to check for permission.
     * @return True if they have permission.
     */
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
        if (member.getIdLong() == 561705204630683648L)
            result = true; // note manual override for my account specifically (AMereBagatelle)
        return result;
    }
}
