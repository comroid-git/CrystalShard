package de.comroid.crystalshard.api.model.channel;

import java.io.OutputStream;
import java.io.PrintStream;

import de.comroid.crystalshard.api.entity.channel.TextChannel;

import org.jetbrains.annotations.NotNull;

public final class MessagePrintStream extends PrintStream {
    private final TextChannel channel;

    public MessagePrintStream(TextChannel channel, @NotNull OutputStream out) {
        super(out, true);
        this.channel = channel;
    }

    public TextChannel getChannel() {
        return channel;
    }
}
