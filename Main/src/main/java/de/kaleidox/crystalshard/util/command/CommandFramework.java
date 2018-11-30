package de.kaleidox.crystalshard.util.command;

import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import java.lang.reflect.Method;
import java.util.List;

public interface CommandFramework {
    void registerCommands(Class commandClass) throws IllegalArgumentException, IllegalStateException;

    List<Command> getCommands();

    Discord getDiscord();

    void enable();

    void disable();

    void ignore(TextChannel object);

    boolean unignore(TextChannel object);

    void registerCommands(Method commandMethod) throws IllegalArgumentException, IllegalStateException;

    boolean unregisterCommand(Method commandMethod);
}
