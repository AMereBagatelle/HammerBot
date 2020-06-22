package hammer.hammerbot.util.command;

public class CommandList {
    @Command(
            name = "whitelist",
            desc = "Whitelisting command",
            permittedRoles = {"admin"}
    )
    public void whitelist(String addOrRemove, String player) {
    }

}
