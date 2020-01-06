package org.comroid.crystalshard.api.model.channel;

import java.io.OutputStream;
import java.io.PrintStream;

import org.comroid.crystalshard.api.entity.channel.TextChannel;

import org.jetbrains.annotations.NotNull;

public abstract class MessagePrintStream extends PrintStream {
    protected TextChannel channel;

    protected MessagePrintStream(TextChannel channel, @NotNull OutputStream out) {
        super(out, true);

        this.channel = channel;
    }

    protected MessagePrintStream(OutputStream out) {
        super(out);
    }

    public TextChannel getChannel() {
        return channel;
    }
}
