package hammer.hammerbot.util.command;

public @interface Command {
    String name();

    String desc();

    String[] permittedRoles() default {};
}
