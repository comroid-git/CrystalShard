package org.comroid.crystalshard.impl.model.channel;

import java.io.OutputStream;

import org.comroid.crystalshard.api.entity.channel.TextChannel;

public class ChannelOutputStream extends OutputStream {
    private StringBuilder str;
    TextChannel textChannel;

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
        waitForChannel();

        textChannel.composeMessage()
                .addText(str.toString())
                .send();

        str = new StringBuilder();
    }

    private void waitForChannel() {
        try {
            while (textChannel == null)
                this.wait(80);
        } catch (InterruptedException IEx) {
            throw new RuntimeException("Could not wait for channel", IEx);
        }
    }
}
