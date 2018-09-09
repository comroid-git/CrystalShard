package de.kaleidox.crystalshard.internal.items.message;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.items.message.MessageActivity;
import de.kaleidox.crystalshard.main.items.message.MessageActivityType;

import java.util.Optional;

public class MessageActivityInternal implements MessageActivity {
    private final MessageActivityType type;
    private final String partyId;

    public MessageActivityInternal(JsonNode data) {
        this.type = MessageActivityType.getById(data.get("type").asInt());
        this.partyId = data.get("party_id").asText(null);
    }

    @Override
    public MessageActivityType getType() {
        return type;
    }

    @Override
    public Optional<String> getPartyId() {
        return Optional.ofNullable(partyId);
    }
}
