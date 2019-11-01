package de.comroid.crystalshard.api.model.message;

// https://discordapp.com/developers/docs/resources/channel#message-object-message-activity-structure
// https://discordapp.com/developers/docs/resources/channel#message-object-message-activity-types

import java.util.Optional;

import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTrait;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonTrait.simple;

@JsonTraits(MessageActivity.Trait.class)
public interface MessageActivity extends JsonDeserializable {
    default Type getType() {
        return getTraitValue(Trait.TYPE);
    }

    default Optional<String> getPartyID() {
        return wrapTraitValue(Trait.PARTY_ID);
    }

    interface Trait {
        JsonTrait<Integer, Type> TYPE = simple(JsonNode::asInt, "type", Type::valueOf);
        JsonTrait<String, String> PARTY_ID = identity(JsonNode::asText, "party_id");
    }

    enum Type {
        JOIN(1),
        SPECTATE(2),
        LISTEN(3),
        JOIN_REQUEST(5);

        public final int value;

        Type(int value) {
            this.value = value;
        }

        public static @Nullable Type valueOf(int value) {
            for (Type type : values())
                if (type.value == value)
                    return type;

            return null;
        }
    }
}
