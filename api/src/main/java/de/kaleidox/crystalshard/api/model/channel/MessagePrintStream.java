package de.kaleidox.crystalshard.api.model.channel;

import java.io.OutputStream;
import java.io.PrintStream;

import org.jetbrains.annotations.NotNull;

public abstract class MessagePrintStream extends PrintStream {
    public MessagePrintStream(@NotNull OutputStream out) {
        super(out, true);
    }
}
