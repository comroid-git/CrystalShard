package de.comroid.crystalshard.impl.model.channel;

import java.io.OutputStream;

import de.comroid.crystalshard.api.entity.channel.TextChannel;

public class ChannelOutputStream extends OutputStream {
    private final TextChannel textChannel;

    private StringBuilder str;

    public ChannelOutputStream(TextChannel textChannel) {
        this.textChannel = textChannel;

        str = new StringBuilder();
    }

    @Override
    public void write(int b) {
        char c = (char) b;

        if (c == '\n')
            flush();
        else str.append((char) b);
    }

    @Override
    public void flush() {
        textChannel.composeMessage()
                .addText(str.toString())
                .send();

        str = new StringBuilder();
    }
}
