package hammer.hammerbot.util.command;

import java.lang.reflect.Method;

public class ParsedCommand {
    private final Method method;
    private final Command annotation;
    private final String name;
    private CommandManager.Roles permissionLevel;

    public ParsedCommand(Method method, Command annotation) {
        this.method = method;
        this.annotation = annotation;
        this.name = annotation.name().equals("") ? method.getName() : annotation.name();
    }

    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }

    public String getDescription() {
        return annotation.desc();
    }

    public String[] getPermittedServers() {
        return annotation.permittedServers();
    }

    public void setPermissionLevel(CommandManager.Roles permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public CommandManager.Roles getPermissionLevel() {
        return permissionLevel;
    }
}
