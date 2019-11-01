package de.comroid.crystalshard.impl.entity.channel;

import java.time.Instant;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.model.channel.MessagePrintStream;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.impl.model.channel.ChannelOutputStream;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractTextChannel<Self extends AbstractTextChannel<Self>>
        extends AbstractChannel<Self>
        implements TextChannel {
    protected @JsonData("last_message_id") long lastMessageId;
    protected @JsonData("rate_limit_per_user") int rateLimitPerUser;
    protected @JsonData("last_pin_timestamp") Instant lastPinTimestamp;

    protected AbstractTextChannel(Discord api, JsonNode data) {
        super(api, data);
    }

    @Override
    public MessagePrintStream openPrintStream() {
        return new MessagePrintStream(this, new ChannelOutputStream(this));
    }
}
