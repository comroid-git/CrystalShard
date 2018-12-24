package de.kaleidox.crystalshard.internal.items.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.intellij.lang.annotations.MagicConstant;

import de.kaleidox.crystalshard.api.entity.message.MessageActivity;

import java.util.Optional;

public class MessageActivityInternal implements MessageActivity {
    @MagicConstant(valuesFromClass = MessageActivity.Type.class)
    private final int type;
    private final String partyId;

    @SuppressWarnings("MagicConstant")
    public MessageActivityInternal(JsonNode data) {
        this.type = data.get("type").asInt();
        this.partyId = data.get("party_id")
                .asText(null);
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public Optional<String> getPartyId() {
        return Optional.ofNullable(partyId);
    }
}
