package de.kaleidox.crystalshard.util.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Command {
    String[] aliases();
}
